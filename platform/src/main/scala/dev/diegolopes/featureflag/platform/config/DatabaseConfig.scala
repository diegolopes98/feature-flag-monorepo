package dev.diegolopes.featureflag.platform.config

case class DatabaseConfig(
    driver: String,
    url: String,
    user: String,
    password: String,
    poolSize: Int,
    threadPoolSize: Int,
    minimumIdle: Int
)
