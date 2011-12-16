package pclark.universe

import scala.util.parsing.combinator._

/*
  Class to implement a DSL for parsing universe statements. A universe statement should look like
  sex = 1 and age > 10
  or
  (relate in [10, 20, 30]) and age > 30
  
  The first shot at a grammar looks like this:
  expr ::= term {"and" term | "or" term}
  term ::= varname operator value | varname "in" intlist | "(" expr ")"
  operator ::= "<" | "<=" | "=" | ">=" | ">"| "!="
  value ::= wholeNumber
  intlist ::= "[" wholeNumber {"," wholeNumber} "]"
*/


class Universe extends JavaTokenParsers {
  def expr: Parser[Expression] = term~rep("and"~term | "AND"~term | "&"~term | "or"~term | "OR"~term | "|"~term) ^^ {
    case v ~ f => f.foldLeft(v) {
      case (x, "and" ~ term) => new Conjunction(x, term)
      case (x, "AND" ~ term) => new Conjunction(x, term)
      case (x, "&" ~ term) => new Conjunction(x, term)
      case (x, "or" ~ term) => new Disjunction(x, term)
      case (x, "OR" ~ term) => new Disjunction(x, term)
      case (x, "|" ~ term) => new Disjunction(x, term)
    }
  }
  def term: Parser[Expression] = unconditional | cond | incond | "(" ~> expr <~ ")"
  def unconditional: Parser[Expression] = "All" ^^ { case _ => new AllPersons() } | "None" ^^ { case _ => new NoPersons() } | "Nontabulated" ^^ { case _ => new Nontabulated() }
  def incond: Parser[InCondition] = varname ~ "in" ~ intlist ^^ { case name~op~values => new InCondition(name, values) }
  def cond: Parser[Condition] = varname~operator~value ^^ { case name~op~num => new Condition(name, op, num) } 
  def varname: Parser[String] = ident ^^ { case ident => ident }
  def operator: Parser[String] = "<=" | "<" | "=" | ">=" | ">" | "!="
  def intlist: Parser[List[Long]] = "[" ~> repsep(value, ",") <~ "]" ^^ (List() ++ _)
  def value: Parser[Long] = wholeNumber ^^ { _.toLong }
}

object Universe {
  val Nontabulated = "Nontabulated"
  val None = "None"
  val All = "All"
}