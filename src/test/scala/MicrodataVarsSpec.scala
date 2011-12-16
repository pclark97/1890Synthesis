import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import pclark.data._
import pclark.data.table._

class MicrodataVarsSpec extends FlatSpec with ShouldMatchers {
    val line1 = " STATEFIP           H  28-29           2      . "
    val line2 = " STATEFIP           H  28-29           2      .     X "
    
  "an MicrodataVars instance" should "be instantiable from a one-sample extract" in {
      val mv1 = new MicrodataVars("one-sample", Source.fromString(line1))
      mv1.hVars.size should be === 1
  }
  
  it should "be instantiable from a two-sample extract" in {
      val mv2 = new MicrodataVars("one-sample", Source.fromString(line2))
      mv2.hVars.size should be === 1
  }
}