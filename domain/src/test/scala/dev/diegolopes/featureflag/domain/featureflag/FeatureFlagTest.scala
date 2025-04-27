package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError.{
  InvalidEmptyName,
  InvalidNameLength,
  InvalidUpperSnakeCaseName
}
import munit.FunSuite

import java.util.UUID

class FeatureFlagTest extends FunSuite {

  test("should create FeatureFlag when properties are valid") {
    val givenId    = UUID.randomUUID()
    val givenName  = "VALID_NAME"
    val givenValue = true

    val actualOutput = FeatureFlag(givenId, givenName, givenValue)

    assertEquals(actualOutput.isRight, true)
    assert(actualOutput.exists(flag => flag.id == givenId && flag.name == givenName))
  }

  test("should fail when name is empty") {
    val givenId    = UUID.randomUUID()
    val givenName  = ""
    val givenValue = true

    val actualOutput = FeatureFlag(givenId, givenName, givenValue)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists(errors => errors.exists(_.isInstanceOf[InvalidEmptyName.type])))
  }

  test("should fail when name is too short") {
    val givenId    = UUID.randomUUID()
    val givenName  = "AB"
    val givenValue = true

    val actualOutput = FeatureFlag(givenId, givenName, givenValue)

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
    val givenId    = UUID.randomUUID()
    val givenName  = "A" * 51
    val givenValue = true

    val actualOutput = FeatureFlag(givenId, givenName, givenValue)

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
    val givenId    = UUID.randomUUID()
    val givenName  = "invalidName"
    val givenValue = true

    val actualOutput = FeatureFlag(givenId, givenName, givenValue)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists(errors => errors.exists(_.isInstanceOf[InvalidUpperSnakeCaseName.type])))
  }

  test("should collect multiple errors when both id and name are invalid") {
    val givenId    = UUID.randomUUID()
    val givenName  = "er"
    val givenValue = true

    val actualOutput = FeatureFlag(givenId, givenName, givenValue)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists(errors => errors.length > 1))
    assert(actualOutput.left.exists(errors => errors.contains(InvalidNameLength(3, 50))))
    assert(actualOutput.left.exists(errors => errors.contains(InvalidUpperSnakeCaseName)))
  }
}
