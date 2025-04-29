object CompilerOptions {
  val options = Seq(
    "-no-indent",
    "-encoding",
    "utf-8",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Wunused:imports",
    "-Wunused:privates",
    "-Wunused:locals",
    "-Wunused:params",
    "-Wunused:implicits",
    "-Wvalue-discard"
  )

  val nativeOptions = Seq(
    "--no-fallback",
    "--initialize-at-run-time=zio.Runtime$,zio.Unsafe$,zio.internal.Platform$",
    "-H:+ReportExceptionStackTraces",
    "--install-exit-handlers",
    "--enable-url-protocols=http,https"
  )
}
