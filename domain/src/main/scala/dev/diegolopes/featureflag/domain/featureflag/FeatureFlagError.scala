package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.validation.ValidationError

sealed trait FeatureFlagError extends ValidationError
object FeatureFlagError {
  case object InvalidId extends FeatureFlagError {
    override def message: String = "'id' is invalid"
  }

  case object InvalidEmptyName extends FeatureFlagError {
    override def message: String = "'name' is empty"
  }

  case class InvalidNameLength(min: Int, max: Int) extends FeatureFlagError {
    override def message: String = s"'name' must be between $min and $max characters"
  }

  case object InvalidUpperSnakeCaseName extends FeatureFlagError {
    override def message: String = "'name' must be in upper snake case"
  }
}
