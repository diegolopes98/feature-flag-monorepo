package dev.diegolopes.featureflag.application.featureflag.create

import dev.diegolopes.featureflag.application.UseCase
import dev.diegolopes.featureflag.application.featureflag.create.CreateFeatureFlagError.{InternalError, ValidationError}
import dev.diegolopes.featureflag.domain.featureflag.FeatureFlag
import dev.diegolopes.featureflag.domain.featureflag.driven.WritingFeatureFlag
import zio.{IO, UIO, ZIO, ZLayer}

import java.util.UUID

case class CreateFeatureFlagUseCase(
    writingFeatureFlag: WritingFeatureFlag[UIO]
) extends UseCase[CreateFeatureFlagInput, CreateFeatureFlagError, Unit] {
  override def apply(input: CreateFeatureFlagInput): IO[CreateFeatureFlagError, Unit] =
    for {
      flag <- createFlag(input)
      _    <- saveFlag(flag)
    } yield ()

  private def createFlag(input: CreateFeatureFlagInput) =
    ZIO
      .fromEither(
        FeatureFlag(
          id = UUID.randomUUID(),
          name = input.name,
          description = input.description,
          value = input.value,
          active = true
        )
      )
      .mapError(ValidationError(_))

  private def saveFlag(flag: FeatureFlag) =
    writingFeatureFlag.save(flag).catchAllDefect(d => ZIO.fail(InternalError(d)))
}

object CreateFeatureFlagUseCase {
  val layer: ZLayer[
    WritingFeatureFlag[UIO],
    CreateFeatureFlagError,
    CreateFeatureFlagUseCase
  ] = ZLayer.fromFunction(CreateFeatureFlagUseCase(_))
}
