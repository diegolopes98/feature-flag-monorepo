package dev.diegolopes.featureflag.domain.featureflag.driven

import dev.diegolopes.featureflag.domain.featureflag.{FeatureFlag, FeatureFlagId}

trait WritingFeatureFlag[F[_]] {
  def save(featureFlag: FeatureFlag): F[Unit]
  def update(featureFlag: FeatureFlag): F[Unit]
  def delete(id: FeatureFlagId): F[Unit]
}
