import scala.io.Source
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.io.Source
import pclark.universe._
import pclark.data._
import pclark.data.table._
import pclark.data.cph1900.ICPSR_02896

class ICPSR_02896_for1900Spec extends FlatSpec with ShouldMatchers with GZipSource {
  
  "ICPSR 02896 1900 VarSetup" should "have the right number of agg vars" in {
      ICPSR_02896.numericAggVars.size should be === 190
  }
  
  it should "be able to read agg data vars for states" in {
    ICPSR_02896.readData.size should be === 53
  }

  it should "get the right population size for Minnesota" in {
    val totalPopVar = ICPSR_02896.vars("totpop")
    val data = ICPSR_02896.stateData
    data(ICPSR_02896.minnesotaICP).name should be === "MINNESOTA"
    data(ICPSR_02896.minnesotaICP).apply(totalPopVar) should be === 1751394
  }

  it should "be able to get a table by name" in {
    val raceByNativityForAlabama = ICPSR_02896.getTableForState(ICPSR_02896.nt16, ICPSR_02896.alabamaICP)
    raceByNativityForAlabama.size should be === 5
    raceByNativityForAlabama(ICPSR_02896.vars("nwnpil10").defaultKey) should be === 102779
    // println(raceByNativityForAlabama)
  }
  
  it should "have the right universe statement for pbgerman" in {
    val herr = "P1900030000000700050020000303010101699089900002200453001000010010000100100100000000200010220203403041000000"
    val frau = "P1900030000000700050020000303010102699089900002200453001000010010000100100100000000200010220203403041000000"
    val senorita = "P1900030000000700050020000303010101699089900002200438001000010010000100100100000000200010220203403041000000"
    
    val us1900Microdata = new MicrodataVars("1900", Source.fromFile( ICPSR_02896.targetData + "1900/Microdata/usa_00054.cbk"))
    val pbgerman = ICPSR_02896.vars("pbgerman")
    
    // get a curried universe check function
    val universeCheck = pbgerman.inUniverse(us1900Microdata.pVars)_
    
    universeCheck(herr) should be === true
    universeCheck(frau) should be === true
    universeCheck(senorita) should be === false
  }
}
