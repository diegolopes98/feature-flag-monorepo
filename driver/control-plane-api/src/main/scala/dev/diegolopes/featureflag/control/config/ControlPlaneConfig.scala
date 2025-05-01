package dev.diegolopes.featureflag.control.config

import zio.{Config, ULayer, ZLayer}
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider

case class ControlPlaneConfig(
    database: DatabaseConfig,
    http: HttpConfig,
    kafka: KafkaConfig
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
