package dev.diegolopes.featureflag.control

import dev.diegolopes.featureflag.control.config.ControlPlaneConfig
import dev.diegolopes.featureflag.platform.kafka.KafkaProducer
import dev.diegolopes.featureflag.platform.postgres.PostgresDatabase
import io.github.gaelrenoux.tranzactio.doobie.Database
import zio.*
import zio.kafka.producer.Producer

object Main extends ZIOAppDefault {
  private val effect = for {
    _ <- ZIO.service[ControlPlaneConfig]
    _ <- ZIO.service[Producer]
    _ <- ZIO.service[Database]
  } yield ()

  def run: Task[Unit] = effect.provideLayer {
    ControlPlaneConfig.layer ++
      (ControlPlaneConfig.layer >>> (ZLayer
        .fromFunction((cfg: ControlPlaneConfig) => cfg.database) >>> PostgresDatabase.layer)) ++
      (ControlPlaneConfig.layer >>> (ZLayer
        .fromFunction((cfg: ControlPlaneConfig) => cfg.kafka) >>> KafkaProducer.layer))
  }
}
