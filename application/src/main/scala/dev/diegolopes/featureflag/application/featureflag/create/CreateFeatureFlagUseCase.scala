package dev.diegolopes.featureflag.application.featureflag.create

import dev.diegolopes.featureflag.application.UseCase
import dev.diegolopes.featureflag.application.featureflag.create.CreateFeatureFlagError.{InternalError, ValidationError}
import dev.diegolopes.featureflag.domain.featureflag.FeatureFlag
import dev.diegolopes.featureflag.domain.featureflag.driven.{PublishingFeatureFlag, WritingFeatureFlag}
import zio.{IO, Task, UIO, ZIO, ZLayer}

import java.util.UUID

case class CreateFeatureFlagUseCase(
    writingFeatureFlag: WritingFeatureFlag[UIO],
    publishingFeatureFlag: PublishingFeatureFlag[Task]
) extends UseCase[CreateFeatureFlagInput, CreateFeatureFlagError, Unit] {
  override def execute(input: CreateFeatureFlagInput): IO[CreateFeatureFlagError, Unit] =
    for {
      flag <- createFlag(input)
      _    <- saveFlag(flag)
      _    <- publishFlag(flag)
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

  private def publishFlag(flag: FeatureFlag) =
    publishingFeatureFlag
      .created(flag)
      .mapError(InternalError(_))
      .catchAllDefect(d => ZIO.fail(InternalError(d)))
}

object CreateFeatureFlagUseCase {
  private type ZLayerEnv = WritingFeatureFlag[UIO] & PublishingFeatureFlag[Task]

  val layer: ZLayer[
    ZLayerEnv,
    CreateFeatureFlagError,
    CreateFeatureFlagUseCase
  ] = ZLayer.fromFunction(CreateFeatureFlagUseCase(_, _))
}
