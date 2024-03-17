package co.example.source

import io.circe.Decoder
import io.circe.parser.decode
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.slf4j.{Logger, LoggerFactory}

class JsonFileSource[T: Decoder](path: String) extends SourceFunction[T] { // parallelism set to 1

  @volatile private var isRunning: Boolean = true
  private lazy val logger: Logger = LoggerFactory.getLogger(getClass)

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
              logger.error(s"bad decoding in line $line")
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
