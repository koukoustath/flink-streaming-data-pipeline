package co.example

import co.example.model.UserAction
import co.example.source.JsonFileSource
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, createTypeInformation}

object Pipeline extends App {
  private val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  val path: String =
    "/Users/stathiskoukouvinos/IdeaProjects/flink-streaming-data-pipeline/src/main/resources/test_streaming_dataset.jsonl"

  val source: JsonFileSource[UserAction] = new JsonFileSource[UserAction](path)
  val dataStream: DataStream[UserAction] = env.addSource(source)

  dataStream.print()

  env.execute("Flink-pipeline")
}
