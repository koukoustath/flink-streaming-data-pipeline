ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "2.13.13"

lazy val root = (project in file("."))
  .settings(
    name := "flink-streaming-data-pipeline"
  )
