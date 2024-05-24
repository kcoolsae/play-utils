/*
 * TabsOrPills.scala
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package views.html.be.ugent.caagt.play.ext

import play.twirl.api.Html
import be.ugent.caagt.play.util.Tab
import views.html.be.ugent.caagt.play.tabs._tabs

import scala.jdk.CollectionConverters._

private[ext]
class TabsOrPills(val css: String) {

  def apply(tablist: java.util.Collection[Tab]): Html = apply(tablist.asScala.toSeq: _*)

  def apply(active: String, tablist: java.util.Collection[Tab]): Html = apply(active, tablist.asScala.toSeq: _*)

  def apply(tabs: Tab*): Html = _tabs(css, Html(""), tabs: _*)

  def apply(active: String, tabs: Tab*) = {
    for (tab <- tabs) {
      tab.setActive(tab.getId == active)
    }
    _tabs(css, Html(""), tabs: _*)
  }

  def withExtra(tabs: Tab*)(block: => Html): Html = _tabs(css, block, tabs: _*)

  def withExtra(tablist: java.util.Collection[Tab])(block: => Html): Html
  = withExtra(tablist.asScala.toSeq: _*)(block)

  def withExtra(active: String, tabs: Tab*)(block: => Html) = {
    for (tab <- tabs) {
      tab.setActive(tab.getId == active)
    }
    _tabs(css, block, tabs: _*)
  }

}

object tabs extends TabsOrPills("nav-tabs") {

}

object pills extends TabsOrPills("nav-pills") {

}


