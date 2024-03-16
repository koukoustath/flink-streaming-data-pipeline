package co.example.job

import co.example.model.UserAction
import co.example.utils.{TestSinkFunction, TestSourceFunction}
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatest.{GivenWhenThen, Tag}

class UserActionMovingAverageJobTest extends AnyFunSpecLike with Matchers with GivenWhenThen {
  def tag: Seq[Tag] = Seq(new Tag("unit test"))

  val events: List[UserAction] = List(
    UserAction(
      "3d9a78370396df7a5844ff82e0eeb0de",
      1696140551727L,
      1,
      "10.2217/fon-2023-0298",
      Some(List("189708586", "71924100"))
    ),
    UserAction(
      "261510a0aab54ec98e3f7e0ff083735f",
      1696146435938L,
      1,
      "10.1089/soro.2021.0067",
      Some(List("172707124", "2776026197", "85725439"))
    ),
    UserAction(
      "01fe0f717cdde0a3d71ee5d82af6deb9",
      1696154487288L,
      1,
      "10.1056/NEJMoa2109965",
      Some(List("2780252810"))
    ),
    UserAction(
      "c1ef5f7406076d866ff31c9027b2c3ea",
      1696163923331L,
      1,
      "10.1145/contrib-99659377364",
      None
    )
  )

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
