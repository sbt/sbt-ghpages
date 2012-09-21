import sbt._
import Keys._
object ghpages extends Build {
  override def projects = Seq(root)
  lazy val root = Project("sbt-ghpages", file(".")) settings(
     resolvers += Resolver.url("scalasbt", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns),
     resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven",
     addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.5.0"),
     addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "0.6.0")
  )
}
