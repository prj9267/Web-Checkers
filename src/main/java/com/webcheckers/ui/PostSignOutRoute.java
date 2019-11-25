package com.webcheckers.ui;

import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class PostSignOutRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private PlayerServices playerServices;
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSignOutRoute(PlayerServices playerServices, TemplateEngine templateEngine){
        // validation
        Objects.requireNonNull(playerServices, "playerServices must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.playerServices = playerServices;

        LOG.config("PostSignOutRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page after signing out.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page after being signed out.
     */
    @Override
    public Object handle(Request request, Response response){
        LOG.finer("PostSignOutRoute is invoked.");
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();
        final Session httpSession = request.session();
        String username = httpSession.attribute("currentPlayer");
        Player player = playerServices.getPlayer(username);

        if(httpSession.attribute("playerServices") != null){
            // remove the player from game center
            if (player.getStatus() == Player.Status.challenged ||
                    player.getStatus() == Player.Status.ingame) {
                player.setSignOut(true);
                response.redirect(WebServer.RESIGN_URL);
                halt();
                return null;
            }
            playerServices.removePlayer(player);

            // aka as clearing the session
            httpSession.removeAttribute("playerServices");
            httpSession.removeAttribute("currentPlayer");
        }
        response.redirect(WebServer.HOME_URL);
        halt();
        return null;
    }
}
