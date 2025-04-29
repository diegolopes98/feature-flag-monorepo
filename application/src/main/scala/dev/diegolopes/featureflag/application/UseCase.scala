package dev.diegolopes.featureflag.application

import zio.IO

trait UseCase[Input, Error, Output] {
  def execute(input: Input): IO[Error, Output]
}
