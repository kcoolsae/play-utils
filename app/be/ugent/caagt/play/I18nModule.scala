/*
 * I18nModule.scala
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * source code adapted from
 * https://github.com/marcospereira/play-i18n-hocon/ © 2016 Marcos Pereira
 * and its fork
 * https://github.com/tino1231/play2-i18n-hocon © 2022 Takeshi Inoue
 * Both licensed under Apache License, Version 2.0, see APACHE_LICENSE in the top level project directory.
 */

package be.ugent.caagt.play

import java.net.URL
import java.util.Properties
import javax.inject.{Inject, Singleton}

import com.typesafe.config.ConfigFactory
import play.api.http.HttpConfiguration
import play.api.i18n._
import play.api.inject.Module
import play.api.{Configuration, Environment}
import play.utils.Resources

import scala.jdk.CollectionConverters._

@Singleton
class HoconMessagesApiProvider @Inject()(
                                          environment: Environment,
                                          config: Configuration,
                                          langs: Langs,
                                          httpConfiguration: HttpConfiguration)
  extends DefaultMessagesApiProvider(environment, config, langs, httpConfiguration) {

  override lazy val get: MessagesApi = {
    new DefaultMessagesApi(
      loadAllMessages,
      langs,
      langCookieName = langCookieName,
      langCookieSecure = langCookieSecure,
      langCookieHttpOnly = langCookieHttpOnly,
      langCookieSameSite = langCookieSameSite,
      httpConfiguration = httpConfiguration,
      langCookieMaxAge = langCookieMaxAge)
  }

  override protected def loadMessages(file: String): Map[String, String] = {
    getResources(file)
      .filterNot(url => Resources.isDirectory(environment.classLoader, url)).reverse
      .map(getMessages)
      .foldLeft(Map.empty[String, String]) {
        _ ++ _
      }
  }

  override protected def loadAllMessages: Map[String, Map[String, String]] = {
    langs.availables.map(_.code).map { lang =>
      (lang, loadMessages(s"messages.$lang.conf"))
    }.toMap ++ Map(
      "default" -> loadMessages("messages.conf"),
      "default.play" -> loadMessages("messages.default"))
  }

  override protected def joinPaths(first: Option[String], second: String): String = first match {
    case Some(parent) => new java.io.File(parent, second).getPath
    case None => second
  }

  private def getResources(file: String): Seq[URL] = {
    environment.classLoader.getResources(joinPaths(messagesPrefix, file)).asIterator().asScala.toSeq
  }

  private def getMessages(url: URL): Map[String, String] = {
    // messages.default is bundled with play and it is a properties file
    val config = if (url.toString.endsWith("messages.default")) {
      ConfigFactory.parseProperties(getProperties(url))
    } else {
      ConfigFactory.parseURL(url)
    }

    // TODO remove asScala
    config.resolve().entrySet().asScala
      .map(e => e.getKey -> String.valueOf(e.getValue.unwrapped()))
      .toMap
  }

  private def getProperties(url: URL): Properties = {
    val properties = new Properties()
    val input = url.openStream()
    try {
      properties.load(input)
    } finally {
      input.close()
    }
    properties
  }
}

/**
 * Module that replaces built-in MessagesApi implementation with a HOCON format based implementation. To enable this
 * module, you have to edit your `application.conf` and add these two lines:
 *
 * {{{
 *   play.modules.disabled += play.api.i18n.I18nModule
 *   play.modules.enabled += be.ugent.caagt.play.HoconI18nModule
 * }}}
 */
class HoconI18nModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = {
    Seq(
      bind[Langs].toProvider[DefaultLangsProvider],
      bind[MessagesApi].toProvider[HoconMessagesApiProvider],
      bind[play.i18n.MessagesApi].toSelf,
      bind[play.i18n.Langs].toSelf)
  }
}
