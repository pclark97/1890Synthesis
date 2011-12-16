name := "PlanB"

version := "0.7"

organization := "pclark"

scalaVersion := "2.9.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1"

parallelExecution in Test := false 

scalacOptions ++= Seq("-optimize", "-deprecation")
// scalacOptions ++= Seq("-deprecation")

mainClass in (Compile, run) := Some("pclark.data.Tabulator")
