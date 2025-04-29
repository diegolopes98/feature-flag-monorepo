package dev.diegolopes.featureflag.control

import zio.*
import java.io.IOException

object Main extends ZIOAppDefault {
  def run: IO[IOException, Unit] = Console.printLine("Hello, World!")
}
