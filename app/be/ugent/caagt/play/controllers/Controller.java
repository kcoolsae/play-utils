/*
 * Controller.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.controllers;

import be.ugent.caagt.play.deputies.Deputy;
import com.typesafe.config.Config;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.function.Supplier;

/**
 * Main super class for controllers that delegate to deputies.
 */
public class Controller<H extends Deputy> extends play.mvc.Controller {

    private final Supplier<H> deputyFactory;

    public Controller(Supplier<H> deputyFactory) {
        this.deputyFactory = deputyFactory;
    }

    @Inject
    FormFactory formFactory;

    @Inject
    Config config;

    @Inject
    MessagesApi messagesApi;

    /**
     * Creates a deputy to handle the given request
     */
    protected H createDeputy(Http.Request request) {
        H deputy = deputyFactory.get();
        deputy.setRequest(request);
        deputy.setConfig(config);
        deputy.setFormFactory(formFactory);
        deputy.setMessagesApi(messagesApi);
        deputy.setParent(this);
        return deputy;
    }

}
