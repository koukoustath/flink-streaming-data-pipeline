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

class UserMovingAverageJob(source: SourceFunction[UserAction], sink: SinkFunction[String])
  extends FlinkJob {

  def job(executionEnvironment: StreamExecutionEnvironment): Unit = {

    val windowDuration = Time.seconds(10)
    val windowOverlap = Time.seconds(5)

    // the event timestamps are in monotonously ascending trend, it is used as the event time
    val userActionDataStream: DataStream[UserAction] =
      executionEnvironment.addSource(source).assignAscendingTimestamps(_.eventTimestamp)

    // the moving average event count for Users depending on the event type
    val results: DataStream[String] = userActionDataStream
      .keyBy(ua => (ua.userId, ua.eventType))
      .window(SlidingEventTimeWindows.of(windowDuration, windowOverlap))
      .process(new ProcessWindowFunction[UserAction, String, (String, Int), TimeWindow] {
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
      })

    results.addSink(sink)
  }
}
