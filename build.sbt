import scala.language.postfixOps

ThisBuild / version := "0.0.1"

ThisBuild / scalaVersion := "2.12.17"

lazy val root = (project in file("."))
  .settings(
    name := "flink-streaming-data-pipeline"
  )

val versions = new {
  val flinkScala: String = "1.18.0"
  val flinkStreamingScala: String = "1.18.0"
}

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala"           % versions.flinkScala,
  "org.apache.flink" %% "flink-streaming-scala" % versions.flinkStreamingScala
)
