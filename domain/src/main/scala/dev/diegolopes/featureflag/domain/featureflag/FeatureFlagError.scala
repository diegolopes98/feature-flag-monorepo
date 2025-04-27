package dev.diegolopes.featureflag.domain.featureflag

sealed trait FeatureFlagError
object FeatureFlagError {
  case object InvalidId                            extends FeatureFlagError
  case object InvalidEmptyName                     extends FeatureFlagError
  case class InvalidNameLength(min: Int, max: Int) extends FeatureFlagError
  case object InvalidUpperSnakeCaseName            extends FeatureFlagError
  case class InvalidDescriptionLength(max: Int)    extends FeatureFlagError
}
