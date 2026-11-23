libraryDependencies += "com.github.xuwei-k" %% "scala-version-from-sbt-version" % "0.2.0"
libraryDependencies += "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value
addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.12.1")
