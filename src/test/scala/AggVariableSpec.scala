import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import pclark.data._
import pclark.data.table._

class AggVariableSpec extends FlatSpec with ShouldMatchers {
  "an agg variable" should "be instantiable" in {
    val myAggVar = new AggVariable("totpop", "Total population, 1900", 45, 8, "Total Population")
    
    myAggVar.mnemonic should be === "totpop"
    myAggVar.universeStatement should be === "None"
  }
}