import sbt.*

object Dependencies {
  private val munitVersion = "1.1.0"

  private val munit = "org.scalameta" %% "munit" % munitVersion % Test

  private val munitDeps = Seq(
    munit
  )

  private val zioVersion = "2.1.17"

  private val zio             = "dev.zio" %% "zio"               % zioVersion
  private val zioTest         = "dev.zio" %% "zio-test"          % zioVersion % Test
  private val zioTestSbt      = "dev.zio" %% "zio-test-sbt"      % zioVersion % Test
  private val zioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % zioVersion % Test

  private val zioDeps = Seq(
    zio,
    zioTest,
    zioTestSbt,
    zioTestMagnolia
  )

  val domainDeps: Seq[ModuleID] = munitDeps

  val applicationDeps: Seq[ModuleID] = zioDeps
}
