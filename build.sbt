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
crossSbtVersions := Seq("1.2.8")

name := "sbt-ghpages"

organization := "com.github.sbt"

version := "0.6.4-SNAPSHOT"

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.0.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.4.1")
