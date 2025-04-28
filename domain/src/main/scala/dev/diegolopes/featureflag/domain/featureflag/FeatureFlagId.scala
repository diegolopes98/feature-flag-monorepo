package dev.diegolopes.featureflag.domain.featureflag

import java.util.UUID

opaque type FeatureFlagId = FeatureFlagId.Type

object FeatureFlagId {
  type Type = UUID

  def from(id: UUID): FeatureFlagId = id

  extension (id: FeatureFlagId) {
    def value: UUID = id
  }
}
