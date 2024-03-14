package co.example

import co.example.job.UserActionMovingAverageJob
import co.example.model.UserAction
import co.example.source.JsonFileSource
import org.apache.flink.streaming.api.functions.sink.{PrintSinkFunction, SinkFunction}

object UserActionMovingAverage extends App {

  val path: String = "src/main/resources/streaming_dataset.json"
  val source: JsonFileSource[UserAction] = new JsonFileSource[UserAction](path)
  val sink: SinkFunction[String] = new PrintSinkFunction[String]

  val job = new UserActionMovingAverageJob(source, sink)
  job.run("user-actions-moving-average-count-pipeline")

}
