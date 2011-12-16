package pclark.util

import pclark._
import pclark.data._
import pclark.data.table._
import scala.io.Source

/* This object makes a frequency count of Age values in the input data. It then prints out the counts. */

object FreqCounter extends GZipSource {
  
  val us1900Microdata = new MicrodataVars("1900", Source.fromFile("target/scala-2.9.0.final/classes/1900/Microdata/usa_00054.cbk"))
  val microdataSource = "target/scala-2.9.0.final/classes/1900/Microdata/Alabama.dat.gz"
  
	def countLines(varToCount:Variable, datafile: Source) = {
	  val freqs = new BasicCount(varToCount.mnemonic)
	  
		for (line <- datafile.getLines) {
			line.charAt(0) match {
				case '#' => null
				case 'H' => null
				case 'P' => freqs.incrementCountForKey(Map(varToCount -> varToCount(line).toInt))
			}
		}
		freqs
	}
	
	def doVariable(v:Variable) {
		// this assumes that the Counter class is being executed by sbt run
		val gzipSource = getSourceForGzipFile(microdataSource)
	  val freqs = countLines(v, gzipSource)
		println("Frequencies for " + v.mnemonic + ":")
		println(freqs)
	}
	
	def main(args: Array[String]) {
	  val varName = args(0)
		println("Current directory is " + System.getProperty("user.dir"))
		println("Counting frequencies for " + varName)
		
		val varToCount = us1900Microdata.pVars.get(varName)
		
		varToCount match {
		  case Some(v) => doVariable(v)
		  case None => println("Variable " + varName + " not found.")
		}
	}
}
