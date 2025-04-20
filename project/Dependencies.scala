import sbt.*

object Dependencies {
  private val munitVersion = "1.0.0"
  private val munit        = "org.scalameta" %% "munit" % munitVersion % Test

  private val munitDeps = Seq(
    munit
  )

  val domainDeps: Seq[ModuleID] = munitDeps
}
