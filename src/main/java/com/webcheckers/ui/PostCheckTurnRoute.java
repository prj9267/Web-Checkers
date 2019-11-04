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
    public static final Message isYourTurn = Message.info("It is your turn");
    public static final Message notYourTurn = Message.error("It is not your turn yet");


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

    public boolean isMyTurn(Player currentPlayer, Player red, Player white, Piece.Color activeColor){
        if (currentPlayer.equals(red) && activeColor == Piece.Color.RED ||
            currentPlayer.equals(white) && activeColor == Piece.Color.WHITE)
            return true;
        return false;
    }

    @Override
    public Object handle(Request request, Response response){
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        if(playerServices != null) {
            // get the information of the current user
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);


            // Get the information from the match
            Match currentMatch = gameCenter.getMatch(currentPlayer);
            Player redPlayer = currentMatch.getRedPlayer();
            Player whitePlayer = currentMatch.getWhitePlayer();

            // get different board from the match
            BoardView whiteBoardView = currentMatch.getWhiteBoardView();
            BoardView redBoardView = currentMatch.getRedBoardView();


            //TODO take care of game end
            ArrayList<Location> pieces;
            ArrayList<Location> oppPieces = currentMatch.getWhitePieces();
            if (currentPlayer.equals(redPlayer)) {
                pieces = currentMatch.getRedPieces();
                oppPieces = currentMatch.getWhitePieces();
            }
            else{

                pieces = currentMatch.getWhitePieces();
                oppPieces = currentMatch.getRedPieces();
            }

            if (pieces.size() == 0) {
                gameCenter.removePlayer(currentPlayer);
                currentPlayer.changeStatus(Player.Status.waiting);
                currentMatch.setWinner(currentPlayer);
            }
            else if (oppPieces.size() == 0){
                gameCenter.removePlayer(currentPlayer);
                currentPlayer.changeStatus(Player.Status.waiting);
                currentMatch.resignGame();
                currentMatch.setWinner(currentPlayer);
            }
            // verify turn
            Message message;
            if (isMyTurn(currentPlayer, redPlayer, whitePlayer, currentMatch.getActiveColor())) {
                isMyTurn = true;
                message = isYourTurn;
            } else {
                isMyTurn = false;
                message = notYourTurn;
            }

            return gson.toJson(message);
        }
        else{
            //TODO take care of resign
            /*response.redirect("/");
            halt();*/
            return null;
        }
    }
}
