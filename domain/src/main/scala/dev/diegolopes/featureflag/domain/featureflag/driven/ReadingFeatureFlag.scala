package dev.diegolopes.featureflag.domain.featureflag.driven

import dev.diegolopes.featureflag.domain.featureflag.{FeatureFlag, FeatureFlagId}

trait ReadingFeatureFlag[F[_]] {
  def findById(id: FeatureFlagId): F[Option[FeatureFlag]]
}
