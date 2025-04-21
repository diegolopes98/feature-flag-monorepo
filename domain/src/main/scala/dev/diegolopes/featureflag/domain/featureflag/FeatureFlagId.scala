package dev.diegolopes.featureflag.domain.featureflag

import java.util.UUID

opaque type FeatureFlagId = UUID

object FeatureFlagId {
  extension (id: FeatureFlagId) {
    def value    = id
    def toString = id.toString()
  }
}
