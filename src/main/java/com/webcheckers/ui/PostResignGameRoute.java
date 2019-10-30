package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Match;
import com.webcheckers.model.Player;
import spark.*;

public class PostResignGameRoute implements Route {
    private PlayerServices playerServices;
    private GameCenter gameCenter;
    private TemplateEngine templateEngine;

    private static final String INSTRUCTION_MSG = "Choose your next action."; //TODO change
    private static final Logger LOG = Logger.getLogger(PostResignGameRoute.class.getName());

    /**
     * Constructor for the PostResignGameRoute
     * @param playerServices    - playerServices for list of players
     * @param gameCenter        - gameCenter for managing games
     * @param templateEngine    - templateEngine for display // might not need
     */
    public PostResignGameRoute(PlayerServices playerServices,
                               GameCenter gameCenter,
                               TemplateEngine templateEngine){
        Objects.requireNonNull(playerServices, "playerServices must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerServices = playerServices;
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
    }

    /**
     * Remove players from the inGame list in gameCenter since the game ended
     * @param redPlayer
     * @param whitePlayer
     */
    public void removePlayers(Player redPlayer, Player whitePlayer) {
        gameCenter.removePlayer(redPlayer);
        gameCenter.removePlayer(whitePlayer);

        redPlayer.changeStatus(Player.Status.waiting);
        whitePlayer.changeStatus(Player.Status.waiting);
    }

    /**
     * Render the WebCheckers home page after resigning.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign In page
     */
    public Object handle(Request request, Response response) {
        LOG.finer("PostResignGameRoute has been invoked.");

        final Session httpSession = request.session();

        String username = httpSession.attribute("currentPlayer");
        Player currentPlayer = playerServices.getPlayer(username);
        Match currentMatch = gameCenter.getMatch(currentPlayer);
        removePlayers(currentMatch.getRedPlayer(), currentMatch.getWhitePlayer());
        // TODO change, check whose the winner
        currentMatch.setWinner(currentPlayer);
        // TODO leave match or delete match? TBD
        //redirect to home since that's the next page after ending a game
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
