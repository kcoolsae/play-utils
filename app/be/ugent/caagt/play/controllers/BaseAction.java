/*
 * BaseAction.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2022-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.play.controllers;

import com.typesafe.config.Config;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.typedmap.TypedKey;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Used with @With-notation on {@link BaseController} to provide play configuration to templates
 */
public class BaseAction extends Action.Simple {

    @Inject
    Config configuration;

    @Inject
    MessagesApi messagesApi;

    public static final TypedKey<Config> PLAY_CONFIGURATION = TypedKey.create("PlayConfiguration");

    public static final TypedKey<Messages> MESSAGES = TypedKey.create("Messages");

    @Override
    public CompletionStage<Result> call(Http.Request request) {
        return delegate.call(
                request.addAttr(PLAY_CONFIGURATION, configuration)
                        .addAttr(MESSAGES, messagesApi.preferred(request))
        );
    }

}
