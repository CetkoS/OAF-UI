import sbt.Keys._

name := """OAF"""

version := "1.0-SNAPSHOT"

lazy val dal = ProjectRef(file("../OAF-DAL"), "oaf-dal")

lazy val ui = (project in file(".")).enablePlugins(PlayScala).aggregate(dal).dependsOn(dal)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  anorm,
  jdbc,
  cache,
  ws
)

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.27",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "jp.t2v" %% "play2-auth"      % "0.13.5",
  "jp.t2v" %% "play2-auth-test" % "0.13.5" % "test",
  "org.mindrot" % "jbcrypt" % "0.3m"
)


resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

