package com.typesafe.sbt

import sbt._
import Keys._
import com.typesafe.sbt.SbtGit.GitKeys
import com.typesafe.sbt.git.GitRunner
import GitKeys.{gitBranch, gitRemoteRepo}
import com.typesafe.sbt.site.SitePlugin

// Plugin to make use of github pages.
object GhPagesPlugin extends AutoPlugin {

  override val trigger: PluginTrigger = noTrigger

  override val  requires: Plugins = SitePlugin && GitPlugin

  override lazy val  projectSettings: Seq[Setting[_]] = ghPagesProjectSettings

  object GhPagesKeys {
    lazy val repository = SettingKey[File]("ghpages-repository", "sandbox environment where git project ghpages branch is checked out.")
    lazy val noJekyll = SettingKey[Boolean]("ghpages-no-jekyll", "If this flag is set, ghpages will automatically generate a .nojekyll file to prevent github from running jekyll on pushed sites.")
    lazy val updatedRepository = TaskKey[File]("ghpages-updated-repository", "Updates the local ghpages branch on the sandbox repository.")
    // Note:  These are *only* here in the event someone wants to completely bypass the sbt-site plugin.
    lazy val privateMappings = mappings in synchLocal
    lazy val synchLocal = TaskKey[File]("ghpages-synch-local", "Copies the locally generated site into the local ghpages repository.")
    lazy val cleanSite = TaskKey[Unit]("ghpages-clean-site", "Cleans the staged repository for ghpages branch.")
    lazy val pushSite = TaskKey[Unit]("ghpages-push-site", "Pushes a generated site into the ghpages branch.  Will not clean the branch unless you run clean-site first.")
  }


  object autoImport {
    val GhPages = GhPagesKeys // make it easy for users to use  ghpages keys in .sbt files with GhPages.<key> syntax
  }

  // TODO - Add some sort of locking to the repository so only one thread accesses it at a time...
    import GhPagesKeys._

    def ghPagesProjectSettings: Seq[Setting[_]] = Seq(
      //example: gitRemoteRepo := "git@github.com:jsuereth/scala-arm.git",
      noJekyll := true,
      repository := {
        val buildHash: String =
          Hash.toHex(Hash.apply(sbt.Keys.thisProjectRef.value.build.toASCIIString))
        file(System.getProperty("user.home")) / ".sbt" / "ghpages" / buildHash /  organization.value / name.value
      },
      gitBranch in updatedRepository <<= gitBranch ?? Some("gh-pages"),
      updatedRepository <<= updatedRepo(repository, gitRemoteRepo, gitBranch in updatedRepository),
      pushSite <<= pushSite0,
      privateMappings <<= mappings in SitePlugin.autoImport.makeSite,
      synchLocal <<= synchLocal0,
      cleanSite <<= cleanSite0
    )
    private def updatedRepo(repo: SettingKey[File], remote: SettingKey[String], branch: SettingKey[Option[String]]) =
       (repo, remote, branch, GitKeys.gitRunner, streams) map { (local, uri, branch, git, s) =>
         git.updated(remote = uri, cwd = local, branch = branch, log = s.log);
         local
    }

    private def synchLocal0 = (privateMappings, updatedRepository, noJekyll, GitKeys.gitRunner, streams) map { (mappings, repo, noJekyll, git, s) =>
      // TODO - an sbt.Synch with cache of previous mappings to make this more efficient. */
      val betterMappings = mappings map { case (file, target) => (file, repo / target) }
      // First, remove 'stale' files.
      cleanSiteForRealz(repo, git, s)
      // Now copy files.
      IO.copy(betterMappings)
      if(noJekyll) IO.touch(repo / ".nojekyll")
      repo
    }

    private def cleanSite0 = (updatedRepository, GitKeys.gitRunner, streams) map { (dir, git, s) =>
      cleanSiteForRealz(dir, git, s)
    }
    private def cleanSiteForRealz(dir: File, git: GitRunner, s: TaskStreams): Unit = {
      val toClean = IO.listFiles(dir).filterNot(_.getName == ".git").map(_.getAbsolutePath).toList
      if(!toClean.isEmpty)
        git(("rm" :: "-r" :: "-f" :: "--ignore-unmatch" :: toClean) :_*)(dir, s.log)
      ()
    }

    val commitMessage = sys.env.getOrElse("SBT_GHPAGES_COMMIT_MESSAGE", "updated site")
    private def pushSite0 = (synchLocal, GitKeys.gitRunner, streams) map { (repo, git, s) => git.commitAndPush(commitMessage)(repo, s.log) }


    /** TODO - Create ghpages in the first place if it doesn't exist.
        *$ cd /path/to/fancypants
        *$ git symbolic-ref HEAD refs/heads/gh-pages
        *$ rm .git/index
        *$ git clean -fdx
        *<copy api and documentation>
        *$ echo "My GitHub Page" > index.html
        *$ git add .
        *$ git commit -a -m "First pages commit"
        *$ git push origin gh-pages
     */
}
