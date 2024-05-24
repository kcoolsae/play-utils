/*
 * TemplateJavaMagic.scala
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.util

import be.ugent.caagt.play.controllers.BaseAction
import be.ugent.caagt.play.deputies.Deputy
import play.mvc.Http

import java.util.Optional
import scala.collection.convert._

/**
  * Provide some implicit conversions for use in templates.
  */
object TemplateJavaMagic {

    import scala.compat.java8.OptionConverters._
    import scala.language.implicitConversions

    /** Transforms a Play Java `Optional` to a proper Scala `Option`. */
    implicit def javaOptionalToScalaOption[T](x: Optional[T]): Option[T] = x.asScala

    implicit def javaRequestHeader2ScalaRequestHeader(implicit r: Http.RequestHeader): play.api.mvc.RequestHeader = {
        r.asScala()
    }

    implicit def requestToMessages(implicit request: Http.Request): play.i18n.Messages = {
        request.attrs().get(BaseAction.MESSAGES)
    }

    implicit def deputyToMessages(implicit deputy: Deputy): play.i18n.Messages = {
        deputy.getMessages
    }

    implicit def request2Session(implicit request: Http.Request): Http.Session = request.session()

    implicit def request2Flash(implicit request: Http.Request): Http.Flash = request.flash()

}
