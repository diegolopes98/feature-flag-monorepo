package dev.diegolopes.featureflag.domain.featureflag

import java.util.UUID
import scala.util.Try

opaque type FeatureFlagId = FeatureFlagId.Type

object FeatureFlagId {
  type Type = UUID

  def from(id: String): Option[FeatureFlagId] =
    Try(UUID.fromString(id)) match {
      case scala.util.Success(value) => Some(value)
      case scala.util.Failure(_)     => None
    }

  def from(id: UUID): FeatureFlagId = id

  extension (id: FeatureFlagId) {
    def value: FeatureFlagId = id
    def toString: String     = id.toString
  }
}
