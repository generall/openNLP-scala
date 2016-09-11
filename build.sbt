name := "sentence-chunker"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

organization := "ml.generall"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "org.scalanlp" % "chalk" % "1.1.2",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models",
  "com.google.protobuf" % "protobuf-java" % "2.6.1",
  "xerces" % "xercesImpl" % "2.9.1"
)


dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "1.0.5"


resolvers ++= Seq(
  Resolver.mavenLocal
)

