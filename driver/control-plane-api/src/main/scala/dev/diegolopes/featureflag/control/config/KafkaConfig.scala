package dev.diegolopes.featureflag.control.config

import zio.Config
import zio.config.magnolia.deriveConfig

case class KafkaConfig(broker: String)

object KafkaConfig {
  given config: Config[KafkaConfig] = deriveConfig[KafkaConfig]
}
