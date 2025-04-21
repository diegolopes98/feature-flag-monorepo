package dev.diegolopes.featureflag.domain

object UpperSnakeCaseString {
  opaque type Type = String

  def from(s: String): Option[Type] =
    if s.matches("^[A-Z0-9]+(_[A-Z0-9]+)*$") then Some(s)
    else None

  extension (u: Type) def value: String = u
}

type UpperSnakeCaseString = UpperSnakeCaseString.Type
