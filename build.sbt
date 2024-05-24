/*
 * build.sbt
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

name := "Play utilities"

normalizedName := "play-utils"

description := "Various utilities for the Play framework"

organization := "be.ugent.caagt"

version := "1.1"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .disablePlugins(PlayFilters, PlayLogback, PlayAkkaHttpServer)

scalaVersion := "2.13.12"

Compile / doc / logLevel := Level.Error
Compile / packageDoc / publishArtifact := false
Test / publishArtifact := false

Global / lintUnusedKeysOnLoad := false // avoid warning messages on unused keys
