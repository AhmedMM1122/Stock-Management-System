ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "project_pl3",libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.8.0",
      libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R25"

)
