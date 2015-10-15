name := """elevator-scheduler"""

version := "1.0"

scalaVersion := "2.11.7"

mainClass in Compile := Some("io.datastrophic.elevator.ElevatorControlSystemApp")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"


