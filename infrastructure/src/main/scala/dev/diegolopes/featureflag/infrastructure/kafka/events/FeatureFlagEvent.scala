package dev.diegolopes.featureflag.infrastructure.kafka.events

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagId
import dev.diegolopes.featureflag.infrastructure.kafka.events
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder, JsonEncoderDerivation}

import java.util.UUID

sealed trait FeatureFlagEvent {
  val id: FeatureFlagId
}

object FeatureFlagEvent {
  case class CreatedFeatureFlagEvent(
      id: FeatureFlagId,
      name: String,
      description: Option[String],
      value: Boolean
  ) extends FeatureFlagEvent

  case class DeletedFeatureFlagEvent(
      id: FeatureFlagId
  ) extends FeatureFlagEvent

  case class UpdatedFeatureFlagEvent(
      id: FeatureFlagId,
      description: Option[String],
      value: Boolean
  ) extends FeatureFlagEvent

  given idEncoder: JsonEncoder[FeatureFlagId] =
    JsonEncoder[String].contramap(_.toString)

  given idDecoder: JsonDecoder[FeatureFlagId] =
    JsonDecoder[String].map(uuidStr => FeatureFlagId.from(UUID.fromString(uuidStr)))

  given encoder: JsonEncoder[FeatureFlagEvent] = DeriveJsonEncoder.gen[FeatureFlagEvent]
  given decoder: JsonDecoder[FeatureFlagEvent] = DeriveJsonDecoder.gen[FeatureFlagEvent]
}
