import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import pclark.data._
import scala.io.Source

class HouseholdSpec extends FlatSpec with ShouldMatchers {

  val inputData = """# Household vars - 
# 00/1 - RECTYPEH
# 01/4 - YEAR
# 05/1 - urban/rural (0=urban, 1=rural)
# 06/2 - Number of people
# Person vars - 
# 00/1 - RECTYPEP
# 01/2 - PERNUM
# 03/3 - AGE
# 06/1 - Sex (0=male, 1=female)
# 07/2 - Relate - IPUMS USA general codes (01=head,02=spouse,03=child,04=child-in-law,09=grandchild)
# ---------------------
# table of age by sex:
#      M  F
# 077  1  0
# 035  1  0
# 037  0  1
# 014  0  1
# 029  2  1
# 055  1  0
# 053  0  1
# 024  0  1
# 003  1  0
# ---------------------
# table of relate by sex:
#     M  F
# 01  4  0
# 02  0  3
# 03  1  1
# 04  0  1
# 09  1  0
# ---------------------
H1950103
P01035001
P02037102
P03014103
H1950102
P01029001
P02029102
H1950105
P01055001
P02053102
P03029003
P04024104
P05003009
H1950101
P01077001"""

  val someAggVars = List(
    new AggVariable("totpop", "Total population, 1900", 0, 0, "", "All"),
    new AggVariable("totmen", "Total male population, 1900", 0, 0, "", "SEX = 0"),
    new AggVariable("totwomen", "Total female population, 1900", 0, 0, "", "SEX = 1")
  )
  
  val firstHHExpected = List(3, 1, 2)
  
  val microdataVars = Map("AGE" -> new Variable("AGE", 3, 3), "SEX" -> new Variable("SEX", 6, 1))

  "A Household" should "be readable from basic data" in {
    val reader = Source.fromString(inputData)
    val hhs = Household.readHouseholds(reader)
    hhs.size should be === 4
    hhs(0).numPersons should be === 3
    
    hhs(1).id should be === 1
  }
  
  it should "be able to compute total number of person records" in {
    val reader = Source.fromString(inputData)
    val hhs = Household.readHouseholds(reader)
    
    Household.totalPersons(hhs) should be === 11
  }
  
  it should "be able to cache deltas for some aggvars" in {
    val reader = Source.fromString(inputData)
    val hhs = Household.readHouseholds(reader)
    
    val expectedDeltas = someAggVars.zip(firstHHExpected).toMap
    
    // make the caching happen
    hhs foreach ((x:Household) => x.computeDeltas(microdataVars, someAggVars))
    
    for (av <- someAggVars) {
      expectedDeltas(av) should be === hhs(0).deltas(av)
    }    
  }
  
  it should "be markable as inactive" in {
    val reader = Source.fromString(inputData)
    val hhs = Household.readHouseholds(reader)
    
    hhs(1) should be ('active)
    hhs(1).markInactive
    hhs(1).isActive should not be ('active)
  }
  
}
