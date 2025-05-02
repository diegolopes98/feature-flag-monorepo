package dev.diegolopes.featureflag.infrastructure.postgres.adapters

import dev.diegolopes.featureflag.domain.featureflag.{FeatureFlag, FeatureFlagId}
import dev.diegolopes.featureflag.domain.featureflag.driven.FindingFeatureFlag
import dev.diegolopes.featureflag.infrastructure.postgres.rows.FeatureFlagRow
import doobie.implicits.*
import doobie.postgres.implicits.*
import doobie.util.transactor
import io.github.gaelrenoux.tranzactio.doobie.{Connection, tzio}
import zio.{URIO, ZLayer}
import io.scalaland.chimney.dsl.*

case class FindingFeatureFlagPostgresAdapter() extends FindingFeatureFlag[[T] =>> URIO[Connection, T]] {

  override def byId(id: FeatureFlagId): URIO[Connection, Option[FeatureFlag]] = {
    val stmt =
      sql"""|SELECT ${FeatureFlagRow.columnsAsLast()}
            |FROM feature_flag
            |WHERE id = ${id.value}
            |""".stripMargin

    tzio(stmt.query[FeatureFlagRow].option).map(_.map(mapFeatureFlag)).orDie
  }

  private def mapFeatureFlag(ffr: FeatureFlagRow) =
    ffr
      .into[FeatureFlag]
      .withFieldConst(_.id, FeatureFlagId.from(ffr.id))
      .transform
}

object FindingFeatureFlagPostgresAdapter {
  val layer: ZLayer[Connection, Nothing, FindingFeatureFlag[[T] =>> URIO[Connection, T]]] =
    ZLayer.succeed(FindingFeatureFlagPostgresAdapter())
}
