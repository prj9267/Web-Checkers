package com.webcheckers.ui;

import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetGameRoute implements Route {
    // Values used in the view-model map for rendering the game view.
    static final String VIEW_NAME = "game.ftl";
    static final String TITLE = "Let's play a game...";
    private final TemplateEngine templateEngine;

    //TODO temp player
    private Player player;


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
        this.player = new Player("Name");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response){
        // no session for now

        // build the View-Model
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
