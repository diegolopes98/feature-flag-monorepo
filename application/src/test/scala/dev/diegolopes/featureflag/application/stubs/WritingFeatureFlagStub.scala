package dev.diegolopes.featureflag.application.stubs

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlag
import dev.diegolopes.featureflag.domain.featureflag.driven.WritingFeatureFlag
import zio.*

object WritingFeatureFlagStub {
  private def make(effect: UIO[Unit]): ULayer[WritingFeatureFlag[UIO]] =
    ZLayer.succeed(
      new WritingFeatureFlag[UIO] {
        override def save(featureFlag: FeatureFlag): UIO[Unit] = effect
      }
    )

  val successLayer: ULayer[WritingFeatureFlag[UIO]] =
    make(ZIO.unit)

  val defectLayer: ULayer[WritingFeatureFlag[UIO]] =
    make(ZIO.die(new RuntimeException("Fake test defect")))
}
