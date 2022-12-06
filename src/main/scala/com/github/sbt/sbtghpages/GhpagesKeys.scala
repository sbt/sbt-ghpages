package com.github.sbt
package sbtghpages

import sbt._
import Keys._

trait GhpagesKeys {
  lazy val ghpagesCommitOptions = settingKey[Seq[String]]("commit options")
  lazy val ghpagesRepository = settingKey[File]("sandbox environment where git project ghpages branch is checked out.")
  lazy val ghpagesBranch = settingKey[String]("Name of the git branch in which to store ghpages content. Defaults to gh-pages.")
  lazy val ghpagesNoJekyll = settingKey[Boolean]("If this flag is set, ghpages will automatically generate a .nojekyll file to prevent github from running jekyll on pushed sites.")
  lazy val ghpagesUpdatedRepository = taskKey[File]("Updates the local ghpages branch on the sandbox repository.")
  // Note:  These are *only* here in the event someone wants to completely bypass the sbt-site plugin.
  lazy val ghpagesPrivateMappings = ghpagesSynchLocal / mappings
  lazy val ghpagesSynchLocal = taskKey[File]("Copies the locally generated site into the local ghpages repository.")
  lazy val ghpagesCleanSite = taskKey[Unit]("Cleans the staged repository for ghpages branch.")
  lazy val ghpagesPushSite = taskKey[Unit]("Pushes a generated site into the ghpages branch.  Will not clean the branch unless you run clean-site first.")
}
