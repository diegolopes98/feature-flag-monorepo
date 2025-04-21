package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagId
import dev.diegolopes.featureflag.domain.UpperSnakeCaseString

final case class FeatureFlag(
    id: FeatureFlagId,
    name: UpperSnakeCaseString
)
