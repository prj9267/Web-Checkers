package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.CSVutility;
import com.webcheckers.model.Match;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

public class PostResignGameRoute implements Route {
    private PlayerServices playerServices;
    private GameCenter gameCenter;
    private TemplateEngine templateEngine;
    private final CSVutility csvutility;
    private final Gson gson;
    private final Message message = Message.info("You've resigned.");
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
                               TemplateEngine templateEngine,
                               Gson gson) {
        Objects.requireNonNull(playerServices, "playerServices must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerServices = playerServices;
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
        this.gson = gson;
        this.csvutility = new CSVutility();
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
        // just in case there are moves that is not removed
        httpSession.removeAttribute("moves");
        Player currentPlayer = playerServices.getPlayer(username);
        Match currentMatch = gameCenter.getMatch(currentPlayer);
        Player opponentPlayer;
        if (currentMatch.getRedPlayer().equals(currentPlayer))
            opponentPlayer = currentMatch.getWhitePlayer();
        else
            opponentPlayer = currentMatch.getRedPlayer();
        currentPlayer.changeStatus(Player.Status.waiting);
        currentMatch.resignGame(currentPlayer, opponentPlayer);
        // delete the player from the ingame match list after exiting to home/lobby
        currentPlayer.changeRecentlyInGame(true);
        // guard so that their records won't be modified more than once in case of mandatory refreshes
        if (currentPlayer.getRecordsModified() == false) {
            int piecesTaken, piecesLost;
            if (currentPlayer.equals(currentMatch.getRedPlayer())) {
                piecesTaken = 12 - currentMatch.getWhitePieces().size();
                piecesLost = 12 - currentMatch.getRedPieces().size();
            } else {
                piecesTaken = 12 - currentMatch.getRedPieces().size();
                piecesLost = 12 - currentMatch.getWhitePieces().size();
            }
            currentPlayer.addPiecesTaken(piecesTaken);
            currentPlayer.addPiecesLost(piecesLost);
            currentPlayer.addLost();
            csvutility.editPlayerRecords(currentPlayer);
            currentPlayer.setRecordsModified(true);
        }
        //redirect to home since that's the next page after ending a game by sending a message
        return gson.toJson(message);
    }
}
