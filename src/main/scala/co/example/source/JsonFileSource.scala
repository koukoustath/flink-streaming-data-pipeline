package co.example.source

import io.circe.Decoder
import io.circe.parser.decode
import org.apache.flink.streaming.api.functions.source.SourceFunction

class JsonFileSource[T: Decoder](path: String) extends SourceFunction[T] { // parallelism set to 1

  @volatile private var isRunning: Boolean = true

  override def run(ctx: SourceFunction.SourceContext[T]): Unit = {

    val reader = scala.io.Source.fromFile(path).bufferedReader()

    while (isRunning) {
      reader.readLine() match {
        case null =>
          reader.close()
          cancel()
        case line =>
          val decodedLine = decode[T](line) match {
            case Left(error)  =>
              println(s"bad decoding in line $line")  // TODO: Add logger
              throw error
            case Right(value) => value
          }
          ctx.collect(decodedLine)
          Thread.sleep(1000) // TODO: add randomness
      }
    }
  }

  override def cancel(): Unit =
    isRunning = false
}
