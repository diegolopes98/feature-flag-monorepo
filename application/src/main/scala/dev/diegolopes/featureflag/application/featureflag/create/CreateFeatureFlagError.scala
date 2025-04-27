package dev.diegolopes.featureflag.application.featureflag.create

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError

sealed trait CreateFeatureFlagError
object CreateFeatureFlagError {
  case class ValidationError(errors: List[FeatureFlagError]) extends CreateFeatureFlagError
  case class InternalError(cause: Throwable)                 extends CreateFeatureFlagError
}
