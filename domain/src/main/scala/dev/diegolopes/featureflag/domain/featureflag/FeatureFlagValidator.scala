package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError.{
  InvalidEmptyName,
  InvalidId,
  InvalidNameLength,
  InvalidUpperSnakeCaseName
}
import dev.diegolopes.featureflag.domain.validation.Validation
import dev.diegolopes.featureflag.domain.validation.Validation.{Invalid, Valid, combine}

import java.util.UUID

object FeatureFlagValidator {
  private val NAME_MIN_LENGTH = 3
  private val NAME_MAX_LENGTH = 50

  def validate(id: UUID, name: String): Validation[FeatureFlagError] =
    combine(
      validateId(id),
      validateName(name)
    )

  def validate(id: String, name: String): Validation[FeatureFlagError] =
    combine(
      validateId(id),
      validateName(name)
    )

  private def validateId(id: UUID): Validation[FeatureFlagError] =
    Valid

  private def validateId(id: String): Validation[FeatureFlagError] =
    FeatureFlagId.from(id) match {
      case Some(_) => Valid
      case None    => Invalid(List(InvalidId))
    }

  private def validateName(name: String): Validation[FeatureFlagError] =
    combine(
      validateNameNotEmpty(name),
      validateNameLength(name),
      validateNameIsUpperSnakeCase(name)
    )

  private def validateNameNotEmpty(name: String): Validation[FeatureFlagError] =
    if (name.trim.isEmpty) Invalid(List(InvalidEmptyName))
    else Valid

  private def validateNameLength(name: String): Validation[FeatureFlagError] =
    if (name.length < NAME_MIN_LENGTH || name.length > NAME_MAX_LENGTH)
      Invalid(List(InvalidNameLength(NAME_MIN_LENGTH, NAME_MAX_LENGTH)))
    else Valid

  private def validateNameIsUpperSnakeCase(name: String): Validation[FeatureFlagError] =
    if (!name.matches("^[A-Z0-9]+(_[A-Z0-9]+)*$")) Invalid(List(InvalidUpperSnakeCaseName))
    else Valid
}
