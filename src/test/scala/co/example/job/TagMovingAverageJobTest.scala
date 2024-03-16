package co.example.job

import co.example.model.UserAction
import co.example.specs.{TestSinkFunction, TestSourceFunction}
import co.example.specs.fixture.UserActionFixture
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.scalatest.{GivenWhenThen, Tag}
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers

class TagMovingAverageJobTest
  extends AnyFunSpecLike
  with Matchers
  with GivenWhenThen
  with UserActionFixture {
  def tag: Seq[Tag] = Seq(new Tag("unit test"))

  describe("A TagMovingAverageJob") {
    it(
      "should calculate the moving average event count for Tags depending on the event type",
      tag: _*
    ) {

      Given("a SourceFunction of UserActions")
      val testSource: SourceFunction[UserAction] = new TestSourceFunction[UserAction](events)

      And("a SinkFunction")
      val testSink: TestSinkFunction[String] = new TestSinkFunction[String]

      When("an instance of TagMovingAverageJob is created")
      val job: TagMovingAverageJob = new TagMovingAverageJob(testSource, testSink)

      And("the job is executed")
      val jobResult = job.run("test-job")

      Then("the collected output elements should be the expected ones")
      testSink.getResults(jobResult) should contain theSameElementsAs List(
        "Window {2023-10-01T06:09:05Z, 2023-10-01T06:09:15Z} - (TagId, EventType): (189708586, 1) - Number of events: 1",
        "Window {2023-10-01T06:09:05Z, 2023-10-01T06:09:15Z} - (TagId, EventType): (71924100, 1) - Number of events: 1",
        "Window {2023-10-01T06:09:10Z, 2023-10-01T06:09:20Z} - (TagId, EventType): (189708586, 1) - Number of events: 1",
        "Window {2023-10-01T06:09:10Z, 2023-10-01T06:09:20Z} - (TagId, EventType): (71924100, 1) - Number of events: 1",
        "Window {2023-10-01T07:47:10Z, 2023-10-01T07:47:20Z} - (TagId, EventType): (172707124, 1) - Number of events: 1",
        "Window {2023-10-01T07:47:10Z, 2023-10-01T07:47:20Z} - (TagId, EventType): (2776026197, 1) - Number of events: 1",
        "Window {2023-10-01T07:47:10Z, 2023-10-01T07:47:20Z} - (TagId, EventType): (85725439, 1) - Number of events: 1",
        "Window {2023-10-01T07:47:15Z, 2023-10-01T07:47:25Z} - (TagId, EventType): (172707124, 1) - Number of events: 1",
        "Window {2023-10-01T07:47:15Z, 2023-10-01T07:47:25Z} - (TagId, EventType): (2776026197, 1) - Number of events: 1",
        "Window {2023-10-01T07:47:15Z, 2023-10-01T07:47:25Z} - (TagId, EventType): (85725439, 1) - Number of events: 1",
        "Window {2023-10-01T10:01:20Z, 2023-10-01T10:01:30Z} - (TagId, EventType): (2780252810, 1) - Number of events: 1",
        "Window {2023-10-01T10:01:25Z, 2023-10-01T10:01:35Z} - (TagId, EventType): (2780252810, 1) - Number of events: 1"
      )
    }
  }
}
