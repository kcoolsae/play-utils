/*
 * BaseController.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.controllers;

import be.ugent.caagt.play.deputies.Deputy;
import play.mvc.With;

import java.util.function.Supplier;

/**
 * Base controller for use with templates that need access to configuration and internationalized messages
 */
@With(BaseAction.class)
public class BaseController<D extends Deputy> extends Controller<D> {

    public BaseController(Supplier<D> deputyFactory) {
        super(deputyFactory);
    }

}
