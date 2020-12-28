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

// Don't update to sbt 1.3.x or later
// https://github.com/sbt/sbt/issues/5049
crossSbtVersions := Seq("1.2.8", "0.13.18")

name := "sbt-ghpages"

organization := "com.typesafe.sbt"

version := "0.6.4-SNAPSHOT"

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.4.1")
