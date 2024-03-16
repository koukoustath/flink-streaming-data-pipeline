package co.example.core

import org.apache.flink.api.common.JobExecutionResult
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

trait FlinkJob {

  def job(executionEnvironment: StreamExecutionEnvironment): Unit

  def run(jobName: String): JobExecutionResult = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    job(env)
    env.execute(jobName)
  }
}
