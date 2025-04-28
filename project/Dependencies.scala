import sbt.*

object Dependencies {
  private val munitVersion = "1.1.0"
  private val munit        = "org.scalameta" %% "munit" % munitVersion % Test

  private val munitDeps = Seq(
    munit
  )

  private val zioVersion      = "2.1.17"
  private val zio             = "dev.zio" %% "zio"               % zioVersion
  private val zioTest         = "dev.zio" %% "zio-test"          % zioVersion % Test
  private val zioTestSbt      = "dev.zio" %% "zio-test-sbt"      % zioVersion % Test
  private val zioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % zioVersion % Test

  private val zioKafkaVersion = "2.12.0"
  private val zioKafka        = "dev.zio" %% "zio-kafka" % zioKafkaVersion

  private val zioJsonVersion = "0.7.42"
  private val zioJson        = "dev.zio" %% "zio-json" % zioJsonVersion

  private val zioDeps = Seq(
    zio,
    zioTest,
    zioTestSbt,
    zioTestMagnolia
  )

  private val zioKafkaDeps = Seq(
    zioKafka,
    zioJson
  )

  private val doobieVersion  = "1.0.0-RC8"
  private val doobieCore     = "org.tpolecat" %% "doobie-core"     % doobieVersion
  private val doobieHikari   = "org.tpolecat" %% "doobie-hikari"   % doobieVersion
  private val doobiePostgres = "org.tpolecat" %% "doobie-postgres" % doobieVersion

  private val tranzactIOVersion = "5.3.0"
  private val tranzactIO        = "io.github.gaelrenoux" %% "tranzactio-doobie" % tranzactIOVersion

  private val doobieCoreDeps = Seq(
    doobieCore,
    doobiePostgres,
    tranzactIO
  )

  private val doobieConnectionDeps = Seq(
    doobieHikari
  )

  private val chimneyVersion = "1.7.3"
  private val chimney        = "io.scalaland" %% "chimney" % "1.7.3"

  private val chimneyDeps = Seq(
    chimney
  )

  val domainDeps: Seq[ModuleID] = munitDeps

  val applicationDeps: Seq[ModuleID] = zioDeps

  val infrastructureDeps: Seq[ModuleID] = zioDeps ++ zioKafkaDeps ++ doobieCoreDeps ++ chimneyDeps
}
