package dev.diegolopes.featureflag.domain.featureflag.driven

import dev.diegolopes.featureflag.domain.featureflag.{FeatureFlag, FeatureFlagId}

trait FindingFeatureFlag[F[_]] {
  def findById(id: FeatureFlagId): F[Option[FeatureFlag]]
}
