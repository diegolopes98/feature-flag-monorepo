package dev.diegolopes.featureflag.domain.featureflag

import java.util.UUID
import scala.util.Try

opaque type FeatureFlagId = FeatureFlagId.Type

object FeatureFlagId {
  type Type = UUID

  def from(id: UUID): FeatureFlagId = id

  def from(id: String): Either[FeatureFlagError.InvalidId.type, FeatureFlagId] =
    Try(UUID.fromString(id)).toEither match {
      case Right(validId) => Right(FeatureFlagId.from(validId))
      case Left(_)        => Left(FeatureFlagError.InvalidId)
    }
}
