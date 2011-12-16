import scala.io.Source
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import pclark.data._
import pclark.data.table._

class BasicCountSpec extends FlatSpec with ShouldMatchers {
  
	val age = new Variable("AGE", 3, 3)
	val income = new Variable("INCOME", 7, 3)
	val sex = new Variable("Sex", 6, 1)
	val relate = new Variable("Relate", 7, 2)
	
	val item = 40
  
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

	"A BasicCount" should "support setting an explicit value" in {
		val table = new BasicCount("Age")
		val key = Map(age -> item)
		table.setValueForKey(key, 10)
		table.countFor(key) should be === 10
	}
  
  it should "have one entry for a single value" in {
		val table = new BasicCount("Age")
		val key = Map(age -> item)
		table.incrementCountForKey(key)
		table.countFor(key) should be === 1
	}
	
	it should "dedupe identical keys" in {
		val anotherItem = 40
		
		val table = new BasicCount("Age")
		
		val key = Map(age -> item)
		table.incrementCountForKey(key)
		
		val duplicateKey = Map(age -> anotherItem)
		table.incrementCountForKey(duplicateKey)
		
		table.countFor(key) should be === 2
		table.size should be === 1
	}

	it should "count separate items separately" in {
		val anotherItem = 42
		
		val table = new BasicCount("Age")
		
		val key = Map(age -> item)
		table.incrementCountForKey(key)
		
		val duplicateKey = Map(age -> anotherItem)
		table.incrementCountForKey(duplicateKey)
		
		table.countFor(key) should be === 1
		table.countFor(duplicateKey) should be === 1
		table.size should be === 2		
	}
	
	it should "support setting an explicit value for a compound key" in {
		val table = new BasicCount("Age")
		val key = Map(age -> item, income -> 4)
		table.setValueForKey(key, 10)
		table.countFor(key) should be === 10
	}
	
	it should "not look like ass" in {
		val table = new BasicCount("Age")

		table.setValueForKey(Map(age -> item, income -> 4), 40)
		table.setValueForKey(Map(age -> item, income -> 1), 10)
		table.setValueForKey(Map(age -> (item-1)), 20)

		// output should list counts of 20, then 10, then 40
		println(table.toString)
	}
	
	it should "be comparable to another table" in {
  	  val testData = List(3 -> 1, 14 -> 2)
  	  val expectedAge = new BasicCount("Expected Age")
    	val tabulatedAge = new BasicCount("AGE")

  	  // load up the expected table from sample data
  	  for (datum <- testData) {
  	    expectedAge.setValueForKey(Map(age -> datum._1), datum._2)
  	    tabulatedAge.setValueForKey(Map(age -> datum._1), 1+datum._2)
  	  }
	  
	  tabulatedAge.compare(expectedAge) should be === 2.0
	}
	
	it should "be constructable from code and match microdata" in {
	  val expectedTableInitialier = List(3 -> 1, 14 -> 1, 24 -> 1, 29 -> 3, 35 -> 1, 37 -> 1, 53 -> 1, 55 -> 1, 77 -> 1)
	  val expectedAge = new BasicCount("Expected Age")
	  
  	val tabulatedAge = new BasicCount("AGE")
  	
	  // load up the expected table from sample data
	  for (datum <- expectedTableInitialier) {
	    expectedAge.setValueForKey(Map(age -> datum._1), datum._2)
	  }
	  
	  // tabulate the fixed-column-width microdata in inputData
		for (line <- Source.fromString(inputData).getLines) {
			line.charAt(0) match {
				case 'H' => null
				case 'P' => tabulatedAge.incrementCountForKey(Map(age -> age(line).toInt))
				case _ => null
			}
		}

		// compare the two tables - they should be equal.
	  tabulatedAge.compare(expectedAge) should be === 0.0
	}
	
	it should "support tabulation using a tabulation function" in {
	  val expectedTableInitialier = List(
	   (Map(relate->1, sex->0), 4),
	   (Map(relate->3, sex->0), 1),
	   (Map(relate->9, sex->0), 1),
	   (Map(relate->2, sex->1), 3),
	   (Map(relate->3, sex->1), 1),
	   (Map(relate->4, sex->1), 1))
	    
	  val tabulatedRelBySex = new BasicCount("Relate by Sex")
	  val expectedRelBySex = new BasicCount("Expected Relate by Sex")
	  // load up the expected table from sample data
	  for (datum <- expectedTableInitialier) {
	    expectedRelBySex.setValueForKey(datum._1, datum._2)
	  }
	  
	  def tabulator(data:String) = {
	    Map(relate -> relate(data).toInt, sex -> sex(data).toInt)
	  }
	  
	  // tabulate the fixed-column-width microdata in inputData
		for (line <- Source.fromString(inputData).getLines) {
			line.charAt(0) match {
				case 'H' => null
				case 'P' => tabulatedRelBySex.incrementCountForKey(tabulator(line))
				case _ => null
			}
		}
	  tabulatedRelBySex.compare(expectedRelBySex) should be === 0.0
	}

	it should "support tabulation using a non-rectangular tabulation function" in {
	  // this is the same input data as the previous test, but now the male and female records for relate code 3 are being consolidated.
	  val expectedTableInitialier = List(
	   (Map(relate->1, sex->0), 4),
	   (Map(relate->3), 2),
	   (Map(relate->9, sex->0), 1),
	   (Map(relate->2, sex->1), 3),
	   (Map(relate->4, sex->1), 1))
	    
	  val tabulatedRelBySex = new BasicCount("Relate by Sex")
	  val expectedRelBySex = new BasicCount("Expected Relate by Sex")
	  // load up the expected table from sample data
	  for (datum <- expectedTableInitialier) {
	    expectedRelBySex.setValueForKey(datum._1, datum._2)
	  }
	  
	  // a tabulator should return a map of variables to variable values that identify a cell in the crosstab
	  def tabulator(data:String) = {
	    val relateCode = relate(data).toInt
	    
	    relateCode match {
	      case 3 => Map(relate -> relateCode)
	      case _ => Map(relate -> relateCode, sex -> sex(data).toInt)
	    }
	  }
	  
	  // tabulate the fixed-column-width microdata in inputData
		for (line <- Source.fromString(inputData).getLines) {
			line.charAt(0) match {
				case 'H' => null
				case 'P' => tabulatedRelBySex.incrementCountForKey(tabulator(line))
				case _ => null
			}
		}
	  tabulatedRelBySex.compare(expectedRelBySex) should be === 0.0
	}
	
}
