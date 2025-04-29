package dev.diegolopes.featureflag.data

import zio.*
import java.io.IOException

object Main extends ZIOAppDefault {
  def run: IO[IOException, Unit] = Console.printLine("Hello, World!")
}
