package dev.diegolopes.featureflag.platform.postgres

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import dev.diegolopes.featureflag.platform.config.DatabaseConfig
import doobie.util.log.LogHandler
import io.github.gaelrenoux.tranzactio.doobie.*
import zio.{Task, ZLayer}
import zio.interop.catz.*

object PostgresDatabase {
  given doobieContext: DbContext = DbContext(logHandler = LogHandler.jdkLogHandler[Task])

  val layer: ZLayer[DatabaseConfig, Nothing, Database] =
    ZLayer.fromFunction { (cfg: DatabaseConfig) =>
      new HikariDataSource(buildHikariConfig(cfg))
    } >>> Database.fromDatasource

  private def buildHikariConfig(config: DatabaseConfig): HikariConfig = {
    val hc = new HikariConfig()

    hc.setDriverClassName(config.driver)
    hc.setJdbcUrl(config.url)
    hc.setUsername(config.user)
    hc.setPassword(config.password)
    hc.setMaximumPoolSize(config.poolSize)
    hc.setMinimumIdle(config.minimumIdle)

    hc
  }
}
