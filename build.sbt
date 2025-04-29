ThisBuild / version                      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion                 := "3.6.4"
ThisBuild / organization                 := "dev.diegolopes"
ThisBuild / organizationName             := "Diego Lopes"
ThisBuild / packageDoc / publishArtifact := false

ThisBuild / coverageFailOnMinimum           := true
ThisBuild / coverageMinimumStmtTotal        := 90
ThisBuild / coverageMinimumBranchTotal      := 90
ThisBuild / coverageMinimumStmtPerPackage   := 90
ThisBuild / coverageMinimumBranchPerPackage := 85
ThisBuild / coverageMinimumStmtPerFile      := 85
ThisBuild / coverageMinimumBranchPerFile    := 80

Global / excludeLintKeys ++= Set(
  nativeImageJvm,
  nativeImageVersion
)

enablePlugins(ScalafmtPlugin)

lazy val domain = (project in file("domain"))
  .settings(name := "domain")
  .settings(Settings.compilerOptions)
  .settings(Settings.domainDependencies)

lazy val application = (project in file("application"))
  .dependsOn(domain)
  .settings(name := "application")
  .settings(Settings.compilerOptions)
  .settings(Settings.applicationDependencies)

lazy val infrastructure = (project in file("infrastructure"))
  .dependsOn(domain, application)
  .settings(name := "infrastructure")
  .settings(Settings.compilerOptions)
  .settings(Settings.infrastructureDependencies)

lazy val controlPlaneApi = (project in file("driver/control-plane-api"))
  .dependsOn(domain, application, infrastructure)
  .enablePlugins(NativeImagePlugin)
  .settings(name := "control-plane-api", nativeImageVersion := "21.0.2", nativeImageJvm := "graalvm-java21")
  .settings(Settings.compilerOptions)
  .settings(Settings.nativeOptions)
  .settings(Settings.driverApiDependencies)

lazy val dataPlaneApi = (project in file("driver/data-plane-api"))
  .dependsOn(domain, application, infrastructure)
  .enablePlugins(NativeImagePlugin)
  .settings(name := "data-plane-api", nativeImageVersion := "21.0.2", nativeImageJvm := "graalvm-java21")
  .settings(Settings.compilerOptions)
  .settings(Settings.nativeOptions)
  .settings(Settings.driverApiDependencies)

lazy val hydrationConsumer = (project in file("driver/hydration-consumer"))
  .dependsOn(domain, application, infrastructure)
  .enablePlugins(NativeImagePlugin)
  .settings(name := "hydration-consumer", nativeImageVersion := "21.0.2", nativeImageJvm := "graalvm-java21")
  .settings(Settings.compilerOptions)
  .settings(Settings.nativeOptions)
  .settings(Settings.driverConsumerDependencies)
