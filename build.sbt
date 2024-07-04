name := "akka-scala-seed"

ThisBuild / version := "1.0"

ThisBuild / scalaVersion := s"3.3.3"

ThisBuild / resolvers += "Akka library repository".at("https://repo.akka.io/maven")



// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true


lazy val root = Project("my-project", file("."))
  .aggregate(
    akkaStreamsStuff,
    scalaStuff
  )

lazy val akkaStreamsStuff = Project("akka-streams-stuff", file("akka-streams-stuff"))

lazy val scalaStuff = Project("scala-stuff", file("scala-stuff"))
