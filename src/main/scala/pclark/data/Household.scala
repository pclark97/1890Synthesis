package pclark.data

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import scala.io.Source

/* Hold an entire household's worth of person microdata records. */
class Household(val id:Int, val hrec: String) {
	val persons = new ArrayBuffer[String]
	
	var isActive = true
	
	var deltas: Map[AggVariable, Long] = Map()
	
	def addPerson(thisPerson: String) {
		persons += thisPerson
	}
	
	// a household is empty if the hrec is empty
	def isEmpty = hrec.isEmpty
	
	def numPersons = persons.length
	def allPersons = persons.toArray
	
	/* remember that scala uses C-style array numbering, personAt(0) is the first person. */
	def personAt(i: Int) : String = persons(i)

  // compute deltas for the household as a whole for the given microdata variables, and remember the results.
	def computeDeltas(microdataVars:Map[String, Variable], aggVars:Iterable[AggVariable]) = {
	  var result = scala.collection.mutable.Map[AggVariable, Long]()
	  // for each tabulable AggVar, check each person in the household and increment the aggvar's value in the map if the person is in universe.
    for (aggVar <- aggVars) {
	    for (p <- allPersons) {
	      if (aggVar.inUniverse(microdataVars)(p)) {
	        val oldVal = result getOrElse(aggVar, 0L)
      		result(aggVar) = (oldVal + 1L)
	      }
	    }
  	}
  	
  	// convert the result to an immutable map.
  	deltas = Map[AggVariable, Long]() ++ result
	}
	
	def markInactive {
	  isActive = false
	}
	
	/*
	  Answer a single String that concatenates the household record and all the people in it, in order.
	  */
	override def toString() : String = {
	  val base = hrec+"\n"
	  persons.foldLeft(base)(_ + _ + "\n")
	}
}

object Household {
	// build a list of Household objects from the input stream - and consume the whole input stream.
	// probably want to be living in a 64-bit JVM for big census files.
	def readHouseholds(datafile: Source) : Array[Household] = {
		var thisHH = new Household(-1, "")
		var household_id = 0;
		val households = new ListBuffer[Household]()
		
		for (line <- datafile.getLines) {
			line.charAt(0) match {
				case '#' => null
				case 'H' =>
					if (!thisHH.isEmpty) {households += thisHH}
					thisHH = new Household(household_id, line)
					household_id = household_id+1
				case 'P' => thisHH.addPerson(line)
			}
		}
		// make sure to get the last one
		if (!thisHH.isEmpty) {households += thisHH}
		
		households toArray
	}
	
	def totalPersons(hhs:Array[Household]) = {
	  hhs.foldLeft(0) {(accumulator, thisHH) => accumulator + thisHH.numPersons}
	}
}