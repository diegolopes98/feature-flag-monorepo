name := "feature-flag"

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.4"

enablePlugins(ScalafmtPlugin)

lazy val root = (project in file("."))
  .enablePlugins(NativeImagePlugin)
  .settings(Settings.compilerOptions)
  .settings(Settings.nativeOptions)
  .settings(Settings.dependencies)

coverageFailOnMinimum           := true
coverageExcludedPackages        := "<empty>"
coverageMinimumStmtTotal        := 90
coverageMinimumBranchTotal      := 90
coverageMinimumStmtPerPackage   := 90
coverageMinimumBranchPerPackage := 85
coverageMinimumStmtPerFile      := 85
coverageMinimumBranchPerFile    := 80
