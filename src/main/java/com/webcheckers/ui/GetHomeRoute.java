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

import static spark.Spark.halt;

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
    static final String TITLE = "Welcome!";
    static final String CURRENT_PLAYER_ATTR = "currentPlayer";

    public static final String VIEW_NAME = "home.ftl";
    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
    public static final Message SIGNIN_MSG = Message.info("You Have Successfully Sign In!");

    public static final String CURRENT_USERNAME_KEY = "currentPlayer";
    public static final String PLAYERSERVICES_KEY = "playerServices";
    public static final String TIMEOUT_SESSION_KEY = "timeoutWatchDog";

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

        vm.put(TITLE_ATTR, TITLE);

        //If no session is currently active
        if(httpSession.attribute(PLAYERSERVICES_KEY) == null) {
            //get object for specific services for the player
            httpSession.attribute(PLAYERSERVICES_KEY, playerServices);

            httpSession.attribute(TIMEOUT_SESSION_KEY, new SessionTimeoutWatchdog(playerServices));

            //Can be not active for 10 min before it times you out.
            httpSession.maxInactiveInterval(600);
        }

        //If user is currently logged in
        ArrayList<Player> players = playerServices.getPlayerList();
        if(httpSession.attribute(CURRENT_USERNAME_KEY) != null){
            Player player = playerServices.getPlayer(httpSession.attribute(CURRENT_USERNAME_KEY));
            // redirect the challenged player to the game
            if (player.getStatus() == Player.Status.challenged ||
                player.getStatus() == Player.Status.ingame){
                response.redirect(WebServer.GAME_URL);
                halt();
                return null;
            }

            vm.put(MESSAGE_ATTR, SIGNIN_MSG);
            vm.put(CURRENT_USERNAME_KEY, httpSession.attribute(CURRENT_USERNAME_KEY));
            players.remove(player);
            vm.put(PLAYERS_ATTR, players);
        } else {
            // only show the number of players online if you are not signin
            vm.put(NUM_PLAYERS_ATTR, playerServices.getPlayerList().size());
            vm.put(MESSAGE_ATTR, WELCOME_MSG);
        }

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
