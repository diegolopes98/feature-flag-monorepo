package dev.diegolopes.featureflag.control.config

import dev.diegolopes.featureflag.platform.config.{DatabaseConfig, KafkaConfig}
import zio.{Config, ULayer, ZLayer}
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider

case class ControlPlaneConfig(
    database: DatabaseConfig,
    kafka: KafkaConfig,
    httpServer: HttpServerConfig
)

object ControlPlaneConfig {
  given config: Config[ControlPlaneConfig] = deriveConfig[ControlPlaneConfig]

  val layer: ULayer[ControlPlaneConfig] =
    ZLayer.fromZIO(
      TypesafeConfigProvider
        .fromResourcePath()
        .load
        .orDie
    )
}
