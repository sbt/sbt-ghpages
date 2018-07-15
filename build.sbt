lazy val root = (project in file("."))
  .settings(
    ScriptedPlugin.scriptedSettings,
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    scriptedLaunchOpts ++= sys.process.javaVmArguments.filter(
      a => Seq("-Xmx", "-Xms", "-XX", "-Dsbt.log.noformat").exists(a.startsWith)
    ),
    sbtPlugin := true,
    name := "sbt-ghpages",
    organization := "com.typesafe.sbt",
    version := "0.6.3-SNAPSHOT",
    libraryDependencies ++= Seq(
      Defaults.sbtPluginExtra(
        "com.typesafe.sbt" % "sbt-git" % "0.9.3",
        (sbtBinaryVersion in pluginCrossBuild).value,
        (scalaBinaryVersion in pluginCrossBuild).value
      ),
      Defaults.sbtPluginExtra(
        "com.typesafe.sbt" % "sbt-site" % "1.3.0",
        (sbtBinaryVersion in pluginCrossBuild).value,
        (scalaBinaryVersion in pluginCrossBuild).value
      )
    )
  )
