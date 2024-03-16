package co.example.utils

import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.watermark.Watermark

class TestSourceContext[T] extends SourceContext[T] {

  var collectedElements: List[T] = List()

  def collect(element: T): Unit = collectedElements :+= element

  def collectWithTimestamp(element: T, timestamp: Long): Unit = {
    // Not needed for my tests
  }

  def emitWatermark(mark: Watermark): Unit = {
    // Not needed for my tests
  }

  def markAsTemporarilyIdle(): Unit = {
    // Not needed for my tests
  }

  def getCheckpointLock: AnyRef =
    // Not needed for my tests
    null

  def close(): Unit = {
    // Not needed for my tests
  }

}
