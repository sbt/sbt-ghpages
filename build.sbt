lazy val root = (project in file("."))
  .settings(
    ScriptedPlugin.scriptedSettings,
    scriptedBufferLog := false,
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    scriptedLaunchOpts ++= sys.process.javaVmArguments.filter(
      a => Seq("-Xmx", "-Xms", "-XX", "-Dsbt.log.noformat").exists(a.startsWith)
    ),
    sbtPlugin := true,
    crossSbtVersions += "1.2.8",
    name := "sbt-ghpages",
    organization := "com.typesafe.sbt",
    version := "0.6.4-SNAPSHOT",
    libraryDependencies ++= Seq(
      Defaults.sbtPluginExtra(
        "com.typesafe.sbt" % "sbt-git" % "0.9.3",
        (sbtBinaryVersion in pluginCrossBuild).value,
        (scalaBinaryVersion in pluginCrossBuild).value
      ),
      Defaults.sbtPluginExtra(
        "com.typesafe.sbt" % "sbt-site" % "1.3.3",
        (sbtBinaryVersion in pluginCrossBuild).value,
        (scalaBinaryVersion in pluginCrossBuild).value
      )
    )
  )
