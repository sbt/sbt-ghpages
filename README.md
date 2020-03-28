# sbt-ghpages #

[![Build Status](https://travis-ci.org/sbt/sbt-ghpages.svg?branch=master)](https://travis-ci.org/sbt/sbt-ghpages)

The GitHub Pages plugin for sbt makes it simple to publish a project website to
[GitHub Pages](https://pages.github.com/).

First, you need a site to publish, or you may wish to start simply with your
project's Scaladoc API documentation. The [sbt-site plugin] has you covered: it
can help to manage several popular static site generation tools automatically
from sbt.

Start by setting up `sbt-site`, and once you have it locally generating a site
and/or Scaladoc to your liking, `sbt-ghpages` will integrate to publish it on
the web with GitHub Pages where it will be served at
`http://{your username}.github.io/{your project}/`.

[sbt-site plugin]: http://github.com/sbt/sbt-site


## Adding the plugin to your project ##

*Note, sbt-ghpages now requires the use of sbt version 0.13.5 or greater.*

Create a `project/ghpages.sbt` file that looks like the following:

```scala
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.3")
```

Then in your `build.sbt` file, simply enable the GhpagesPlugin via an
`enablePlugins` statement for your project, and specify the location of
your github repository (for more information on enabling and disabling sbt plugins,
see the [sbt plugin documentation](http://www.scala-sbt.org/1.x/docs/Using-Plugins.html#Enabling+and+disabling+auto+plugins)):

```scala
enablePlugins(GhpagesPlugin)

git.remoteRepo := "git@github.com:{your username}/{your project}.git"
```
### Settings

sbt-ghpages provides the following optional setting keys for use in your `build.sbt` file:

- `ghpagesRepository` - Location of the sandbox repository to be used to check out the gh-pages branch.
- `ghpagesNoJekyll` - If set to true will cause a .nojekyll file to be generated, to prevent GitHub from running Jekyll on pushed sites.
- `ghpagesBranch` - Name of the branch in which to store static files. Defaults to gh-pages.


## Initializing the gh-pages branch ##

GitHub Pages works by processing the contents of a special branch in your
project repository named `gh-pages` and then serving your static files.

Before using `sbt-ghpages`, you must [create the `gh-pages` branch in your
repository][create branch] and push the branch to GitHub. The quick steps are:

    # Using a fresh, temporary clone is safest for this procedure
    $ pushd /tmp
    $ git clone git@github.com:youruser/yourproject.git
    $ cd yourproject

    # Create branch with no history or content
    $ git checkout --orphan gh-pages
    $ git rm -rf .

    # Establish the branch existence
    $ git commit --allow-empty -m "Initialize gh-pages branch"
    $ git push origin gh-pages

    # Return to original working copy clone, we're finished with the /tmp one
    $ popd
    $ rm -rf /tmp/yourproject

Now that this is done, you can begin using the plugin with sbt.

[create branch]: https://help.github.com/articles/creating-project-pages-using-the-command-line/


## Publishing your site ##

Simply run the `ghpagesPushSite` task to publish your website.

### How it works

Behind the scenes, `sbt-ghpages` will create a new "sandbox" clone of your Git
repository, with its location determined by the `ghpagesRepository` setting key
(by default set to a directory under `~/.sbt/ghpages`). Whenever you run `sbt ghpagesPushSite`
it will copy your site content into that sandbox repository, commit it to the
`gh-pages` branch, and push the branch to GitHub.

The sandbox repo approach spares you from doing the `gh-pages` checkout/commit
dance yourself each time you update your site content, while avoiding any
mistakes with dirty or untracked files in your normal working copy clone.


## Publishing Scaladoc

A common use for `sbt-ghpages` is to automate the publishing of Scaladoc. If you wish to
use it for this, first ask `sbt-site` to generate your Scaladoc by adding an `enablePlugins` directive
for the `SiteScaladocPlugin` (included in sbt-site) to your `build.sbt` see the
[sbt-site documentation](https://www.scala-sbt.org/sbt-site/api-documentation.html) for more information:

```scala
enablePlugins(SiteScaladocPlugin)
```

After using `ghpagesPushSite` you should find your Scaladoc at:

`http://{your username}.github.io/{your project}/latest/api`

If you aren't publishing any other content to the root of your project site, it
is recommended that you add a redirect to provide a better experience for users
visiting it. You can do this by creating a page in `src/site/index.html` that
automatically redirects to either the above link, or even better, to a good
starting point in your documentation. Otherwise, people visiting `http://{your
username}.github.io/{your project}` will just get a 404.

Here's an example `src/site/index.html` you can use as a starting point:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Project Documentation</title>
    <script language="JavaScript">
    <!--
    function doRedirect()
    {
        window.location.replace("latest/api");
    }

    doRedirect();
    //-->
    </script>
</head>
<body>
<a href="latest/api">Go to the project documentation
</a>
</body>
</html>
```

## Protecting Existing Files
The default behaviour of sbt-ghpages is to remove all existing files in the Github Pages repository
prior to publishing current pages. sbt-ghpages supports customisation of this behaviour via the provided
`includeFilter in ghpagesCleanSite` and/or `excludeFilter in ghpagesCleanSite` setting keys.

sbt-ghpages will only delete files which are matched by the FileFilter specified by the
`includeFilter in ghpagesCleanSite` setting key AND are not matched by the FileFilter specified by the
`excludeFilter in ghpagesCleanSite` key.

For example, to prevent sbt-ghpages from deleting the "CNAME" file located at the root of your site, and any file
named "versions.html", add the following to your build.sbt:

## Maintaining Old Versions
By setting just `ghpagesKeepVersions` flag, you can maintain history of all your old and current versions of site/documents.
Behind the scenes, this flag plays two important roles:
1. Set's the `includeFilter in ghpagesCleanSite` to include all the files from current versions. 
    (That means, if you publish same version again, old content from that version will be replaced by new content)
2. While publishing site to gh-pages, new directory with current version's name is created and all the site contents will be
    copied to that directory
    
Avoid setting `includeFilter in ghpagesCleanSite` explicitly to protect old files as this is set by plugin to protect files when `ghpagesKeepVersions := true`

```scala
excludeFilter in ghpagesCleanSite :=
  new FileFilter{
    def accept(f: File) = (ghpagesRepository.value / "CNAME").getCanonicalPath == f.getCanonicalPath
  } || "versions.html"
```

For more information on creating more complex filters, please refer to the [sbt FileFilter documentation](http://www.scala-sbt.org/1.x/docs/Paths.html#File+Filters).


## LICENSE ##

Copyright (c) 2008, 2009, 2010, 2011 Josh Suereth, Steven Blundy, Josh Cough, Mark Harrah, Stuart Roebuck, Tony Sloane, Vesa Vilhonen, Jason Zaugg
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. The name of the author may not be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


Note:
This plugin is adapted from the sbt 0.10.x source code for general usage among projects.
