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

libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.13.0"

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.1.0")

addSbtPlugin("com.github.sbt" % "sbt-site" % "1.7.0")

homepage := Some(url("https://github.com/sbt/sbt-ghpages"))

licenses := Seq("BSD-style" -> url("https://raw.githubusercontent.com/sbt/sbt-ghpages/master/LICENSE"))

pomExtra := (
  <developers>
    <developer>
      <id>xuwei-k</id>
      <name>Kenji Yoshida</name>
      <url>https://github.com/xuwei-k</url>
    </developer>
  </developers>
)
