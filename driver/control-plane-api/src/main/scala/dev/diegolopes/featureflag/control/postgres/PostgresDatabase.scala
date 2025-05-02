package dev.diegolopes.featureflag.control.postgres

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import dev.diegolopes.featureflag.control.config.{ControlPlaneConfig, DatabaseConfig}
import doobie.util.log.LogHandler
import io.github.gaelrenoux.tranzactio.doobie.*
import zio.{Task, ZLayer}
import zio.interop.catz.*

object PostgresDatabase {
  given doobieContext: DbContext = DbContext(logHandler = LogHandler.jdkLogHandler[Task])

  val layer: ZLayer[ControlPlaneConfig, Nothing, Database] =
    ZLayer.fromFunction { (cfg: ControlPlaneConfig) =>
      new HikariDataSource(buildHikariConfig(cfg.database))
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
