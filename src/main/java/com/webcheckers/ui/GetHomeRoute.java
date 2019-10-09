package com.webcheckers.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Player;
import spark.*;

import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
    // Values used in the view-model map for rendering the game view.
    static final String TITLE_ATTR = "title";
    static final String MESSAGE_ATTR = "message";
    static final String PLAYERS_ATTR = "players";
    static final String GAME_ID_ATTR = "gameId";

    private static final String TITLE = "Welcome!";
    private static final String VIEW_NAME = "home.ftl";
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");


    // Attribute
    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetHomeRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
        //
        LOG.config("GetHomeRoute is initialized.");
    }



    /**
     * Render the WebCheckers Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetHomeRoute is invoked.");
        //
        final Session httpSession = request.session();

        if(httpSession.attribute("playerServices") == null) {
            //get object for specific services for the player
            final PlayerServices playerService = gameCenter.newPlayerServices();
            httpSession.attribute("playerServices", playerService);

            httpSession.attribute("timeoutWatchDog", new SessionTimeoutWatchdog(playerService));
            //Can be not active for 10 min before it times you out.
            httpSession.maxInactiveInterval(600);
        }

        Map<String, Object> vm = new HashMap<>();
        vm.put(TITLE_ATTR, TITLE);

        // display a user message in the Home page
        vm.put(MESSAGE_ATTR, WELCOME_MSG);

        vm.put(PLAYERS_ATTR, gameCenter.getPlayers().size());

        // render the View
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
