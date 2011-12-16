package pclark.data

import pclark.universe._

/*
   Given a data line and an expression, see if the data line meets the criteria specified in the expression.
   Expressions are given using the classes in the pclark.universe package.
   
   TODO: generalize this to households.
   
   */

object UniverseChecker {
  
  def conditionEvaluate(vars:Map[String, Variable], c:Condition, line:String) : Boolean = {
    val varToEvaluate = vars(c.variableName)
    val valueForVar = varToEvaluate.asNumber(line)
    val valueForCondition = c.value
    
    c match {
      case Condition(_, "=", _) => (valueForVar == valueForCondition)
      case Condition(_, ">", _) => (valueForVar > valueForCondition)
      case Condition(_, "<", _) => (valueForVar < valueForCondition)
      case Condition(_, ">=", _) => (valueForVar >= valueForCondition)
      case Condition(_, "<=", _) => (valueForVar <= valueForCondition)
      case Condition(_, "!=", _) => (valueForVar != valueForCondition)
      case _ => false
    }
  }
  
  def inConditionEvaluate(vars:Map[String, Variable], i:InCondition, line:String) : Boolean = {
    val varToEvaluate = vars(i.variableName)
    val valueForVar = varToEvaluate.asNumber(line)
    val valuesForCondition = i.values
    (valuesForCondition contains valueForVar)
   }
  
   def conjunctionEvaluate(vars:Map[String, Variable], c:Conjunction, line:String) : Boolean = {
     val leftSide = evaluate(vars, c.left, line)
     
     // do short-circuit evalation. If the left-hand-side of the conjunction is false, then the expression is false.
     // if it's not, then the value of the expression is the value of the right-hand-side.
     if (leftSide) {
       evaluate(vars, c.right, line)
     } else {
       false
     }
    }

    def disjunctionEvaluate(vars:Map[String, Variable], d:Disjunction, line:String) : Boolean = {
      val leftSide = evaluate(vars, d.left, line)

      // do short-circuit evalation. If the left-hand-side of the conjunction is true, then the expression is true.
      // if it's not, then the value of the expression is the value of the right-hand-side.
      if (!leftSide) {
        evaluate(vars, d.right, line)
      } else {
        true
      }
     }
  
  
  def evaluate(vars:Map[String, Variable], e:Expression, line:String) : Boolean = {
    e match {
      case c:Condition => conditionEvaluate(vars, c, line)
      case i:InCondition => inConditionEvaluate(vars, i, line)
      case c:Conjunction => conjunctionEvaluate(vars, c, line)
      case d:Disjunction => disjunctionEvaluate(vars, d, line)
      case a:AllPersons => true
      case n:NoPersons => false
      case _ => false
      
    }
  }
}
