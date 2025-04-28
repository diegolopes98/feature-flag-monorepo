ThisBuild / version                      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion                 := "3.6.4"
ThisBuild / organization                 := "dev.diegolopes"
ThisBuild / organizationName             := "Diego Lopes"
ThisBuild / packageDoc / publishArtifact := false

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

coverageFailOnMinimum           := true
coverageExcludedPackages        := "<empty>"
coverageMinimumStmtTotal        := 90
coverageMinimumBranchTotal      := 90
coverageMinimumStmtPerPackage   := 90
coverageMinimumBranchPerPackage := 85
coverageMinimumStmtPerFile      := 85
coverageMinimumBranchPerFile    := 80
