package dev.diegolopes.featureflag.application.stubs

import zio.*

trait Stub[R, E, A] {
  protected val stubsRef: Ref[Map[String, List[ZIO[R, E, A]]]]

  protected def getOrFail(method: String): ZIO[R, E, A] =
    for {
      stubs <- stubsRef.get
      result <- stubs.get(method) match {
                  case Some(effect :: rest) =>
                    stubsRef.update(_.updated(method, rest)) *> effect
                  case _ =>
                    ZIO.die(new RuntimeException(s"Stub effect not set for method: $method"))
                }
    } yield result

  protected def addStub(method: String, stub: ZIO[R, E, A]): UIO[Unit] =
    stubsRef.update { stubs =>
      stubs.updatedWith(method) {
        case Some(effects) => Some(effects :+ stub)
        case None          => Some(List(stub))
      }
    }
}
