package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError.{
  InvalidDescriptionLength,
  InvalidEmptyName,
  InvalidNameLength,
  InvalidUpperSnakeCaseName
}
import dev.diegolopes.featureflag.domain.validation.Validation
import dev.diegolopes.featureflag.domain.validation.Validation.{Invalid, Valid, combine}

import java.util.UUID

object FeatureFlagValidator {
  private val NAME_MIN_LENGTH = 3
  private val NAME_MAX_LENGTH = 50
  private val DESC_MAX_LENGTH = 512

  def validate(id: UUID, name: String, description: Option[String]): Validation[FeatureFlagError] =
    combine(
      validateId(id),
      validateName(name),
      validateDescription(description)
    )

  private def validateId(id: UUID): Validation[FeatureFlagError] =
    Valid

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

  private def validateDescription(description: Option[String]): Validation[FeatureFlagError] =
    description.map(validateDescriptionLength).getOrElse(Valid)

  private def validateDescriptionLength(description: String): Validation[FeatureFlagError] =
    if (description.length > DESC_MAX_LENGTH) Invalid(List(InvalidDescriptionLength(DESC_MAX_LENGTH)))
    else Valid
}
