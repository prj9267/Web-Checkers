package com.webcheckers.ui;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Leaderboard;
import com.webcheckers.model.Match;
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
    static final String GAMES_ATTR = "games";
    static final String WON_ATTR = "won";
    static final String LOST_ATTR = "lost";
    static final String RATIO_ATTR = "ratio";
    static final String GAMESBOARD_ATTR = "gamesBoard";
    static final String WONBOARD_ATTR = "wonBoard";
    static final String LOSTBOARD_ATTR = "lostBoard";

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
     * Deletes the player from the ingame list if they recently left a match. Checks if the player still has a match
     * and they are still in the ingame list before deleting them from the list. Game is not removed since we still need
     * that if another player is in the game screen, which GetGameRoute still needs the game to refresh the page without
     * crashing.
     * @param player the player to remove from ingame list
     */
    public synchronized void deletePlayerFromList(Player player) {
        //if the player just recently left a match, delete them from the ingame list
        if (gameCenter.getMatch(player) != null && gameCenter.isInMatch(player)) {
            gameCenter.removePlayer(player);
            player.changeRecentlyInGame(false);
        }
    }

    /**
     * Deletes the match from gameCenter IF AND ONLY IF both players have left the game screen, since the GetGameRoute
     * needs match. Checks by seeing if both players have been deleted from the ingame list, which means that both players
     * have left the game screen.
     * @param player the player name which identifies which game to remove from gameCenter
     */
    public synchronized void deleteMatchIfPossible(Player player) {
        // if both players of the match has left the match, and returned to the home screen, delete the match
        // since nothing about the match is needed, otherwise will crash the session still in the game screen
        if (gameCenter.getMatch(player) != null) {
            Match match = gameCenter.getMatch(player);
            if (player.equals(match.getRedPlayer())) {
                if (!gameCenter.isInMatch(match.getWhitePlayer()))
                    gameCenter.removeMatch(match);
            } else if (player.equals(match.getWhitePlayer())) {
                if (!gameCenter.isInMatch(match.getRedPlayer()))
                    gameCenter.removeMatch(match);
            }
        }
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

            // if the player just recently left a match, delete them from the ingame list
            if (player.wasRecentlyInGame()) {
                deletePlayerFromList(player);
            }

            // if both players of the match has left the match, and returned to the home screen, delete the match
            // since nothing about the match is needed, otherwise will crash the session still in the game screen
            deleteMatchIfPossible(player);

            // Grab leaderboards
            Leaderboard leaderboard = new Leaderboard();
            leaderboard.updateAllBoards();
            TreeSet<Player> gamesBoard = leaderboard.getGamesBoard();
            TreeSet<Player> wonBoard = leaderboard.getWonBoard();
            TreeSet<Player> lostBoard = leaderboard.getLostBoard();

            //TODO DEBUG STATEMENTS

            for(Player p: gamesBoard)
                System.out.println(p.getName() + " Number of games: " + p.getGames());

            /*if (gameCenter.isInMatch(player)) {
                System.out.println(player.getName() + " is in a match!");
            } else {
                System.out.println(player.getName() + " is not in a match!");
            }
            if (gameCenter.getMatch(player) != null) {
                System.out.println(player.getName() + " has a match!");
            } else {
                System.out.println(player.getName() + " doesn't have a match!");
            }*/

            // redirect the challenged player to the game
            if (player.getStatus() == Player.Status.challenged ||
                player.getStatus() == Player.Status.ingame){
                response.redirect(WebServer.GAME_URL);
                halt();
                return null;
            }

            vm.put(MESSAGE_ATTR, SIGNIN_MSG);
            if (httpSession.attribute("message") != null)
                vm.put(MESSAGE_ATTR, httpSession.attribute("message"));
            httpSession.removeAttribute("message");
            vm.put(GAMES_ATTR, player.getGames());
            vm.put(WON_ATTR, player.getWon());
            vm.put(LOST_ATTR, player.getLost());
            vm.put(RATIO_ATTR, player.getRatio());
            vm.put(CURRENT_USERNAME_KEY, httpSession.attribute(CURRENT_USERNAME_KEY));
            players.remove(player);
            vm.put(PLAYERS_ATTR, players);
            vm.put(GAMESBOARD_ATTR, gamesBoard);
            vm.put(WONBOARD_ATTR, wonBoard);
            vm.put(LOSTBOARD_ATTR, lostBoard);
        } else {
            // only show the number of players online if you are not signed in
            vm.put(NUM_PLAYERS_ATTR, playerServices.getPlayerList().size());
            vm.put(MESSAGE_ATTR, WELCOME_MSG);
        }

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
