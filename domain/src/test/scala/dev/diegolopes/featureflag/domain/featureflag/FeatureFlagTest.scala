package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError.{
  InvalidDescriptionLength,
  InvalidEmptyName,
  InvalidNameLength,
  InvalidUpperSnakeCaseName
}
import munit.FunSuite

import java.util.UUID

class FeatureFlagTest extends FunSuite {

  test("should create FeatureFlag when properties are valid") {
    val givenId           = UUID.randomUUID()
    val givenName         = "VALID_NAME"
    val givenDescription  = None
    val givenValue        = true
    val givenActiveStatus = true

    val actualOutput = FeatureFlag(givenId, givenName, givenDescription, givenValue, givenActiveStatus)

    assertEquals(actualOutput.isRight, true)
    assert(actualOutput.exists(flag => flag.id == givenId && flag.name == givenName))
  }

  test("should create FeatureFlag when properties are valid including description") {
    val givenId           = UUID.randomUUID()
    val givenName         = "VALID_NAME"
    val givenDescription  = Some("Testing description")
    val givenValue        = true
    val givenActiveStatus = true

    val actualOutput = FeatureFlag(givenId, givenName, givenDescription, givenValue, givenActiveStatus)

    assertEquals(actualOutput.isRight, true)
    assert(actualOutput.exists(flag => flag.id == givenId && flag.name == givenName))
  }

  test("should fail when name is empty") {
    val givenId           = UUID.randomUUID()
    val givenName         = ""
    val givenDescription  = None
    val givenValue        = true
    val givenActiveStatus = true

    val actualOutput = FeatureFlag(givenId, givenName, givenDescription, givenValue, givenActiveStatus)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists(errors => errors.exists(_.isInstanceOf[InvalidEmptyName.type])))
  }

  test("should fail when name is too short") {
    val givenId           = UUID.randomUUID()
    val givenName         = "AB"
    val givenDescription  = None
    val givenValue        = true
    val givenActiveStatus = true

    val actualOutput = FeatureFlag(givenId, givenName, givenDescription, givenValue, givenActiveStatus)

    assertEquals(actualOutput.isLeft, true)
    assert(
      actualOutput.left.exists(errors =>
        errors.exists {
          case InvalidNameLength(_, _) => true
          case _                       => false
        }
      )
    )
  }

  test("should fail when name is too long") {
    val givenId           = UUID.randomUUID()
    val givenName         = "A" * 51
    val givenDescription  = None
    val givenValue        = true
    val givenActiveStatus = true

    val actualOutput = FeatureFlag(givenId, givenName, givenDescription, givenValue, givenActiveStatus)

    assertEquals(actualOutput.isLeft, true)
    assert(
      actualOutput.left.exists(errors =>
        errors.exists {
          case InvalidNameLength(_, _) => true
          case _                       => false
        }
      )
    )
  }

  test("should fail when name is not in upper snake case format") {
    val givenId           = UUID.randomUUID()
    val givenName         = "invalidName"
    val givenDescription  = None
    val givenValue        = true
    val givenActiveStatus = true

    val actualOutput = FeatureFlag(givenId, givenName, givenDescription, givenValue, givenActiveStatus)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists(errors => errors.exists(_.isInstanceOf[InvalidUpperSnakeCaseName.type])))
  }

  test("Should fail when description is too long") {
    val givenId           = UUID.randomUUID()
    val givenName         = "VALID_NAME"
    val givenDescription  = Some("*" * 513)
    val givenValue        = true
    val givenActiveStatus = true

    val actualOutput = FeatureFlag(givenId, givenName, givenDescription, givenValue, givenActiveStatus)

    assertEquals(actualOutput.isLeft, true)
    assert(
      actualOutput.left.exists(errors =>
        errors.exists {
          case InvalidDescriptionLength(_) => true
          case _                           => false
        }
      )
    )
  }

  test("should collect multiple errors when both id and name are invalid") {
    val givenId           = UUID.randomUUID()
    val givenName         = "er"
    val givenDescription  = None
    val givenValue        = true
    val givenActiveStatus = true

    val actualOutput = FeatureFlag(givenId, givenName, givenDescription, givenValue, givenActiveStatus)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists(errors => errors.length > 1))
    assert(actualOutput.left.exists(errors => errors.contains(InvalidNameLength(3, 50))))
    assert(actualOutput.left.exists(errors => errors.contains(InvalidUpperSnakeCaseName)))
  }
}
