lazy val root = (project in file("."))
  .settings(
    sbtPlugin := true,
    name := "sbt-ghpages",
    organization := "com.typesafe.sbt",
    version := "0.6.0-SNAPSHOT",
    resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven",
    addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3"),
    addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.2.1")
  )
