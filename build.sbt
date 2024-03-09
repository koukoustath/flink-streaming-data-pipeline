import scala.language.postfixOps

ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "2.12.17"

// TODO: Check if this is the correct way to use the "addCompilerPlugin"
addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.1" cross CrossVersion.full)

lazy val root = (project in file("."))
  .settings(
    name := "flink-streaming-data-pipeline"
  )

val versions = new {
  // 1.18.0 many deprecated things (SourceFunction, Scala API in general)
  val flink: String = "1.17.0"
  val circe: String = "0.14.3"
  val scalaTest: String = "3.2.17"
}

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala"           % versions.flink,
  "org.apache.flink" %% "flink-streaming-scala" % versions.flink,
  // "org.apache.flink"  % "flink-connector-files" % versions.flinkConnectorFiles,
  "org.apache.flink" % "flink-clients"        % versions.flink,
  "io.circe"        %% "circe-core"           % versions.circe,
  "io.circe"        %% "circe-generic"        % versions.circe,
  "io.circe"        %% "circe-parser"         % versions.circe,
  "io.circe"        %% "circe-generic-extras" % versions.circe,
  "org.scalatest"   %% "scalatest"            % versions.scalaTest % Test
)
