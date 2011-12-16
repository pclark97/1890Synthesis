package pclark.util

import pclark._
import pclark.data._
import pclark.data.table._
import scala.io.Source

/* This object makes a frequency count of Age values in the input data. It then prints out the counts. */

object BasicCounter {

	val age = new Variable("AGE", 3, 3)
	val ageFreq = new BasicCount("AGE")
	
	def countLines(datafile: Source) = {
		for (line <- datafile.getLines) {
			line.charAt(0) match {
				case '#' => null
				case 'H' => null
				case 'P' => ageFreq.incrementCountForKey(Map(age -> age(line).toInt))
			}
		}
	}
	
	def main(args: Array[String]) {
		println("Current directory is " + System.getProperty("user.dir"))
		println("Frequency counts for all variables in the file:")
		// this assumes that the Counter class is being executed by sbt run
		countLines(Source.fromFile("target/scala-2.9.0.final/classes/input.dat"))
		println("Age Frequencies:")
		println(ageFreq)
	}
}
