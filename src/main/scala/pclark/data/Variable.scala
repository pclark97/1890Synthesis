package pclark.data

/*	Simple definition of a variable - it has a name/mnemonic, a description, a start location in the line, and a width.
	*/

class Variable(val mnemonic: String, val desc: String, val start: Int, val width: Int) extends Ordered[Variable] {
  
  def this(n:String, s:Int, w:Int) = this(n, "", s, w)
  
  @deprecated("Please use mnemonic instead", "Since may or so")
  def name = mnemonic
  
  def compare(that:Variable) = {
    mnemonic.compare(that.mnemonic)
  }
  
  // given a data line, get the value for this variable.
	def apply(line: String) : String = {
		line.substring(start, start + width).trim
	}
	
	// return the value of this variable for the given line as a Long.
	def asNumber(data:String):Long = {
    val substr = this(data)
    if (substr.length > 0) {
      substr.toLong
    } else {
      0
    }
  }
  
	// return a default CountKey for this variable.
	def defaultKey = Map(this->1)
	
	// return a default Map element for this variable
	def defaultMapElement = (mnemonic -> this)
}

object Variable {
  // defines some useful constants.
  val NIU = new Variable("NIU", "Not In Universe", 0, 0)
  val MISSING = new Variable("Missing", "Missing", 0, 0)
  val OTHER = new Variable("Other", "Other", 0, 0)
}