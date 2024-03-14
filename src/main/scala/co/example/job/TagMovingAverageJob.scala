package co.example.job

import co.example.core.FlinkJob
import co.example.model.UserAction
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class TagMovingAverageJob(source: SourceFunction[UserAction], sink: SinkFunction[String])
  extends FlinkJob {

  def job(executionEnvironment: StreamExecutionEnvironment): Unit = {

    val windowDuration = Time.seconds(10)
    val windowOverlap = Time.seconds(5)

    // the event timestamps are in monotonously ascending trend, it is used as the event time
    val userActionDataStream: DataStream[UserAction] =
      executionEnvironment.addSource(source).assignAscendingTimestamps(_.eventTimestamp)

    // the moving average event count for Tags depending on the event type
    val results: DataStream[String] = userActionDataStream
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

    results.addSink(sink)
  }
}
