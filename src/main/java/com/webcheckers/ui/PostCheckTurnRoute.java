package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

public class PostCheckTurnRoute implements Route {
    // Values used in the view-model map for rendering the game view.
    public static final Message isYourTurn = Message.info("true");
    public static final Message notYourTurn = Message.error("false");


    private boolean isMyTurn = false;

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final PlayerServices playerServices;
    private final Gson gson;

    /**
     * The constructor for the {@code POST /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public PostCheckTurnRoute(final PlayerServices playerServices,
                        final GameCenter gameCenter,
                        final TemplateEngine templateEngine, Gson gson){
        Objects.requireNonNull(playerServices, "playerServices must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerServices = playerServices;
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
        this.gson = gson;
    }

    /**
     * Check if it is the player's turn
     * @param currentPlayer current player
     * @param red red player
     * @param white white player
     * @param activeColor current active color
     * @return
     */
    public boolean isMyTurn(Player currentPlayer, Player red, Player white, Piece.Color activeColor){
        if (currentPlayer.equals(red) && activeColor == Piece.Color.RED ||
            currentPlayer.equals(white) && activeColor == Piece.Color.WHITE)
            return true;
        return false;
    }

    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        Message message;
        // get the information of the current user
        if (playerServices != null) {
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);
            Player opponentPlayer;

            // Get the information from the match
            Match currentMatch = gameCenter.getMatch(currentPlayer);
            Player redPlayer = currentMatch.getRedPlayer();
            Player whitePlayer = currentMatch.getWhitePlayer();

            //TODO take care of game end
            ArrayList<Position> pieces;
            ArrayList<Position> oppPieces = currentMatch.getWhitePieces();
            if (currentPlayer.equals(redPlayer)) {
                opponentPlayer = whitePlayer;
                pieces = currentMatch.getRedPieces();
                oppPieces = currentMatch.getWhitePieces();
            } else {
                opponentPlayer = redPlayer;
                pieces = currentMatch.getWhitePieces();
                oppPieces = currentMatch.getRedPieces();
            }

            if (pieces.size() == 0) {
                currentMatch.setWinner(opponentPlayer);
                currentPlayer.changeStatus(Player.Status.waiting);
                opponentPlayer.changeStatus(Player.Status.waiting);
                message = Message.info(opponentPlayer.getName() + " has captured all the pieces.");
                //gameCenter.removePlayer(currentPlayer);
            } else if (oppPieces.size() == 0) {
                currentMatch.setWinner(currentPlayer);
                currentPlayer.changeStatus(Player.Status.waiting);
                opponentPlayer.changeStatus(Player.Status.waiting);
                message = Message.info(currentPlayer.getName() + " has captured all the pieces.");
                //gameCenter.removePlayer(currentPlayer);
            } else {
                // verify turn
                if (isMyTurn(currentPlayer, redPlayer, whitePlayer, currentMatch.getActiveColor())) {
                    isMyTurn = true;
                    message = isYourTurn;
                } else {
                    isMyTurn = false;
                    message = notYourTurn;
                }
            }

            return gson.toJson(message);
        }
        else {
            response.redirect("/home");
            halt();
            return null;
        }
    }
}
