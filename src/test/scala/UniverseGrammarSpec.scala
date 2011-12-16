import scala.io.Source
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import pclark.universe._

class UniverseGrammarSpec extends FlatSpec with ShouldMatchers {
  
  "My UniverseGrammar" should "parse simple universe statements" in {
    val sexStatement = "sex = 1"
    val ageStatement = "age > 10"
    val allStmt = "All"
    val noneStmt = "None"
    val nonTabStmt = "Nontabulated"
    
    val parser = new Universe()
    
    parser.parseAll(parser.expr, sexStatement) match {
        case parser.Success(r,_) => r.toString should be === "Condition(sex,=,1)"
        case x => fail(x.toString)
    }

    parser.parseAll(parser.expr, ageStatement) match {
        case parser.Success(r,_) => r.toString should be === "Condition(age,>,10)"
        case x => fail(x.toString)
    }

    parser.parseAll(parser.expr, allStmt) match {
        case parser.Success(r,_) => r.toString should be === "AllPersons()"
        case x => fail(x.toString)
    }
    
    parser.parseAll(parser.expr, noneStmt) match {
            case parser.Success(r,_) => r.toString should be === "NoPersons()"
            case x => fail(x.toString)
    }

    parser.parseAll(parser.expr, nonTabStmt) match {
            case parser.Success(r,_) => r.toString should be === "Nontabulated()"
            case x => fail(x.toString)
    }

  }
  
  it should "parse 'in' statements" in {
    val inStmt = "relate in [1, 2, 3, 4]"
    
    val parser = new Universe()
    
    val inAST = parser.parseAll(parser.expr, inStmt)
        
    inAST match {
        case parser.Success(r,_) => r.toString should be === "InCondition(relate,List(1, 2, 3, 4))"
        case x => fail(x.toString)
    }
    
  }

  it should "fail to parse bad operators" in {
     val inStmt = "relate => 4"

     val parser = new Universe()

     val inAST = parser.parseAll(parser.expr, inStmt)

     inAST match {
         case parser.Success(r,_) => fail("Unexpected Success with" + r.toString)
         case x => true
     }

   }
  
  it should "parse expressions separated by 'and' or 'or'" in {
    val conjunctiveStmt = "age = 1 and sex > 10 and somethingElse in [1, 2, 3]"
    
    val parser = new Universe()
    
    val ast = parser.parseAll(parser.expr, conjunctiveStmt)
    
    ast match {
      case parser.Success(r,_) => r.toString should be === "Conjunction(Conjunction(Condition(age,=,1),Condition(sex,>,10)),InCondition(somethingElse,List(1, 2, 3)))"
      case x => fail(x.toString)
    }
  }

"AGE >= 5 and AGE <= 20"
  it should "parse expressions nested with parens" in {
    val conjunctiveStmt = "age = 1 and (sex > 10)"
    
    val parser = new Universe()
    
    val ast = parser.parseAll(parser.expr, conjunctiveStmt)
    
    ast match {
      case parser.Success(r,_) => r.toString should be === "Conjunction(Condition(age,=,1),Condition(sex,>,10))"
      case x => fail(x.toString)
    }
  }
  
  it should "parse an expression that's making ICPSR_02896 cranky" in {
    val conjunctiveStmt = "AGE >= 5 and AGE <= 20"
    
    val parser = new Universe()
    
    val ast = parser.parseAll(parser.expr, conjunctiveStmt)
    
    ast match {
      case parser.Success(r,_) => r.toString should be === "Conjunction(Condition(AGE,>=,5),Condition(AGE,<=,20))"
      case x => fail(x.toString)
    }
  }
  

  it should "parse expressions with parens and disjunctions" in {
    val bigStmt = "age = 1 or (sex >= 10 and relate in [10, 20])"
    
    val parser = new Universe()
    
    val ast = parser.parseAll(parser.expr, bigStmt)
    
    ast match {
      case parser.Success(r,_) => r.toString should be === "Disjunction(Condition(age,=,1),Conjunction(Condition(sex,>=,10),InCondition(relate,List(10, 20))))"
      case x => fail(x.toString)
    }
  }

  
}