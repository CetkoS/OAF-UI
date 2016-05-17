import sbt.Keys._

name := """OAF-DAL"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq("com.typesafe.slick" %% "slick" % "2.1.0",
  "mysql" % "mysql-connector-java" % "5.1.27",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
          "org.slf4j" % "slf4j-nop" % "1.6.4")