import sbt.Keys.{libraryDependencies, scalacOptions}
import sbtnativeimage.NativeImagePlugin.autoImport.nativeImageOptions

object Settings {
  lazy val dependencies    = libraryDependencies ++= Dependencies.rootDeps
  lazy val compilerOptions = scalacOptions ++= CompilerOptions.options
  lazy val nativeOptions   = nativeImageOptions ++= CompilerOptions.nativeOptions
}
