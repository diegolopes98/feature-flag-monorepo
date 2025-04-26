package dev.diegolopes.featureflag.domain.featureflag

import java.util.UUID
import scala.util.Try

opaque type FeatureFlagId = FeatureFlagId.Type

object FeatureFlagId {
  type Type = UUID

  def from(id: String): Option[FeatureFlagId] =
    Try(UUID.fromString(id)) match {
      case scala.util.Success(value) => Some(value.asInstanceOf[FeatureFlagId])
      case scala.util.Failure(_)     => None
    }

  def from(id: UUID): Type = id

  extension (id: Type) {
    def value: Type      = id
    def toString: String = id.toString
  }
}
