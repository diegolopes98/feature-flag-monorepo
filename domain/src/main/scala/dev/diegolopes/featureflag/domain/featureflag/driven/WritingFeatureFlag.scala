package dev.diegolopes.featureflag.domain.featureflag.driven

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlag

trait WritingFeatureFlag[F[_]] {
  def save(featureFlag: FeatureFlag): F[Unit]
}
