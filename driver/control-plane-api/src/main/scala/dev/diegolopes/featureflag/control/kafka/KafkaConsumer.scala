package dev.diegolopes.featureflag.control.kafka

import dev.diegolopes.featureflag.control.config.{ControlPlaneConfig, KafkaConfig}
import org.apache.kafka.clients.consumer.ConsumerConfig
import zio.{ZIO, ZLayer}
import zio.kafka.consumer.{Consumer, ConsumerSettings}

import java.time.Duration
import java.util.UUID

object KafkaConsumer {
  def layer(consumerGroup: String): ZLayer[ControlPlaneConfig, Nothing, Consumer] = ZLayer.scoped {
    for {
      cfg      <- ZIO.service[ControlPlaneConfig]
      clientId <- ZIO.succeed(UUID.randomUUID().toString)
      consumer <- Consumer.make(buildConsumerSettings(cfg.kafka).withClientId(clientId).withGroupId(consumerGroup))
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
