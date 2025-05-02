package dev.diegolopes.featureflag.platform.config

case class KafkaConfig(
    broker: String,
    closeTimeout: Long,
    requestTimeout: Long,
    retryBackoff: Long,
    pollInterval: Long,
    pollTimeout: Long
)
