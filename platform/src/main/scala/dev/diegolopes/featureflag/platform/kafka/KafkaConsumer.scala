package dev.diegolopes.featureflag.platform.kafka

import dev.diegolopes.featureflag.platform.config.KafkaConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import zio.kafka.consumer.{Consumer, ConsumerSettings}
import zio.{ZIO, ZLayer}

import java.time.Duration
import java.util.UUID

object KafkaConsumer {
  def layer(consumerGroup: String): ZLayer[KafkaConfig, Nothing, Consumer] = ZLayer.scoped {
    for {
      cfg      <- ZIO.service[KafkaConfig]
      clientId <- ZIO.succeed(UUID.randomUUID().toString)
      consumer <- Consumer.make(buildConsumerSettings(cfg).withClientId(clientId).withGroupId(consumerGroup))
    } yield consumer
  }.orDie

  private def buildConsumerSettings(kafka: KafkaConfig) =
    ConsumerSettings(
      closeTimeout = Duration.ofSeconds(kafka.closeTimeout),
      pollTimeout = Duration.ofMillis(kafka.pollTimeout),
      properties = Map(
        ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG       -> kafka.requestTimeout.toString,
        ConsumerConfig.RETRY_BACKOFF_MS_CONFIG         -> kafka.retryBackoff.toString,
        ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG -> false.toString
      )
    ).withBootstrapServers(kafka.broker.split(",").toList)
}
