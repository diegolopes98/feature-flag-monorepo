package dev.diegolopes.featureflag.domain.validation

sealed trait Validation[+E] {
  def toEither: Either[List[E], Unit] = this match {
    case Validation.Valid           => Right(())
    case Validation.Invalid(errors) => Left(errors)
  }
}

object Validation {
  case object Valid                      extends Validation[Nothing]
  case class Invalid[E](errors: List[E]) extends Validation[E]

  def combine[E](validations: Validation[E]*): Validation[E] =
    validations.toList.foldLeft(Validation.Valid: Validation[E]) {
      case (Validation.Valid, Validation.Valid)             => Validation.Valid
      case (Validation.Invalid(e1), Validation.Invalid(e2)) => Validation.Invalid(e1 ++ e2)
      case (Validation.Invalid(e), _)                       => Validation.Invalid(e)
      case (_, Validation.Invalid(e))                       => Validation.Invalid(e)
    }
}
