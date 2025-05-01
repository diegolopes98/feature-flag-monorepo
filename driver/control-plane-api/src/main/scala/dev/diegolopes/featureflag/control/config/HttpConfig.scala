package dev.diegolopes.featureflag.control.config

import zio.Config
import zio.config.magnolia.deriveConfig

case class HttpConfig(port: Int)

object HttpConfig {
  given config: Config[HttpConfig] = deriveConfig[HttpConfig]
}
