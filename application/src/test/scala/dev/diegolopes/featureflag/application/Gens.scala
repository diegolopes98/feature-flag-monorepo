package dev.diegolopes.featureflag.application

import dev.diegolopes.featureflag.application.featureflag.create.CreateFeatureFlagInput
import zio.test.Gen

object Gens {
  private val upperOrDigit: Gen[Any, Char] =
    Gen.oneOf(Gen.char('A', 'Z'), Gen.char('0', '9'))

  private val segment: Gen[Any, String] =
    Gen.stringBounded(3, 10)(upperOrDigit)

  val upperSnakeCasedString: Gen[Any, String] =
    for {
      first    <- segment
      segments <- Gen.listOfBounded(0, 4)(segment)
    } yield first + segments.map(seg => "_" + seg).mkString

  val validCreateFeatureFlagInput: Gen[Any, CreateFeatureFlagInput] = for {
    name  <- upperSnakeCasedString
    desc  <- Gen.option(Gen.string)
    value <- Gen.boolean
  } yield CreateFeatureFlagInput(
    name = name,
    description = desc,
    value = value
  )

  def customNameFeatureFlagInput(customNameGen: Gen[Any, String]): Gen[Any, CreateFeatureFlagInput] =
    for {
      name  <- customNameGen
      desc  <- Gen.option(Gen.string)
      value <- Gen.boolean
    } yield CreateFeatureFlagInput(
      name = name,
      description = desc,
      value = value
    )
}
