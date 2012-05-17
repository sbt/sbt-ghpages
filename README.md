# xsbt-ghpages-plugin #

The github pages plugin for SBT provides support for auto-generating a project website and pushing to github pages.

Please see the [sbt-site-plugin](http://github.com/sbt/sbt-site-plugin) for information on how you can customize the generation of a project website.

## Creating ghpages branch ##

To use this pluign, you must first create a ghpages branch on github.  To do so:

        $ cd /path/to/tmpdirectory
        $ git clone {your project}
        $ cd {your project}
        $ git symbolic-ref HEAD refs/heads/gh-pages
        $ rm .git/index
        $ git clean -fdx
        $ echo "My GitHub Page" > index.html
        $ git add .
        $ git commit -a -m "First pages commit"
        $ git push origin gh-pages

Once this is done, you can begin using the plugin.

## Adding to your project ##

Create a `project/plugins.sbt` file that looks like the following:

    resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

    addSbtPlugin("com.jsuereth" % "sbt-ghpages-plugin" % "0.4.0")


Then in your build.sbt file, simply add:


    site.settings

    ghpages.settings
    
    git.remoteRepo := "git@github.com:{your username}/{your project}.git"


## Pushing your project ##

Simply run the `ghpages-push-site` task to publish your website.

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
This plugin is adapted from the SBT 0.10.x source code for general usage among projects.
