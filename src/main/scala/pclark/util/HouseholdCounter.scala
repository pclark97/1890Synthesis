package pclark.util

import pclark._
import pclark.data._
import pclark.data.table._
import scala.io.Source

/* This object makes a frequency count of Age values in the input data. It then prints out the counts. */

object HouseholdCounter extends GZipSource {

	val ser = new Variable("SERIAL", 8, 8)
	val serialFreq = new BasicCount("ser")
	
	def countLines(datafile: Source) = {
		for (line <- datafile.getLines) {
			line.charAt(0) match {
				case '#' => null
				case 'H' => serialFreq.incrementCountForKey(Map(ser -> ser(line).toInt))
				case 'P' => null
			}
		}
	}
	
	def main(args: Array[String]) {
		println("Current directory is " + System.getProperty("user.dir"))
		if (args.length == 1) {
  		println("Frequency counts for all variables in the file:" + args(0))
  		// this assumes that the Counter class is being executed by sbt run
  		val src = if (-1 == args(0).indexOf(".gz")) {
  		  println("Using normal Source")
  		  Source.fromFile(args(0))
  		} else {
  		  println("Using gzip Source")
  		  getSourceForGzipFile(args(0))
  		}
  		countLines(src)
  		println("Serial Frequencies:")
  		println(serialFreq)
  	} else {
      println("Usage: HouseholdCounter <path of microdata file>")
    }
  }
}
