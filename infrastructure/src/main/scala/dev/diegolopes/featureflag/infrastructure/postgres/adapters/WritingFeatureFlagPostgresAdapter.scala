package dev.diegolopes.featureflag.infrastructure.postgres.adapters

import dev.diegolopes.featureflag.domain.featureflag.{FeatureFlag, FeatureFlagId}
import dev.diegolopes.featureflag.domain.featureflag.driven.WritingFeatureFlag
import dev.diegolopes.featureflag.infrastructure.postgres.rows.FeatureFlagRow
import doobie.Update
import doobie.implicits.*
import doobie.postgres.implicits.*
import io.github.gaelrenoux.tranzactio.doobie.{Connection, tzio}
import zio.{Clock, URIO, ZLayer}
import io.scalaland.chimney.dsl.*

import java.time.Instant

case class WritingFeatureFlagPostgresAdapter() extends WritingFeatureFlag[[T] =>> URIO[Connection & Clock, T]] {
  override def save(featureFlag: FeatureFlag): URIO[Connection, Unit] = {
    def row(now: Instant) =
      featureFlag
        .into[FeatureFlagRow]
        .withFieldConst(_.id, featureFlag.id.value)
        .withFieldConst(_.createdAt, now)
        .withFieldConst(_.updatedAt, now)
        .withFieldConst(_.deactivatedAt, None)
        .transform

    val stmt =
      sql"""|INSERT INTO feature_flag
            |(${FeatureFlagRow.columnsStr})
            |VALUES (${FeatureFlagRow.placeholdersStr})
            |""".stripMargin

    Clock.instant.flatMap { now =>
      tzio(Update[FeatureFlagRow](stmt.toString).toUpdate0(row(now)).run).orDie.unit
    }
  }

  override def update(featureFlag: FeatureFlag): URIO[Connection & Clock, Unit] = {
    def stmt(now: Instant) =
      sql"""|UPDATE feature_flag SET
            | description = ${featureFlag.description}
            | value = ${featureFlag.value}
            | updated_at = $now
            |WHERE id = ${featureFlag.id.value}
            |""".stripMargin

    Clock.instant.flatMap { now =>
      tzio(stmt(now).update.run).orDie.unit
    }
  }

  override def delete(id: FeatureFlagId): URIO[Connection & Clock, Unit] = {
    def stmt(now: Instant) =
      sql"""|UPDATE feature_flag SET
            | active = false
            | deactivated_at = $now
            |WHERE id = ${id.value}
            |""".stripMargin

    Clock.instant.flatMap { now =>
      tzio(stmt(now).update.run).orDie.unit
    }
  }
}

object WritingFeatureFlagPostgresAdapter {
  val layer: ZLayer[Connection & Clock, Nothing, WritingFeatureFlag[[T] =>> URIO[Connection & Clock, T]]] =
    ZLayer.succeed(new WritingFeatureFlagPostgresAdapter())
}
