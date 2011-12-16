package pclark.util

import scala.util.matching.Regex
import scala.collection.mutable.ListBuffer
import scala.io.Source

import pclark.data.Variable

/* This program reads in a codebook for a hierarchical microdata file and generates a
   scala description of that codebook so that the file contents can be used in code. 
   This currently generates a new class; it would be cooler to build the variables on the fly.
*/

object CodebookParser {
  // a variable record is optional whitespace, then a name, followed by an H or P, followed by either a number or a number-dash-number pattern, followed by a number, followed by an X.
  val VariableDef = """(?:\s*)\b(\w+)(?:\s*)\b(H|P)(?:\s*)(\d+)(?:-\d+)?(?:\s*)([0-9]+)(?:\s*)X\s""".r
  
  val testInput = "  RECTYPE            H   1              1      X "
  val testInput2 = "  RECTYPE2           H   2-10           8      X "
  
  val hhVarList = new ListBuffer[String]
  val pVarList = new ListBuffer[String]

  val newVarPattern = "\t\t\t\tnew Variable(\"%1$s\", %2%s, %3$s)"
  
  val template = """
  package pclark.data.%1$s
  import scala.io.Source
  import scala.collection.mutable.ListBuffer

  import pclark._
  import pclark.data._
  import pclark.data.table._
  
  /* Object to implement Variable definitions for a given extract, based on a codebook. */
  object MicrodataVarSetup {

  	lazy val pVars = buildPersonMicrodataVars
  	lazy val hVars = buildHouseholdMicrodataVars

    // given a Variable, make a map element of the variable name to the variable.
  	def makeMapElement(v: Variable): (String, Variable) = {
  		v.name -> v
  	}

  	def buildHouseholdMicrodataVars: Map[String, Variable] = {
  		/* note to self - the map(makeMapElement) call below results in a List of tuples
  		   that can be used to initialize the Map of strings to variables. However, you can't
  		   initialize a Map with a List, so the _* passes each element of the List
  		   into the Map constructor one at a time */
  		Map(hhMicrodataVarsList map(makeMapElement): _*)
  	}

  	def buildPersonMicrodataVars: Map[String, Variable] = {
  		Map(personMicrodataVarsList map(makeMapElement): _*)
  	}


  	/* TODO - looks like columns are off by one... */
  	def hhMicrodataVarsList: List[Variable] = {
  	  List(
%2$s
  	  )
  	}
  	
  	def personMicrodataVarsList: List[Variable] = {
  		List(
%3$s
  	  )
    }
}
"""
  
  def matchline(line:String) = {
    line match {
      case VariableDef(name, "H", startcol, width) => hhVarList += newVarPattern.format(name, startcol, width)
      case VariableDef(name, "P", startcol, width) => pVarList += newVarPattern.format(name, startcol, width)
      case _ => ""
    }
  }
    
  def main(args: Array[String]) {
    val input = Source.fromFile("target/scala-2.9.0.final/classes/1900/Microdata/usa_00054.cbk")
    for (line <- input.getLines) matchline(line)
    
    val hhVars = hhVarList.reduceLeft(_ + ", \n" + _)
    val pVars = pVarList.reduceLeft(_ + ", \n" + _)
    
    println(template.format(args(0), hhVars, pVars))
  }
}