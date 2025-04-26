package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError.InvalidId
import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagId

final case class FeatureFlag(
    id: FeatureFlagId,
    name: String
)

object FeatureFlag {
  def apply(id: String, name: String): Either[List[FeatureFlagError], FeatureFlag] =
    for {
      _        <- FeatureFlagValidator.validate(id, name).toEither
      parsedId <- FeatureFlagId.from(id).toRight(List(InvalidId))
    } yield FeatureFlag(parsedId, name)
}
