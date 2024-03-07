package co.example

import co.example.model.UserAction
import co.example.source.JsonFileSource
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{
  createTypeInformation,
  DataStream,
  StreamExecutionEnvironment
}
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object Pipeline extends App {
  private val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  val path: String = "src/main/resources/streaming_dataset.jsonl"

  val source: JsonFileSource[UserAction] = new JsonFileSource[UserAction](path)

  private val windowDuration = Time.seconds(10)
  private val windowOverlap = Time.seconds(5)

  // the event timestamps are in monotonously ascending trend, it is used as the event time
  private val userActionDataStream: DataStream[UserAction] =
    env.addSource(source).assignAscendingTimestamps(_.eventTimestamp)

  // the moving average event count for Users depending on the event type
  userActionDataStream
    .keyBy(ua => (ua.userId, ua.eventType))
    .window(SlidingEventTimeWindows.of(windowDuration, windowOverlap))
    .process(
      new ProcessWindowFunction[UserAction, String, (String, Int), TimeWindow] {

        def process(
          key: (String, Int),
          context: Context,
          elements: Iterable[UserAction],
          out: Collector[String]
        ): Unit = {
          val eventsNum = elements.size
          val windowStart = java.time.Instant.ofEpochMilli(context.window.getStart)
          val windowEnd = java.time.Instant.ofEpochMilli(context.window.getEnd)
          out.collect(
            s"Window {$windowStart, $windowEnd} - (UserId, EventType): (${key._1},${key._2}) - Number of events: $eventsNum"
          )
        }
      }
    )
    .print()

  // the moving average event count for Tags depending on the event type
  userActionDataStream
    .keyBy(_.eventType)
    .window(SlidingEventTimeWindows.of(windowDuration, windowOverlap))
    .process(new ProcessWindowFunction[UserAction, String, Int, TimeWindow] {

      def process(
        key: Int,
        context: Context,
        elements: Iterable[UserAction],
        out: Collector[String]
      ): Unit = {
        val windowStart = java.time.Instant.ofEpochMilli(context.window.getStart)
        val windowEnd = java.time.Instant.ofEpochMilli(context.window.getEnd)
        elements
          .flatMap(_.tags)
          .flatten
          .groupBy(identity)
          .map(kV =>
            s"Window {$windowStart, $windowEnd} - (TagId, EventType): (${kV._1}, $key) - Number of events: ${kV._2.size}"
          )
          .foreach(out.collect)
      }
    })
    .print()

  env.execute("Flink-pipeline")
}
