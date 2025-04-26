package dev.diegolopes.featureflag.domain.featureflag

import dev.diegolopes.featureflag.domain.featureflag.FeatureFlagError.{
  InvalidId,
  InvalidEmptyName,
  InvalidNameLength,
  InvalidUpperSnakeCaseName
}
import munit.FunSuite

import java.util.UUID

class FeatureFlagTest extends FunSuite {

  test("should create FeatureFlag when id as string and name are valid") {
    val givenId   = UUID.randomUUID().toString
    val givenName = "VALID_NAME"

    val actualOutput = FeatureFlag(givenId, givenName)

    assertEquals(actualOutput.isRight, true)
    assert(actualOutput.exists(flag => flag.name == givenName))
  }

  test("should create FeatureFlag when id as UUID and name are valid") {
    val givenId   = UUID.randomUUID()
    val givenName = "VALID_NAME"

    val actualOutput = FeatureFlag(givenId, givenName)

    assertEquals(actualOutput.isRight, true)
    assert(actualOutput.exists(flag => flag.name == givenName))
  }

  test("should fail when id is invalid UUID") {
    val givenId   = "not-a-uuid"
    val givenName = "VALID_NAME"

    val actualOutput = FeatureFlag(givenId, givenName)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists(errors => errors.contains(InvalidId)))
  }

  test("should fail when name is empty") {
    val givenId   = UUID.randomUUID().toString
    val givenName = ""

    val actualOutput = FeatureFlag(givenId, givenName)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists(errors => errors.exists(_.isInstanceOf[InvalidEmptyName.type])))
  }

  test("should fail when name is too short") {
    val givenId   = UUID.randomUUID().toString
    val givenName = "AB"

    val actualOutput = FeatureFlag(givenId, givenName)

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
    val givenId   = UUID.randomUUID().toString
    val givenName = "A" * 51

    val actualOutput = FeatureFlag(givenId, givenName)

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
    val givenId   = UUID.randomUUID().toString
    val givenName = "invalidName"

    val actualOutput = FeatureFlag(givenId, givenName)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists(errors => errors.exists(_.isInstanceOf[InvalidUpperSnakeCaseName.type])))
  }

  test("should collect multiple errors when both id and name are invalid") {
    val givenId   = "invalid-uuid"
    val givenName = "bad name"

    val actualOutput = FeatureFlag(givenId, givenName)

    assertEquals(actualOutput.isLeft, true)
    assert(actualOutput.left.exists { errors =>
      errors.exists(_.isInstanceOf[InvalidId.type]) &&
      errors.exists(_.isInstanceOf[InvalidUpperSnakeCaseName.type])
    })
  }
}
