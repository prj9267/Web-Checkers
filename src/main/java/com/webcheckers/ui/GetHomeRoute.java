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
    static final String NUM_PLAYERS_ATTR = "numPlayers";

    private static final String TITLE = "Welcome!";
    private static final String VIEW_NAME = "home.ftl";
    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    // Attribute
    private final GameCenter gameCenter;
    private final PlayerServices playerServices;
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetHomeRoute(final PlayerServices playerServices, final GameCenter gameCenter, final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
        this.playerServices = playerServices;
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
        Map<String, Object> vm = new HashMap<>();
        boolean signedIn = false;

        //If no session is currently active
        if(httpSession.attribute("playerServices") == null) {
            //get object for specific services for the player
            httpSession.attribute("playerServices", playerServices);

            httpSession.attribute("timeoutWatchDog", new SessionTimeoutWatchdog(playerServices));
            //Can be not active for 10 min before it times you out.
            httpSession.maxInactiveInterval(600);

            vm.put(MESSAGE_ATTR, WELCOME_MSG);
        }

        //If user is currently logged in
        String username = httpSession.attribute("username");
        Player player = playerServices.getPlayer(username);
        if(username != null && player != null && username.equals(player.getName())) {
            signedIn = true;
        }

        vm.put("signedIn", signedIn);
        vm.put("username", username);
        vm.put(TITLE_ATTR, TITLE);
        vm.put(MESSAGE_ATTR, Message.info("You Have Successfully Logged In!"));
        vm.put(PLAYERS_ATTR, playerServices.getPlayer(username));
        vm.put(NUM_PLAYERS_ATTR, playerServices.numPlayers());
        vm.put("players", playerServices.getPlayerList());

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
