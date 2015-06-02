name := "redson"

version := "0.1.0"

scalaVersion := "2.10.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test" withSources() withJavadoc()

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.3" % "test"  withSources() withJavadoc()

libraryDependencies += "org.specs2" %% "specs2-core" % "3.6" % "test" withSources() withJavadoc()

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

scalacOptions in Test ++= Seq("-Yrangepos")