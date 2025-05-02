package dev.diegolopes.featureflag.platform.redis

import dev.diegolopes.featureflag.platform.config.RedisConfig as PlatformRedisConfig
import zio.*
import zio.redis.*

import scala.language.postfixOps

object RedisDatabase {
  val layer: ZLayer[PlatformRedisConfig, Nothing, Redis] =
    ZLayer.fromFunction(mapRedisClusterConfig) ++ ZLayer.succeed(CodecSupplier.utf8) >>> Redis.cluster.orDie

  private def mapRedisClusterConfig(cfg: PlatformRedisConfig) = {
    val uris = cfg.hosts.split(",").map(RedisUri.apply)
    RedisClusterConfig(Chunk.from(uris))
  }
}
