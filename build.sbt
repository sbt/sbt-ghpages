enablePlugins(SbtPlugin)

scriptedBufferLog := false

scriptedLaunchOpts += ("-Dplugin.version=" + version.value)

scriptedLaunchOpts ++= {
  val javaVmArgs = {
    import scala.collection.JavaConverters._
    java.lang.management.ManagementFactory.getRuntimeMXBean.getInputArguments.asScala.toList
  }
  javaVmArgs.filter(
    a => Seq("-Xmx", "-Xms", "-XX", "-Dsbt.log.noformat").exists(a.startsWith)
  )
}

name := "sbt-ghpages"

organization := "com.github.sbt"

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.1.0")

addSbtPlugin("com.github.sbt" % "sbt-site" % "1.8.0")

crossScalaVersions += "3.8.4"

scalacOptions ++= {
  scalaBinaryVersion.value match {
    case "2.12" => Seq("-deprecation", "-Xsource:3", "-release", "8")
    case _      => Nil
  }
}

(pluginCrossBuild / sbtVersion) := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.9.7"
    case _      => "2.0.6"
  }
}

scriptedSbt := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.12.15"
    case _      => (pluginCrossBuild / sbtVersion).value
  }
}

description := "GitHub Pages support for sbt"

homepage := Some(url("https://github.com/sbt/sbt-ghpages"))

licenses := Seq("BSD-style" -> url("https://raw.githubusercontent.com/sbt/sbt-ghpages/master/LICENSE"))

developers := List(
  Developer(
    id = "xuwei-k",
    name = "Kenji Yoshida",
    email = "@xuwei-k",
    url = url("https://github.com/xuwei-k")
  )
)
