organization := "com.sidemash"

name := "redson"

version := "0.1.0"

javaOptions in run += "-Xdoclint:none"

fork in run := true

scalaVersion := "2.10.5"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.4" withSources() withJavadoc()

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test" withSources() withJavadoc()

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.12.3" % "test"  withSources() withJavadoc()

libraryDependencies += "org.specs2" %% "specs2-core" % "3.6" % "test" withSources() withJavadoc()

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

scalacOptions in Test ++= Seq("-Yrangepos")

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

// Alternatives to PomExtra
// licenses := Seq("The MIT License (MIT)" -> url("http://opensource.org/licenses/MIT"))
// homepage := Some(url("https://github.com/sidemash/redson"))

pomExtra := (
    <url>https://github.com/sidemash/redson</url>
    <licenses>
      <license>
        <name>The MIT License (MIT)</name>
        <url>http://opensource.org/licenses/MIT</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:sidemash/redson.git</url>
      <connection>scm:git:git@github.com:sidemash/redson.git</connection>
    </scm>
    <developers>
      <developer>
        <id>sergenguetta</id>
        <name>Serge Martial Nguetta</name>
        <url>https://github.com/sergenguetta</url>
      </developer>
      <developer>
        <id>stankoua</id>
        <name>Stéphane Tankoua</name>
        <url>https://github.com/stankoua</url>
      </developer>
    </developers>)


// Do not append Scala versions to the generated artifacts
crossPaths := false