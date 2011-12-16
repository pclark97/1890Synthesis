package pclark.data

import scala.io.Source
import java.io.FileInputStream
import java.util.zip._

/* This trait defines a method for getting a Source to read a gzip file. */

trait GZipSource {
  def getSourceForGzipFile(gzipdatapath: String) : Source = {
		val filereader = new java.io.FileInputStream(gzipdatapath)
		val gzipreader = new java.util.zip.GZIPInputStream(filereader)
		Source.fromInputStream(gzipreader)
	}
  
}