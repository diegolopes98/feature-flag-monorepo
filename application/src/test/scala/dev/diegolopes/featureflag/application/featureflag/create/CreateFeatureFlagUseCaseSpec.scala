package dev.diegolopes.featureflag.application.featureflag.create

import dev.diegolopes.featureflag.application.Gens.{customNameFeatureFlagInput, validCreateFeatureFlagInput}
import dev.diegolopes.featureflag.application.featureflag.create.CreateFeatureFlagError.{InternalError, ValidationError}
import dev.diegolopes.featureflag.application.stubs.{PublishingFeatureFlagStub, WritingFeatureFlagStub}
import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError.{
  InvalidEmptyName,
  InvalidNameLength,
  InvalidUpperSnakeCaseName
}
import zio.*
import zio.test.*
import zio.test.Assertion.*

object CreateFeatureFlagUseCaseSpec extends zio.test.ZIOSpecDefault {
  private val stubsLayer =
    WritingFeatureFlagStub.layer ++ PublishingFeatureFlagStub.layer

  private val layer =
    stubsLayer ++ (stubsLayer >>> CreateFeatureFlagUseCase.layer)

  private val fakeDefect = new RuntimeException("Fake test defect")

  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("CreateFeatureFlagUseCaseSpec")(
      success,
      failures
    ).provideLayer(layer) @@ TestAspect.silentLogging @@ TestAspect.parallel

  private val success = test("Should successfully create feature flag")(
    check(validCreateFeatureFlagInput) { givenCmd =>
      for {
        _ <- ZIO.serviceWithZIO[WritingFeatureFlagStub](_.Save(ZIO.unit))
        _ <- ZIO.serviceWithZIO[PublishingFeatureFlagStub](_.Created(ZIO.unit))
        _ <- ZIO.serviceWithZIO[CreateFeatureFlagUseCase](_.execute(givenCmd))
      } yield assertCompletes
    }
  )

  private val failures = suite("Should fail with...")(
    test("ValidationFailure") {
      check(customNameFeatureFlagInput(Gen.string.resize(1).map(_.toLowerCase))) { givenCmd =>
        val execution = ZIO.serviceWithZIO[CreateFeatureFlagUseCase](_.execute(givenCmd))

        assertZIO(
          execution.exit
        )(fails(equalTo(ValidationError(List(InvalidEmptyName, InvalidNameLength(3, 50), InvalidUpperSnakeCaseName)))))
      }
    },
    test("InternalError when WritingFeatureFlag dies") {
      check(validCreateFeatureFlagInput) { givenCmd =>
        val execution = for {
          _ <- ZIO.serviceWithZIO[WritingFeatureFlagStub](_.Save(ZIO.die(fakeDefect)))
          _ <- ZIO.serviceWithZIO[CreateFeatureFlagUseCase](_.execute(givenCmd))
        } yield ()

        assertZIO(execution.exit)(fails(equalTo(InternalError(fakeDefect))))
      }
    },
    test("InternalError when PublishingFeatureFlag dies") {
      check(validCreateFeatureFlagInput) { givenCmd =>
        val execution = for {
          _ <- ZIO.serviceWithZIO[WritingFeatureFlagStub](_.Save(ZIO.unit))
          _ <- ZIO.serviceWithZIO[PublishingFeatureFlagStub](_.Created(ZIO.die(fakeDefect)))
          _ <- ZIO.serviceWithZIO[CreateFeatureFlagUseCase](_.execute(givenCmd))
        } yield ()

        assertZIO(execution.exit)(fails(equalTo(InternalError(fakeDefect))))
      }
    }
  )
}
