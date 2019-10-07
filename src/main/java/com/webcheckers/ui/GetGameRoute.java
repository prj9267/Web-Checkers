package com.webcheckers.ui;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetGameRoute {
    // Values used in the view-model map for rendering the game view.
    static final String VIEW_NAME = "game.ftl";
    static final String TITLE = "Game!";

    private final TemplateEngine templateEngine;


    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public GetGameRoute(final TemplateEngine templateEngine){
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        // no session for now

        // build the View-Model
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
