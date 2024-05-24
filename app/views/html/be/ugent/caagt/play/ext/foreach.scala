/*
 * foreach.scala
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package views.html.be.ugent.caagt.play.ext

import play.twirl.api.Html

import scala.jdk.CollectionConverters._

/**
  * Provides a more readable way to iterate over a Java Iterable
  */
object foreach {

  // cannot be programmed as a template, see https://groups.google.com/forum/#!topic/play-framework/hWfgx7IwuCw

  /**
    * Iterate over the given Java iterable
    */
  def apply[T](iterable: java.lang.Iterable[T])(block: T => Html): Html = {
    apply(iterable, null)(block)
  }

  /**
    * Iterate over the given Java iterable, optionally enclosing every iteration result within tags
    */
  def apply[T](iterable: java.lang.Iterable[T], tag: String, args: (Symbol, Any)*)(block: T => Html): Html = {
    val scalaIterable = iterable.asScala
    new Html((
      if (tag == null) {
        scalaIterable.map(block)
      } else {
        val st: String = makeString(args)
        scalaIterable.flatMap { el: T => List[Html](Html(s"<$tag $st>"), block(el), Html(s"</$tag>")) }
      }
      ).to(Seq))
  }

  /**
    * Iterate over the given Java map,
    */
  def apply[K,V](map: java.util.Map[K,V])(block: (K,V) => Html):Html = {
      apply(map, null)(block)
  }

  /**
    * Iterate over the given Java map, optionally enclosing every iteration result within tags
    */
  def  apply[K,V](map: java.util.Map[K,V], tag: String, args: (Symbol, Any)*)(block: (K,V) => Html):Html = {
      apply(map.entrySet(), tag, args:_*)(e => block(e.getKey,e.getValue))
  }

  /**
    * Same as apply, but allows an alternative if the list is empty
    */
  def orElse[T](iterable: java.lang.Iterable[T])(block: T => Html)(elseBlock: => Html): Html = {
    orElse(iterable, null)(block)(elseBlock)
  }

  /**
    * Same as apply, but allows an alternative if the list is empty
    */
  def orElse[T](iterable: java.lang.Iterable[T], tag: String, args: (Symbol, Any)*)(block: T => Html)(elseBlock: => Html): Html = {
    if (iterable != null && iterable.iterator().hasNext) {
      apply(iterable, tag, args: _*)(block)
    } else {
      elseBlock
    }
  }

  /**
    * Same as apply, but provides an index during iteration
    */
  def withIndex[T](iterable: java.lang.Iterable[T])(block: (T, Int) => Html): Html = {
    withIndex(iterable, null)(block)
  }

  /**
    * Same as apply, but provides an index during iteration
    */
  def withIndex[T](iterable: java.lang.Iterable[T], tag: String, args: (Symbol, Any)*)(block: (T, Int) => Html): Html = {
    val scalaIterable = iterable.asScala.zipWithIndex
    new Html((
      if (tag == null) {
        scalaIterable.map(t => block(t._1, t._2))
      } else {
        val st: String = makeString(args)
        scalaIterable.flatMap { t: (T, Int) => List[Html](Html(s"<$tag $st>"), block(t._1, t._2), Html(s"</$tag>")) }
      }
      ).to(Seq))
  }

  /**
    * Combines withIndex with OrElse
    */
  def withIndexOrElse[T](iterable: java.lang.Iterable[T])(block: (T, Int) => Html)(elseBlock: => Html): Html = {
    withIndexOrElse(iterable, null)(block)(elseBlock)
  }

  /**
    * Combines withIndex with OrElse
    */
  def withIndexOrElse[T](iterable: java.lang.Iterable[T], tag: String, args: (Symbol, Any)*)(block: (T, Int) => Html)(elseBlock: => Html): Html = {
    if (iterable != null && iterable.iterator().hasNext) {
      withIndex(iterable, tag, args: _*)(block)
    } else {
      elseBlock
    }
  }

  private def inner(args: Seq[(Symbol, Any)]) = args.filter(arg => !arg._1.name.startsWith("_"))

  private def makeString(args: Seq[(Symbol, Any)]): String = {
    inner(args)
    var st = ""
    for ((s, t) <- args) {
      st += s.name + "=\"" + t.toString + "\" "
    }
    st
  }

  /**
    * Alternative notation for the very common case where the enclosing tag is 'tr'
    */
  object tr {
    def apply[T](iterable: java.lang.Iterable[T], args: (Symbol, Any)*)(block: T => Html): Html = foreach.apply(iterable, "tr", args: _*)(block)

    def apply[K,V](map: java.util.Map[K,V],  args: (Symbol, Any)*)(block: (K,V) => Html): Html  = foreach.apply(map, "tr", args:_*)(block)

    def orElse[T](iterable: java.lang.Iterable[T], args: (Symbol, Any)*)(block: T => Html)(elseBlock: => Html): Html = foreach.orElse(iterable, "tr", args: _*)(block)(elseBlock)

    def withIndex[T](iterable: java.lang.Iterable[T], args: (Symbol, Any)*)(block: (T, Int) => Html): Html = foreach.withIndex(iterable, "tr", args: _*)(block)

    def withIndexOrElse[T](iterable: java.lang.Iterable[T], args: (Symbol, Any)*)(block: (T, Int) => Html)(elseBlock: => Html): Html = foreach.withIndexOrElse(iterable, "tr", args: _*)(block)(elseBlock)
  }

  /**
    * Alternative notation for the common case where the enclosing tag is 'li'
    */
  object li {
    def apply[T](iterable: java.lang.Iterable[T], args: (Symbol, Any)*)(block: T => Html): Html = foreach.apply(iterable, "li", args: _*)(block)

    def apply[K,V](map: java.util.Map[K,V],  args: (Symbol, Any)*)(block: (K,V) => Html): Html  = foreach.apply(map, "li", args:_*)(block)

    def orElse[T](iterable: java.lang.Iterable[T], args: (Symbol, Any)*)(block: T => Html)(elseBlock: => Html): Html = foreach.orElse(iterable, "li", args: _*)(block)(elseBlock)

    def withIndex[T](iterable: java.lang.Iterable[T], args: (Symbol, Any)*)(block: (T, Int) => Html): Html = foreach.withIndex(iterable, "li", args: _*)(block)

    def withIndexOrElse[T](iterable: java.lang.Iterable[T], args: (Symbol, Any)*)(block: (T, Int) => Html)(elseBlock: => Html): Html = foreach.withIndexOrElse(iterable, "li", args: _*)(block)(elseBlock)
  }


}
