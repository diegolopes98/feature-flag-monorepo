package dev.diegolopes.featureflag.control.kafka

import dev.diegolopes.featureflag.control.config.{ControlPlaneConfig, KafkaConfig}
import org.apache.kafka.clients.producer.ProducerConfig
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.{ZIO, ZLayer}

import java.time.Duration

object KafkaProducer {
  val layer: ZLayer[ControlPlaneConfig, Nothing, Producer] =
    ZLayer.scoped(ZIO.serviceWithZIO[ControlPlaneConfig](cfg => Producer.make(buildProducerSettings(cfg.kafka)).orDie))

  private def buildProducerSettings(kafka: KafkaConfig) =
    ProducerSettings(
      closeTimeout = Duration.ofSeconds(kafka.closeTimeout),
      properties = Map(
        ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG -> kafka.requestTimeout.toString,
        ProducerConfig.RETRY_BACKOFF_MS_CONFIG   -> kafka.retryBackoff.toString
      )
    ).withBootstrapServers(kafka.broker.split(",").toList)
}
