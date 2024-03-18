package co.example.specs.fixture

import co.example.model.UserAction
import org.scalatest.{TestSuite, TestSuiteMixin}

trait UserActionFixture extends TestSuiteMixin { this: TestSuite =>

  val events: List[UserAction] = List(
    UserAction(
      "3d9a78370396df7a5844ff82e0eeb0de",
      1696140551727L, // 2023-10-01 06:09:11.727
      1,
      "10.2217/fon-2023-0298",
      Some(List("189708586"))
    ),
    UserAction(
      "3d9a78370396df7a5844ff82e0eeb0de",
      1696140554625L, // 2023-10-01 06:09:14.625
      2,
      "10.1056/CAT.23.0116",
      Some(List("189708586"))
    ),
    UserAction(
      "3d9a78370396df7a5844ff82e0eeb0de",
      1696140559543L, // 2023-10-01 06:09:19.543
      1,
      "10.1056/CAT.23.0116",
      Some(List("189708586", "71924125"))
    ),
    UserAction(
      "261510a0aab54ec98e3f7e0ff083735f",
      1696146435938L, // 2023-10-01 07:47:15.938
      1,
      "10.1089/soro.2021.0067",
      Some(List("172707124"))
    ),
    UserAction(
      "01fe0f717cdde0a3d71ee5d82af6deb9",
      1696154487288L, // 2023-10-01 10:01:27.288
      2,
      "10.1056/NEJMoa2109965",
      Some(List("2780252810", "2993838110"))
    ),
    UserAction(
      "be3dd0d5bd2ba42aa0f7161dfec0cf07",
      1696154489636L, // 2023-10-01 10:01:29.636
      2,
      "10.1056/NEJMoa210hgj",
      Some(List("2780252810", "2779161974"))
    ),
    UserAction(
      "c1ef5f7406076d866ff31c9027b2c3ea",
      1696163923331L, // 2023-10-01 12:38:43.331
      1,
      "10.1145/contrib-99659377364",
      None
    )
  )
}
