import scala.io.Source
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import pclark.data._
import pclark.data.table._

class CountKeySpec extends FlatSpec with ShouldMatchers {
  
	val age = new Variable("AGE", 3, 3)
	val income = new Variable("INCOME", 7, 3)
	val sex = new Variable("SEX", 10, 1)
	
	val item = 40
	
	"A CountKey" should "be comparable when containing one element of the same key" in {
		val table = new BasicCount("Age")
		val thisKey = Map(age -> item)
		val thatKey = Map(age -> (item+1))

    table.countKeyLessThan(thisKey, thatKey) should be (true)
	}
	
	it should "be comparable when containing one element of the same key and value" in {
		val table = new BasicCount("Age")
		val thisKey = Map(age -> item)
		val thatKey = Map(age -> item)

    table.countKeyLessThan(thisKey, thatKey) should be (false)
	}

	it should "be comparable when containing one element of different keys" in {
		val table = new BasicCount("Age")
		val thisKey = Map(age -> item)
		val thatKey = Map(income -> item)

    table.countKeyLessThan(thisKey, thatKey) should be (true)
	}
	
	it should "be comparable when containing two elements of different keys" in {
		val table = new BasicCount("Age")
		val thisKey = Map(age -> item, sex -> 1)
		val thatKey = Map(age -> item, income -> item)
    
    table.countKeyLessThan(thisKey, thatKey) should be (false)
    table.countKeyLessThan(thatKey, thisKey) should be (true)
	}
	
	it should "be comparable when containing one element of the same key and one element of a different key" in {
		val table = new BasicCount("Age")
		val thisKey = Map(age -> item, sex -> 1)
		val thatKey = Map(age -> item, sex -> 2)

    table.countKeyLessThan(thisKey, thatKey) should be (true)
	}
	
}
