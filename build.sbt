name := """play-scala-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"


lazy val root = (project in file("."))
  .settings(
    fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value),
    nativeImageSettings
  )
  .enablePlugins(PlayScala, NativeImagePlugin)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"


assemblyMergeStrategy in assembly := {
  case manifest if manifest.contains("MANIFEST.MF") =>
    //     We don't need manifest files since sbt-assembly will create
    //     one with the given settings
    MergeStrategy.discard
  case referenceOverrides if referenceOverrides.contains("reference-overrides.conf") =>
    // Keep the content for all reference-overrides.conf files
    MergeStrategy.concat
  case x =>
    // For all the other files, use the default sbt-assembly merge strategy
    //    val oldStrategy = (assemblyMergeStrategy in assembly).value
    //    oldStrategy(x)
    MergeStrategy.first
}

lazy val nativeImageSettings: Seq[Setting[_]] = Seq(
  Compile / mainClass := Some("play.core.server.ProdServerStart"),
  nativeImageVersion := "22.3.1",
  //  nativeImageAgentExtraConfigs := Seq(" -config-write-period-secs=1"),
  nativeImageOptions ++= Seq(
    "--no-fallback",
    s"-H:ReflectionConfigurationFiles=${target.value / "native-image-configs" / "reflect-config.json"}",
    //    s"-H:ConfigurationFileDirectories=${target.value / "native-image-configs"}",
    "-H:+ReportExceptionStackTraces",
    "--verbose"
    //      "--initialize-at-run-time=io.netty.util.internal.logging.Log4JLogger", // runtime 應該沒用到 Log4J
    //    "--initialize-at-build-time=org.slf4j.impl.StaticLoggerBinder",
    //    "--initialize-at-build-time=ch.qos.logback.core.CoreConstants",
    //    "--initialize-at-build-time=ch.qos.logback.core.status.StatusBase",
    //    "--initialize-at-build-time=ch.qos.logback.core.spi.AppenderAttachableImpl",
    //    "--initialize-at-build-time=ch.qos.logback.classic.Logger,org.slf4j.LoggerFactory",
    //    "--initialize-at-build-time=ch.qos.logback.classic.Level",
    //    "--initialize-at-build-time=ch.qos.logback.classic.PatternLayout",
    //    "--initialize-at-build-time=org.slf4j.impl.StaticLoggerBinder",
    //    "--initialize-at-build-time=org.slf4j.MDC"

    //    "--allow-incomplete-classpath",
  )
  //  nativeImageAgentMerge := true,
)
