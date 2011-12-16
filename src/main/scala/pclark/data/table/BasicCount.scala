package pclark.data.table

import pclark.data._
import scala.io.Source
import scala.collection.immutable.TreeMap

/*
	Counts stores the count of category values seen thus far for a given variable.
	It's represented internally as a map of Map[Variable, int] to ints. The keys are Maps
	and the values (ints) represent counts of how many times the key has been seen in the file thus far.
	
	TODO - deal with households at a time, and deal with PERWT.
	
	This class is probably obsolete.
*/

class BasicCount(val name:String, val weight:Int) {

	// keys in the table are maps of Variables to variable values.
	type CountKey = scala.collection.immutable.Map[Variable, Int]
	
	val table = new scala.collection.mutable.HashMap[CountKey, Long]()
	val descriptions = new scala.collection.mutable.HashMap[CountKey, String]()

  def size = table size
  def keySet = table keySet
	
	// aux constructor - often, the weight is one and we can just leave it at that.
	def this(n:String) = this(n, 1)
	
	// explicitly set the value for a given key.
	def setValueForKey(key:CountKey, a:Long) {
	  table += (key -> a)
	}

	def setValueForKey(key:CountKey, a:Long, desc:String) {
	  setValueForKey(key, a)
	  setDescForKey(key, desc)
	}

  // Load up values for a bunch of variables - useful for bootstrapping tables from agg data files.
	def setValuesForKeys[T <: Variable](dataToLoad:Map[T, Long]) {
	  dataToLoad.foreach((e) => setValueForKey(e._1 defaultKey, e._2))
	}

	def setDescForKey(key:CountKey, desc:String) {
	  descriptions += (key->desc)
  }
  
	// increment the value for the given key
	def incrementCountForKey(key:CountKey) {
		val oldVal:Long = table.getOrElse(key, 0)
		table += (key -> (oldVal + weight))
  }	 

	// decrement the value for the given key
	def decrementCountForKey(key:CountKey) {
		  val oldVal:Long = table.getOrElse(key, weight)
		  table += (key -> (oldVal - weight))
  }

	// increment the value for the list of given keys - useful when dealing with a table that involves totals as well as single categories.
	def incrementCountForKey(keys:List[CountKey]) {
	  for(key <- keys) {
		  incrementCountForKey(key)
		}
  }	 

	// decrement the value for the list of given keys 
	def decrementCountForKey(keys:List[CountKey]) {
	  for(key <- keys) {
		  decrementCountForKey(key)
		}
  }

	// increment the value for the given key, where Not In Universe is a possibility
	def incrementCountForKey(key:Option[CountKey]) {
	  key match {
	    case Some(index) => incrementCountForKey(index)
		  case None => null
		}
  }	 

	// decrement the value for the given key, where Not In Universe is a possibility
	def decrementCountForKey(key:Option[CountKey]) {
	  key match {
	    case Some(index) => decrementCountForKey(index)
		  case None => null
		}
  }

	def apply(key: CountKey) : Long = {
			countFor(key)
	}

	def countFor(key: CountKey) : Long = {
	  table.getOrElse(key, 0)
	}
	
	// return the difference between two tables for the same key
	def deltaForKey(that:BasicCount, thisKey:CountKey) : Double = {
		countFor(thisKey) - that.countFor(thisKey)
	}
	
	// compare two BasicCount tables. Returns a sum-of-squares delta between the two tables.
	def compare(that:BasicCount) : Double = {
	  // note that sets don't allow duplicates - unioning the two keySets should ensure that we don't have duplicates.
	  val allKeys = (this.keySet ++ that.keySet).toList.sortWith(countKeyLessThan)
	  
	  // for each key, fold along the list doing the following:
	  // get the value for that key from this and from that.
	  // Subtract and square, and add that to the sumSoFar.
	  // the result of the foldLeft operation is the value of the function.
	  allKeys.foldLeft(0.0)((sumSoFar: Double, thisKey:CountKey) => {
			val delta = deltaForKey(that, thisKey)
			sumSoFar + (delta * delta)
		})
	}
	
	// return some stats useful for debugging table comparison
	def comparisonInfo(that:BasicCount) : String = {
	  val allKeys = (this.keySet ++ that.keySet).toList.sortWith(countKeyLessThan)
	  val result = new StringBuilder
	  allKeys.foreach((tableKey:CountKey) => result.append("Delta for key: " + prettyprintKey(tableKey) + " is " + deltaForKey(that, tableKey) + "\n"))
	  result toString
	}
	
  // implementation of Compare over CountKeys.
  // assumes that aKeys and bKeys are sorted lists.
  // an empty list is less than a non-empty list.
  // an empty list is equal to another empty list.
  // if neither list is empty, compare the head elements.
  // if the head elements are the same, compare the values from each Map.
  // if they're different, the result is the value of the comparison.
  // if they're the same, recurse.
  @scala.annotation.tailrec
  final def compareCountKeys(a:CountKey, b:CountKey, aKeys:List[Variable], bKeys:List[Variable]) : Long = {
    (aKeys, bKeys) match {
        case (Nil, Nil) => 0
        case (x::xs, Nil) => 1
        case (Nil, y::ys) => -1
        // neither list is empty - compare the heads of each list
        case (x::xs, y::ys) => {
          // println("Comparing %1$s:%2$d with %3$s:%4$d".format(x name, a(x), y name, b(y)))
          (x compare y) match {
            case 0 => {
              // the two key values are the same, check the corresponding values from the map
              (a(x) compare b(y)) match {
                // both the keys and the values are the same, so recurse
                case 0 => compareCountKeys(a, b, xs, ys)
                // the map values aren't the same, so return that comparison.
                case i => i
              }
            }
            // the key values aren't the same, go with the comparison of the keys.
            case i => i
          }
        }
      }
  }
  
  // return true if a is less than b. This is determined by looking at each key of a and b in sequence,
  // and where the keys are the same, looking at the values for that key.
  def countKeyLessThan(a:CountKey, b:CountKey): Boolean = {
    (compareCountKeys(a, b, a.keys.toList.sorted, b.keys.toList.sorted) < 0)
  }
  
  // take a key, which is a Map[Variable, Int], and make it presentable.
	def prettyprintKey(a:CountKey) : String = {
	  if (descriptions.contains(a))
	  {
	    descriptions(a)
	  }
	  else
	  {
  	  val result = new StringBuilder("[")
  	  val sortedKey = new TreeMap[Variable, Int] ++ a
	  
  	  val printableKey = sortedKey.map((mapElement) => mapElement._1.mnemonic + ":" + mapElement._2.toString)
	  
  	  printableKey.reduceLeft((a, b) => a + ", " + b)
	  }
	}
	
	override def toString(): String = {
		val result = new StringBuilder("------------\n" + name + "\n------------\n")
		val keys = table.keys.toList.sortWith(countKeyLessThan)
		
		for (thisKey <- keys) {
			result.append("[%1$s] : %2$8d\n".format(prettyprintKey(thisKey), table(thisKey)))
		}
		result.append("\n------------\n")
		result.toString
	}

}
