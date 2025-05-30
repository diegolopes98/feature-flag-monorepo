import sbt.Keys.{libraryDependencies, scalacOptions}
import sbtnativeimage.NativeImagePlugin.autoImport.nativeImageOptions

object Settings {
  lazy val nativeOptions              = nativeImageOptions ++= CompilerOptions.nativeOptions
  lazy val compilerOptions            = scalacOptions ++= CompilerOptions.options
  lazy val domainDependencies         = libraryDependencies ++= Dependencies.domainDeps
  lazy val applicationDependencies    = libraryDependencies ++= Dependencies.applicationDeps
  lazy val infrastructureDependencies = libraryDependencies ++= Dependencies.infrastructureDeps
  lazy val platformDependencies       = libraryDependencies ++= Dependencies.platformDeps
  lazy val driverApiDependencies      = libraryDependencies ++= Dependencies.driverApiDeps
  lazy val driverConsumerDependencies = libraryDependencies ++= Dependencies.driverConsumerDeps
}
