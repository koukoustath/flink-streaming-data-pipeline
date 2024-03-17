package co.example

import co.example.job.UserMovingAverageJob
import co.example.model.UserAction
import co.example.source.JsonFileSource
import org.apache.flink.streaming.api.functions.sink.{PrintSinkFunction, SinkFunction}

object UserMovingAverage extends App {

  val path: String = "src/main/resources/streaming_dataset.json"
  val source: JsonFileSource[UserAction] = new JsonFileSource[UserAction](path)
  val sink: SinkFunction[String] = new PrintSinkFunction[String]

  val job = new UserMovingAverageJob(source, sink)
  job.run("user-actions-moving-average-count-pipeline")

}
