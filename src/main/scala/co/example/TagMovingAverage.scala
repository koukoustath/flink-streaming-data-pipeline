package co.example

import co.example.job.TagMovingAverageJob
import co.example.model.UserAction
import co.example.source.JsonFileSource
import org.apache.flink.streaming.api.functions.sink.{PrintSinkFunction, SinkFunction}

object TagMovingAverage extends App {

  val path: String = "src/main/resources/streaming_dataset.json"
  val source: JsonFileSource[UserAction] = new JsonFileSource[UserAction](path)
  val sink: SinkFunction[String] = new PrintSinkFunction[String]

  val job = new TagMovingAverageJob(source, sink)
  job.run("tags-moving-average-count-pipeline")

}
