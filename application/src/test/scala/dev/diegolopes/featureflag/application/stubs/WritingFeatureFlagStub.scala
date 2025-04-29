package dev.diegolopes.featureflag.application.stubs

import dev.diegolopes.featureflag.domain.featureflag.{FeatureFlag, FeatureFlagId}
import dev.diegolopes.featureflag.domain.featureflag.driven.WritingFeatureFlag
import zio.*

case class WritingFeatureFlagStub(
    override protected val stubsRef: Ref[Map[String, List[UIO[Unit]]]]
) extends Stub[Any, Nothing, Unit]
    with WritingFeatureFlag[UIO] {

  override def save(featureFlag: FeatureFlag): UIO[Unit]   = getOrFail("save")
  override def update(featureFlag: FeatureFlag): UIO[Unit] = getOrFail("update")
  override def delete(id: FeatureFlagId): UIO[Unit]        = getOrFail("delete")

  def Save(stub: UIO[Unit]): UIO[Unit]   = addStub("save", stub)
  def Update(stub: UIO[Unit]): UIO[Unit] = addStub("update", stub)
  def Delete(stub: UIO[Unit]): UIO[Unit] = addStub("delete", stub)
}

object WritingFeatureFlagStub {
  val layer: ULayer[WritingFeatureFlagStub & WritingFeatureFlag[UIO]] =
    ZLayer.fromZIO {
      Ref.make(Map.empty[String, List[UIO[Unit]]]).map(WritingFeatureFlagStub(_))
    }
}
