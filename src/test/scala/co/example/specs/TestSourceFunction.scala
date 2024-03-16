package co.example.specs

import org.apache.flink.streaming.api.functions.source.SourceFunction

class TestSourceFunction[T](events: List[T]) extends SourceFunction[T] {

  override def run(ctx: SourceFunction.SourceContext[T]): Unit =
    for (e <- events) {
      ctx.collect(e)
      Thread.sleep(1000)
    }

  override def cancel(): Unit = {
    // ignore cancel, finite anyway
  }
}
