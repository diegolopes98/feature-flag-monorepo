package dev.diegolopes.featureflag.infrastructure.postgres.rows

import java.time.Instant
import java.util.UUID

case class FeatureFlagRow(
    id: UUID,
    name: String,
    description: Option[String],
    value: Boolean,
    active: Boolean,
    createdAt: Instant,
    updatedAt: Instant,
    deactivatedAt: Option[Instant]
)

object FeatureFlagRow extends Row[FeatureFlagRow] {
  override val columnNames: List[String] = findColumnNames
  override val defaultAlias: String      = findDefaultAlias
}
