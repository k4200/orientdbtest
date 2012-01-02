name := "orientdbtest"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "Orient Technologies Maven2 Repository" at "http://www.orientechnologies.com/listing/m2"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.9.0" % "1.6.1" % "test",
  "com.orientechnologies" % "orient-commons" % "1.0rc7",
  "com.orientechnologies" % "orientdb-core" % "1.0rc7")
