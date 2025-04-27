package dev.diegolopes.featureflag.application.featureflag.create

import dev.diegolopes.featureflag.application.Gens.{customNameFeatureFlagInput, validCreateFeatureFlagInput}
import dev.diegolopes.featureflag.application.featureflag.create.CreateFeatureFlagError.{InternalError, ValidationError}
import dev.diegolopes.featureflag.application.stubs.WritingFeatureFlagStub
import dev.diegolopes.featureflag.application.stubs.WritingFeatureFlagStub.{defectLayer, successLayer}
import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError.{
  InvalidEmptyName,
  InvalidNameLength,
  InvalidUpperSnakeCaseName
}
import zio.*
import zio.test.*
import zio.test.Assertion.*

object CreateFeatureFlagUseCaseSpec extends zio.test.ZIOSpecDefault {
  override def spec: Spec[TestEnvironment & Scope, Any] =
    suite("CreateFeatureFlagUseCaseSpec")(
      success.provideLayer(successLayer >>> CreateFeatureFlagUseCase.layer),
      failures.provideLayer(defectLayer >>> CreateFeatureFlagUseCase.layer)
    ) @@ TestAspect.silentLogging @@ TestAspect.parallel

  private val success = test("Should successfully create feature flag")(
    check(validCreateFeatureFlagInput) { givenCmd =>
      ZIO
        .serviceWithZIO[CreateFeatureFlagUseCase](_.apply(givenCmd))
        .as(assertCompletes)
    }
  )

  private val failures = suite("Should fail with...")(
    test("ValidationFailure") {
      check(customNameFeatureFlagInput(Gen.string.resize(1).map(_.toLowerCase))) { givenCmd =>
        assertZIO(
          ZIO
            .serviceWithZIO[CreateFeatureFlagUseCase](_.apply(givenCmd))
            .exit
        )(fails(equalTo(ValidationError(List(InvalidEmptyName, InvalidNameLength(3, 50), InvalidUpperSnakeCaseName)))))
      }
    },
    test("InternalError") {
      check(validCreateFeatureFlagInput) { givenCmd =>
        assertZIO(
          ZIO
            .serviceWithZIO[CreateFeatureFlagUseCase](_.apply(givenCmd))
            .exit
        )(
          fails(isSubtype[InternalError](hasField("message", _.cause.getMessage, equalTo("Fake test defect"))))
        )
      }
    }
  )
}
