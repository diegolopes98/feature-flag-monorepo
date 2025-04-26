package dev.diegolopes.featureflag.domain.validation

trait ValidationError {
  def message: String
}
