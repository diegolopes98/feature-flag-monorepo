package dev.diegolopes.featureflag.domain.featureflag.driven

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlag

trait PublishingFeatureFlag[F[_]] {
  def created(featureFlag: FeatureFlag): F[Unit]
  def updated(featureFlag: FeatureFlag): F[Unit]
  def deleted(featureFlag: FeatureFlag): F[Unit]
}
