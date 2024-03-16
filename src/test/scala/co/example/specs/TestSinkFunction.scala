package co.example.specs

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.api.common.accumulators.ListAccumulator
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}

class TestSinkFunction[T] extends RichSinkFunction[T] {

  private val accName: String = "results"

  override def open(parameters: Configuration): Unit =
    getRuntimeContext.addAccumulator(accName, new ListAccumulator[T])

  override def invoke(value: T, context: SinkFunction.Context): Unit =
    getRuntimeContext.getAccumulator(accName).add(value)

  def getResults(jobExecutionResult: JobExecutionResult): java.util.List[T] = {
    //import scala.collection.JavaConverters._
    val l: java.util.List[T] = jobExecutionResult.getAccumulatorResult(accName)
    l//.asScala.toList
  }
}
