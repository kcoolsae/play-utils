/*
 * Table.scala
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.util

import be.ugent.caagt.play.binders._
import play.mvc.Call
import play.twirl.api.{Html, HtmlFormat}

/**
 * Backend object for paged tables with sort buttons and paging buttons and an enclosing form
 * (e.g., for filtering).
 *
 * Implemented in Scala to allow for methods with multiple parameter lists.
 * Can however be extended by Java classes.
 */
abstract class Table(val psf: PSF) {

  def pager = psf.getPager

  def sorter = psf.getSorter

  def filter = psf.getFilter

  /**
   * Returns a route to a 'list'-method. This method is used
   * in default implementations of {@link # previous}, {@link # next} and
   * {@link # sort}.
   * <p>This implementation returns null and should be overridden if
   * not all three methods above are overridden.
   */
  protected def list(psf: PSF): Call = null

  /** Route to be used when the 'previous' button is clicked. */
  def previous: Call = list(psf.previous)

  /** Route to be used when the 'next' button is clicked */
  def next: Call = list(psf.next)

  /**
   * Route to be used when the 'sort' button is clicked for the given field
   *
   * @param field String identifying the column
   */
  def sort(field: String): Call = list(psf.resort(field))

  /** Route to be used when the 'resize' button is clicked */
  def resize: Call

  /** Route to be used when one of the button forms is clicked (e.g., the search button) */
  def action: Call

  /**
   * Generate html for a sortable column header
   */
  def columnheader(field: String)(html : Html): HtmlFormat.Appendable

  /**
   * An input field for filtering
   */
  def searchfield(name: String, placeHolder: String): HtmlFormat.Appendable

  /*
   * Convenience methods
   * ===================
   */

  /**
   * Generate html for several standard column headers
   */
  def columns(args: (Enum[_], String)*)(implicit messages: play.i18n.Messages) = views.html.be.ugent.caagt.play.tables._columns(this, args: _*)


  /**
   * Several input fields
   */
  def searchfields(args: (Enum[_], String)*)(implicit messages: play.i18n.Messages) = views.html.be.ugent.caagt.play.tables._searchfields(this, args: _*)

}
