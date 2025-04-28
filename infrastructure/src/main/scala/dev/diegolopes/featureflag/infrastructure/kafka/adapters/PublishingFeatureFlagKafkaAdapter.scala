package dev.diegolopes.featureflag.infrastructure.kafka.adapters

import dev.diegolopes.featureflag.domain.featureflag.{FeatureFlag, FeatureFlagId}
import dev.diegolopes.featureflag.domain.featureflag.driven.PublishingFeatureFlag
import dev.diegolopes.featureflag.infrastructure.kafka.events.FeatureFlagEvent
import dev.diegolopes.featureflag.infrastructure.kafka.{EventSerde, Topics}
import dev.diegolopes.featureflag.infrastructure.kafka.events.FeatureFlagEvent.{
  CreatedFeatureFlagEvent,
  DeletedFeatureFlagEvent,
  UpdatedFeatureFlagEvent
}
import org.apache.kafka.clients.producer.ProducerRecord
import zio.{Task, ZLayer}
import zio.kafka.producer.Producer
import zio.kafka.serde.Serde
import io.scalaland.chimney.dsl.*

case class PublishingFeatureFlagKafkaAdapter(
    producer: Producer
) extends PublishingFeatureFlag[Task] {
  override def created(featureFlag: FeatureFlag): Task[Unit] =
    producer
      .produce(
        record = new ProducerRecord(
          Topics.FEATURE_FLAG,
          featureFlag.id.value,
          featureFlag.transformInto[CreatedFeatureFlagEvent]
        ),
        keySerializer = Serde.uuid,
        valueSerializer = EventSerde.to[FeatureFlagEvent]
      )
      .unit

  override def updated(featureFlag: FeatureFlag): Task[Unit] =
    producer
      .produce(
        record = new ProducerRecord(
          Topics.FEATURE_FLAG,
          featureFlag.id.value,
          featureFlag.transformInto[UpdatedFeatureFlagEvent]
        ),
        keySerializer = Serde.uuid,
        valueSerializer = EventSerde.to[FeatureFlagEvent]
      )
      .unit

  override def deleted(featureFlag: FeatureFlag): Task[Unit] =
    producer
      .produce(
        record = new ProducerRecord(
          Topics.FEATURE_FLAG,
          featureFlag.id.value,
          featureFlag.transformInto[DeletedFeatureFlagEvent]
        ),
        keySerializer = Serde.uuid,
        valueSerializer = EventSerde.to[FeatureFlagEvent]
      )
      .unit
}

object PublishingFeatureFlagKafkaAdapter {
  val layer: ZLayer[Producer, Nothing, PublishingFeatureFlagKafkaAdapter] =
    ZLayer.fromFunction(PublishingFeatureFlagKafkaAdapter(_))
}
