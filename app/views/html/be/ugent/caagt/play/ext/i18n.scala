/*
 * i18n.scala
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package views.html.be.ugent.caagt.play.ext

import be.ugent.caagt.play.util.I18nData
import play.i18n.Messages
import play.twirl.api.{Html, HtmlFormat}

import scala.jdk.CollectionConverters._
import scala.collection.immutable.ArraySeq._

object i18n {

    /**
      * Convert key and arguments to an internationalized string
      */
    def apply(key: String, args: Object*)(implicit messages: Messages): String = {
        messages.at(key, args: _*)
    }

    def apply(key: String, args: java.util.List[java.lang.Object])(implicit messages: Messages): String = {
        messages.at(key, unsafeWrapArray(args.asScala.toArray): _*)
    }

    def apply(key: String)(implicit messages: Messages): String = messages.at(key)

    /**
      * Convert to internationalized string
      */
    def apply(data: I18nData)(implicit messages: Messages): String = messages.at(data.key(), data.args(): _*)

    /**
      * Convert key and arguments to an internationalized Html object. The corresponding
      * string in the message file must be in html format. Arguments will be html escaped.
      */
    def html(key: String, args: Object*)(implicit messages: Messages): Html = {
        val newArgs = args.map {
            case str: String =>
                HtmlFormat.escape(str)
            case k =>
                k
        }
        Html(messages.at(key, newArgs: _*))
    }

    def html(data: I18nData)(implicit messages: Messages): Html = html(data.key(), unsafeWrapArray(data.args()): _*)

}
