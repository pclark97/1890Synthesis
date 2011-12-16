package pclark.data

import pclark._
import pclark.data._
import pclark.data.table._

/* This class defines an abstract superclass for classes that read in aggregate data files, typically from ICPSR */

abstract class AggDataFile {
  
  // this really ought to be a config parameter of some sort...
  val targetData = "target/scala-2.9.1.final/classes/"
  
  val alabamaICP = 41
	val minnesotaICP = 33
	val connecticutICP = 1
	
	val nontab = "Nontabulated"
	
	val totalPopulation:AggVariable
  
  val vars:Map[String, AggVariable]
  val stateData : Map[Long, AggDataRecord]
  
  def readData : Map[Long, AggDataRecord]
  def processDataline(data:String) : AggDataRecord
  def getTableForState(tableName:String, stateID:Long):BasicCount
}