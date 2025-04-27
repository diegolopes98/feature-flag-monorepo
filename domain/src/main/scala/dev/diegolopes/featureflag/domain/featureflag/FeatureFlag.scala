package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagId

import java.util.UUID

final case class FeatureFlag(
    id: FeatureFlagId,
    name: String,
    value: Boolean
)

object FeatureFlag {
  def apply(id: UUID, name: String, value: Boolean): Either[List[FeatureFlagError], FeatureFlag] =
    for {
      _ <- FeatureFlagValidator.validate(id, name).toEither
    } yield FeatureFlag(FeatureFlagId.from(id), name, value)
}
