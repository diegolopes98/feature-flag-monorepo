package dev.diegolopes.featureflag.application.stubs

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlag
import dev.diegolopes.featureflag.domain.featureflag.driven.PublishingFeatureFlag
import zio.{Ref, Task, ULayer, ZLayer}

case class PublishingFeatureFlagStub(
    override protected val stubsRef: Ref[Map[String, List[Task[Unit]]]]
) extends Stub[Any, Throwable, Unit]
    with PublishingFeatureFlag[Task] {

  override def created(featureFlag: FeatureFlag): Task[Unit] = getOrFail("created")
  override def updated(featureFlag: FeatureFlag): Task[Unit] = getOrFail("updated")
  override def deleted(featureFlag: FeatureFlag): Task[Unit] = getOrFail("deleted")

  def Created(stub: Task[Unit]): Task[Unit] = addStub("created", stub)
  def Updated(stub: Task[Unit]): Task[Unit] = addStub("updated", stub)
  def Deleted(stub: Task[Unit]): Task[Unit] = addStub("deleted", stub)
}

object PublishingFeatureFlagStub {
  val layer: ULayer[PublishingFeatureFlagStub & PublishingFeatureFlag[Task]] =
    ZLayer.fromZIO {
      Ref.make(Map.empty[String, List[Task[Unit]]]).map(PublishingFeatureFlagStub(_))
    }
}
