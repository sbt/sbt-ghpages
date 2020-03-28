enablePlugins(GhpagesPlugin)
enablePlugins(ParadoxSitePlugin)

ghpagesKeepVersions := true
git.remoteRepo := "git://github.com/sbt/sbt-ghpages.git"
ghpagesBranch := "scripted-test"
sourceDirectory in Paradox := baseDirectory.value / "src" / "main"
sourceDirectory in(Paradox, paradoxTheme) := (sourceDirectory in Paradox).value / "_template"
ghpagesRepository := target.value / "ghpages"
paradoxTheme := None
