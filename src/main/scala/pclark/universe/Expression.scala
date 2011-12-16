package pclark.universe 

/* we get toString for free with case classes */

abstract class Expression
case class Condition(val variableName:String, val operator:String, val value:Long) extends Expression
case class InCondition(val variableName:String, val values:List[Long]) extends Expression
case class Conjunction(val left:Expression, val right:Expression) extends Expression
case class Disjunction(val left:Expression, val right:Expression) extends Expression
case class AllPersons() extends Expression
case class NoPersons() extends Expression
case class Nontabulated() extends Expression


