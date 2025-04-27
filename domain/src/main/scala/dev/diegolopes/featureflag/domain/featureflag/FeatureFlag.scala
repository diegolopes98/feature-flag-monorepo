package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagId

import java.util.UUID

final case class FeatureFlag(
    id: FeatureFlagId,
    name: String,
    description: Option[String],
    value: Boolean,
    active: Boolean
) {
  def activate: FeatureFlag   = this.copy(active = true)
  def deactivate: FeatureFlag = this.copy(active = false)
}

object FeatureFlag {
  def apply(
      id: UUID,
      name: String,
      description: Option[String],
      value: Boolean,
      active: Boolean
  ): Either[List[FeatureFlagError], FeatureFlag] =
    for {
      _ <- FeatureFlagValidator.validate(id, name).toEither
    } yield FeatureFlag(FeatureFlagId.from(id), name, description, value, active)
}
