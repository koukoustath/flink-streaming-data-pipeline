package co.example.source

import io.circe.Decoder
import io.circe.parser.decode
import org.apache.flink.streaming.api.functions.source.SourceFunction

class JsonFileSource[T: Decoder](path: String) extends SourceFunction[T] {

  @volatile private var isRunning: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[T]): Unit = {

    var reader = scala.io.Source.fromFile(path).bufferedReader()

    while (isRunning) {
      reader.readLine() match {
        case null =>
          reader.close()
          reader = scala.io.Source.fromFile(path).bufferedReader()
        case line =>
          val decodedLine = decode[T](line) match {
            case Left(error)  =>
              println(s"bad decoding in line $line")  // TODO: Add logger
              throw error
            case Right(value) => value
          }
          ctx.collect(decodedLine)
          Thread.sleep(1000)
      }
    }
  }

  override def cancel(): Unit =
    isRunning = false
}
