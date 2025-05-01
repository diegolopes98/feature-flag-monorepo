package dev.diegolopes.featureflag.control

import dev.diegolopes.featureflag.control.config.ControlPlaneConfig
import zio.*

object Main extends ZIOAppDefault {
  private val effect = for {
    config <- ZIO.service[ControlPlaneConfig]
    _      <- Console.printLine(config)
  } yield ()

  def run: Task[Unit] = effect.provideLayer(ControlPlaneConfig.layer)
}
