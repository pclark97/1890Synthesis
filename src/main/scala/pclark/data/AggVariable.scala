package pclark.data

import scala.collection.immutable.StringOps
import pclark.universe._

/*
  Simple definition of a variable for an aggregate table - in addition to the fields from Variable, 
  it also supports setting a universe expression and a tablename. The universe expression defaults to "All"
  if not otherwise set, and the value conversion function is String.toLong. If the input is known to not
  be an int or a long (for instance, a string or a float), then a different conversion function should be provided
  that still results in an int. For instance:
  val countyICP = new AggVariable("county", "ICPSR county code", 4, 16, "Geographic", (x:String) => x.toDouble.round)
  
  The Universe statement defaults to "None", indicating that it shouldn't be tabulated.
  A different universe statement needs to be provided for tabulation to occur.
  As the codebooks for both NHGIS and ICPSR number columns from 1, but scala and java number strings from index 0,
  this class assumes that it's getting a codebook-derived column start location, and subtracts 1 so that things work as
  expected.
  
  Should it have a conversion function to ints, to deal with the ICPSR state/county FIPS codes that are floats?
	*/

class AggVariable(mnemonic: String, desc: String, start: Int, width: Int, val tablename:String,
    val universeStatement: String, val converter:(String) => Long)
  extends Variable(mnemonic, desc, start-1, width) {
  
  // some potentially useful aux constructors
  def this(n:String, d:String, s:Int, w:Int, t:String, c:(String) => Long) = this(n, d, s, w, t, "None", c)
  def this(n:String, d:String, s:Int, w:Int, t:String, e:String) = this(n, d, s, w, t, e, (x:String) => x.toLong)
  def this(n:String, d:String, s:Int, w:Int, t:String) = this(n, d, s, w, t, "None", (x:String) => x.toLong)
  def this(n:String, d:String, s:Int, w:Int) = this(n, d, s, w, "unset", "None", (x:String) => x.toLong)
  
  // convert the variable's string value to a Long, using the specified converter function. Defaults to 'toLong'
  override def asNumber(data:String):Long = {
    val substr = this(data)
    if (substr.length > 0) {
      converter(substr)
    } else {
      0
    }
  }
  
  // return a default Map element for this AggVariable
  /* this overrides defaultMap on Variable, to ensure that we're returning
     a tuple of type (String, AggVariable)
     */
	override def defaultMapElement = (mnemonic -> this)
	
	val universe:Expression = AggVariable.parseUniverse(this)
	
	def inUniverse(vars:Map[String, Variable])(line:String) = {
	  try {
	    UniverseChecker.evaluate(vars, universe, line)
    }
    catch {
      case nse: NoSuchElementException => throw new Exception("Likely mismatch in universe statement for " + mnemonic, nse)
    }
	}
	
	def isTabulated = (Universe.None != universeStatement && Universe.Nontabulated != universeStatement)
	
	override def toString(): String = mnemonic
  
}

/* Make a companion object to hold the Universe parser, so that we only instantiate one. */
object AggVariable {
  val parser = new Universe()
  
	def parseUniverse(v:AggVariable):Expression = {
    parser.parseAll(parser.expr, v.universeStatement) match {
        case parser.Success(r,_) => r
        case parser.Failure(err, in) => throw new IllegalArgumentException("AggVariable:" + v.mnemonic + " -- Bad universe statement: " + v.universeStatement + "-- error:" + err)
        case _ => throw new IllegalArgumentException("Unspecified error parsing universe statement for " + v.mnemonic)
    }
	}
}