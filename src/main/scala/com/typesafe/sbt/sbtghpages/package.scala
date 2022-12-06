package com.typesafe.sbt

package object sbtghpages {
  @deprecated(message = "will be removed. use com.github.sbt.sbtghpages.GhpagesKeys", since = "0.7.0")
  type GhpagesKeys = com.github.sbt.sbtghpages.GhpagesKeys

  @deprecated(message = "will be removed. use com.github.sbt.sbtghpages.GhpagesPlugin", since = "0.7.0")
  val GhpagesPlugin = com.github.sbt.sbtghpages.GhpagesPlugin
}
