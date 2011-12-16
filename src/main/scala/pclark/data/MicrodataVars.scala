package pclark.data

import scala.util.matching.Regex
import scala.collection.mutable.ListBuffer
import scala.io.Source

/*
  Read in an IPUMS USA codebook file, and create two collections of Variables - one for hhrecs, and one for person recs,
  with names and column locations properly filled in from the codebook.
*/
  
class MicrodataVars(name:String, input:Source) {
  // a variable record is optional whitespace, then a name, followed by an H or P, followed by either a number or a number-dash-number pattern, followed by a number, followed by a period or an X, optionally followed by some whitespace and another period or X.
  val VariableDef = """(?:\s*)\b(\w+)(?:\s*)\b(H|P)(?:\s*)(\d+)(?:-\d+)?(?:\s*)([0-9]+)(?:\s*)(?:\.|X)?(?:\s*)?(?:\.|X)\s""".r
  
  val (hVars, pVars) = loadCodebook(input)
    
  /* read in an IPUMS microdata codebook and generate Variable instances for the vars defined in the codebook */
  def loadCodebook(lines:Source) = {
    val hhVarList = new ListBuffer[Variable]
    val pVarList = new ListBuffer[Variable]
    
    def matchline(line:String) = {
      line match {
        case VariableDef(mnemonic, "H", startcol, width) => hhVarList += new Variable(mnemonic, startcol.toInt-1, width.toInt)
        case VariableDef(mnemonic, "P", startcol, width) => pVarList += new Variable(mnemonic, startcol.toInt-1, width.toInt)
        case _ => ""
      }
    }
    
    for (line <- lines.getLines) matchline(line)
    
    // return a tuple containing two maps - one of hhvar names to hhvars, and one of pvar names to pvars.
    (Map(hhVarList.map(_.defaultMapElement): _*), Map(pVarList.map(_.defaultMapElement): _*))
  }
    
}