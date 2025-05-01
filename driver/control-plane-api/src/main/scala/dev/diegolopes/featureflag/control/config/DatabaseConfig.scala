package dev.diegolopes.featureflag.control.config

import zio.Config
import zio.config.magnolia.deriveConfig

case class DatabaseConfig(
    driver: String,
    url: String,
    user: String,
    password: String,
    poolSize: Int,
    threadPoolSize: Int,
    minimumIdle: Int
)

object DatabaseConfig {
  given config: Config[DatabaseConfig] = deriveConfig[DatabaseConfig]
}
