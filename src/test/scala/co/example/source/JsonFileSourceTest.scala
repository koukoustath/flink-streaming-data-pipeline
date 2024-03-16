package co.example.source

import co.example.model.UserAction
import co.example.specs.TestSourceContext
import org.scalatest.{GivenWhenThen, Tag}
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers

class JsonFileSourceTest extends AnyFunSpecLike with Matchers with GivenWhenThen {
  def tag: Seq[Tag] = Seq(new Tag("unit test"))

  describe("A JsonFileSource") {
    it("should simulate a realtime source by generating data from a Json file", tag: _*) {

      Given("a path of the Json file with user action events")
      val path = "src/test/resources/test_streaming_dataset.jsonl"

      When("an instance of JsonFileSource of UserAction is created")
      val jsonFileSource: JsonFileSource[UserAction] = new JsonFileSource[UserAction](path)

      And("the run method is executed with a TestSourceContext")
      val testSourceContext: TestSourceContext[UserAction] = new TestSourceContext[UserAction]
      jsonFileSource.run(testSourceContext)

      Then(
        "the generated elements should be the expected ones and in the same order as in the file"
      )
      testSourceContext.collectedElements should contain theSameElementsInOrderAs List(
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

    }
  }
}
