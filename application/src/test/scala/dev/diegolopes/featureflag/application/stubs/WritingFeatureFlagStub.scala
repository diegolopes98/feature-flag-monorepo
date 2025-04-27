package dev.diegolopes.featureflag.application.stubs

import dev.diegolopes.featureflag.domain.featureflag.{FeatureFlag, FeatureFlagId}
import dev.diegolopes.featureflag.domain.featureflag.driven.WritingFeatureFlag
import zio.*

object WritingFeatureFlagStub {
  def make(effect: UIO[Unit]): ULayer[WritingFeatureFlag[UIO]] =
    ZLayer.succeed(
      new WritingFeatureFlag[UIO] {
        override def save(featureFlag: FeatureFlag): UIO[Unit]   = effect
        override def update(featureFlag: FeatureFlag): UIO[Unit] = effect
        override def delete(id: FeatureFlagId): UIO[Unit]        = effect
      }
    )
}
