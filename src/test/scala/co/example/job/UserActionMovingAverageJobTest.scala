package co.example.job

import co.example.model.UserAction
import co.example.specs.fixture.UserActionFixture
import co.example.specs.{TestSinkFunction, TestSourceFunction}
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, Tag}

class UserActionMovingAverageJobTest
  extends AnyFunSpecLike
  with Matchers
  with GivenWhenThen
  with UserActionFixture {
  def tag: Seq[Tag] = Seq(new Tag("unit test"))

  describe("A UserActionMovingAverageJob") {
    it(
      "should calculate the moving average event count for Users depending on the event type",
      tag: _*
    ) {

      Given("a SourceFunction of UserActions")
      val testSource: SourceFunction[UserAction] = new TestSourceFunction[UserAction](events)

      And("a SinkFunction")
      val testSink: TestSinkFunction[String] = new TestSinkFunction[String]

      When("an instance of UserActionMovingAverageJob is created")
      val job: UserActionMovingAverageJob = new UserActionMovingAverageJob(testSource, testSink)

      And("the job is executed")
      val jobResult = job.run("test-job")

      Then("the collected output elements should be the expected ones")
      testSink.getResults(jobResult) should contain theSameElementsAs List(
        "Window {2023-10-01T06:09:05Z, 2023-10-01T06:09:15Z} - (UserId, EventType): (3d9a78370396df7a5844ff82e0eeb0de,1) - Number of events: 1",
        "Window {2023-10-01T06:09:10Z, 2023-10-01T06:09:20Z} - (UserId, EventType): (3d9a78370396df7a5844ff82e0eeb0de,1) - Number of events: 1",
        "Window {2023-10-01T07:47:10Z, 2023-10-01T07:47:20Z} - (UserId, EventType): (261510a0aab54ec98e3f7e0ff083735f,1) - Number of events: 1",
        "Window {2023-10-01T07:47:15Z, 2023-10-01T07:47:25Z} - (UserId, EventType): (261510a0aab54ec98e3f7e0ff083735f,1) - Number of events: 1",
        "Window {2023-10-01T10:01:20Z, 2023-10-01T10:01:30Z} - (UserId, EventType): (01fe0f717cdde0a3d71ee5d82af6deb9,1) - Number of events: 1",
        "Window {2023-10-01T10:01:25Z, 2023-10-01T10:01:35Z} - (UserId, EventType): (01fe0f717cdde0a3d71ee5d82af6deb9,1) - Number of events: 1",
        "Window {2023-10-01T12:38:35Z, 2023-10-01T12:38:45Z} - (UserId, EventType): (c1ef5f7406076d866ff31c9027b2c3ea,1) - Number of events: 1",
        "Window {2023-10-01T12:38:40Z, 2023-10-01T12:38:50Z} - (UserId, EventType): (c1ef5f7406076d866ff31c9027b2c3ea,1) - Number of events: 1"
      )
    }
  }
}
