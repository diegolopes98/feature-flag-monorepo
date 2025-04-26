package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError.InvalidId
import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagId

import java.util.UUID

final case class FeatureFlag(
    id: FeatureFlagId,
    name: String,
    value: Boolean
)

object FeatureFlag {
  def apply(id: String, name: String, value: Boolean): Either[List[FeatureFlagError], FeatureFlag] =
    for {
      _        <- FeatureFlagValidator.validate(id, name).toEither
      parsedId <- FeatureFlagId.from(id).toRight(List(InvalidId))
    } yield FeatureFlag(parsedId, name, value)

  def apply(id: UUID, name: String, value: Boolean): Either[List[FeatureFlagError], FeatureFlag] =
    for {
      _ <- FeatureFlagValidator.validate(id, name).toEither
    } yield FeatureFlag(FeatureFlagId.from(id), name, value)
}
