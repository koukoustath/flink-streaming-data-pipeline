package co.example.model

import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}

// TODO: check if a custom decoder will handle the null case as an Empty List (to avoid Option[List])
@ConfiguredJsonCodec
case class UserAction(
  userId: String,
  eventTimestamp: Long,
  eventType: Int,
  doi: String,
  tags: Option[List[String]])

object UserAction {
  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames
}
