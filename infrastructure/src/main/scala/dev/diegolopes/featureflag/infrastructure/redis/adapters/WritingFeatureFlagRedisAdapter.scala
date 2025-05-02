package dev.diegolopes.featureflag.infrastructure.redis.adapters

import dev.diegolopes.featureflag.domain.featureflag.{FeatureFlag, FeatureFlagId}
import dev.diegolopes.featureflag.domain.featureflag.driven.WritingFeatureFlag
import zio.redis.Redis
import zio.{Task, ZLayer}

case class WritingFeatureFlagRedisAdapter(redis: Redis) extends WritingFeatureFlag[Task] {
  override def save(featureFlag: FeatureFlag): Task[Unit] =
    redis.set(featureFlag.id.value.toString, featureFlag.value).unit

  override def update(featureFlag: FeatureFlag): Task[Unit] =
    save(featureFlag)

  override def delete(id: FeatureFlagId): Task[Unit] =
    redis.del(id.value.toString).unit
}

object WritingFeatureFlagRedisAdapter {
  val layer: ZLayer[Redis, Nothing, WritingFeatureFlag[Task]] =
    ZLayer.fromFunction(WritingFeatureFlagRedisAdapter(_))
}
