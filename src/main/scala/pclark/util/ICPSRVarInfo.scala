package pclark.util

import pclark._
import pclark.data._
import pclark.data.cph1890._
import scala.io.Source
import java.io._

object ICPSRVarInfo {
  val ICPSR1890 = pclark.data.cph1890.ICPSR_02896

  val (nonTabulatedVars, tabulatedVars) = ICPSR1890.numericAggVars partition (aggv => (aggv.universeStatement == ICPSR1890.nontab || aggv.universeStatement == "None"))

  val ipumsVars = new pclark.data.MicrodataVars("ipums",
    Source.fromFile("/Users/pclark/Projects/PlanB2/src/main/resources/1880-1900/Microdata/usa_00057.cbk"))
}