import scala.io.Source
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import pclark.universe._
import pclark.data._

class UniverseCheckerSpec extends FlatSpec with ShouldMatchers {
  
  val varname = "COL1"
  val variables = Map(varname -> new Variable(varname, 0, 1))
  val dataline = "12345"
  val anotherDataline = "92345"
  val aThirdDataline = "72345"
  
  "A UniverseChecker" should "return true for equals when it really is" in {
    val expr = new Condition(varname, "=", 1)
    UniverseChecker.evaluate(variables, expr, dataline) should be (true)
  }
  
  it should "return false for equals when it isn't" in {
    val expr = new Condition(varname, "=", 5)
    UniverseChecker.evaluate(variables, expr, dataline) should be (false)
  }

  it should "always return true for AllPersons" in {
    val expr = new AllPersons()
    UniverseChecker.evaluate(variables, expr, dataline) should be (true)
  }

  
  it should "return correct values for other operations, assuming the above two tests work." in {
    UniverseChecker.evaluate(variables, Condition(varname, ">", 5), dataline) should be (false)
    UniverseChecker.evaluate(variables, Condition(varname, "<=", 5), dataline) should be (true)
    UniverseChecker.evaluate(variables, Condition(varname, "!=", 9), dataline) should be (true)
    UniverseChecker.evaluate(variables, Condition(varname, ">=", 0), dataline) should be (true)
    UniverseChecker.evaluate(variables, Condition(varname, ">=", 2), dataline) should be (false)
  }
  
  it should "return true for inexprs when appropriate" in {
    val expr = new InCondition(varname, List(1, 2, 3))
    UniverseChecker.evaluate(variables, expr, dataline) should be (true)
  }
  
  it should "return false for inexprs when appropriate" in {
    val expr = new InCondition(varname, List(100, 200, 300))
    UniverseChecker.evaluate(variables, expr, dataline) should be (false)
  }
  
  it should "do the right thing for conjunctions and disjunctions" in {
    val expr = new Conjunction(new Condition(varname, "<=", 5), new Condition(varname, "=", 1))
    UniverseChecker.evaluate(variables, expr, dataline) should be (true)
    UniverseChecker.evaluate(variables, expr, anotherDataline) should be (false)

    val dexpr = new Disjunction(new Condition(varname, ">=", 9), new Condition(varname, "=", 1))
    UniverseChecker.evaluate(variables, dexpr, dataline) should be (true)
    UniverseChecker.evaluate(variables, dexpr, anotherDataline) should be (true)
    UniverseChecker.evaluate(variables, dexpr, aThirdDataline) should be (false)
  }
}