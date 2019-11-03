package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.*;

import java.util.Objects;
import java.util.Stack;
import java.util.logging.Logger;

public class PostBackupMoveRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final PlayerServices playerServices;
    private final Gson gson;

    /**
     * The constructor for the {@code POST /backupMove} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public PostBackupMoveRoute(final PlayerServices playerServices,
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
     * Updates the current player's board after the backup button is clicked.
     *
     * @param currentPlayer - the current player hitting the backup button.
     * @param previousMove  - the previous move that is going to be undone.
     */
    public void updateCurrentPlayerBoard(Player currentPlayer, Move previousMove){
        Match currentMatch = gameCenter.getMatch(currentPlayer);
        BoardView boardView;

        //Determine which color the current player is.
        if(currentPlayer.getName().equals(currentMatch.getRedPlayer().getName())) {
            boardView = currentMatch.getRedBoardView();
        }
        else {
            boardView = currentMatch.getWhiteBoardView();
        }

        Position newStart = previousMove.getEnd();
        Position newEnd = previousMove.getStart();

        //Remove the piece at the old end location
        Space myStart = boardView.getSpace(newStart.getRow(), newStart.getCell());
        Piece myPiece = myStart.getPiece();
        myStart.setPiece(null);
        myStart.changeValid(true);

        //Put the piece in the old start location.
        Space myEnd = boardView.getSpace(newEnd.getRow(), newEnd.getCell());
        myEnd.setPiece(myPiece);
        myEnd.changeValid(false);
    }

    /**
     * Updates the opponent's board after the current player clicks the backup button.
     *
     * @param currentPlayer - the current player who clicks the backup button.
     * @param previousMove  - the previous move that is going to be undone.
     */
    public void updateOpponentPlayerBoard(Player currentPlayer, Move previousMove){
        Match currentMatch = gameCenter.getMatch(currentPlayer);
        BoardView boardView;

        //Determine which color the opponent player is.
        if(currentPlayer.getName().equals(currentMatch.getRedPlayer().getName())) {
            boardView = currentMatch.getWhiteBoardView();
        }
        else {
            boardView = currentMatch.getRedBoardView();
        }

        Position newStart = previousMove.getEnd();
        Position newEnd = previousMove.getStart();

        //Remove the piece at the old end location
        Space oppStart = boardView.getSpace(7 - newStart.getRow(), 7 - newStart.getCell());
        Piece oppPiece = oppStart.getPiece();
        oppStart.setPiece(null);
        oppStart.changeValid(true);

        //Put the piece in the old start location.
        Space oppEnd = boardView.getSpace(7 - newEnd.getRow(), 7 - newEnd.getCell());
        oppEnd.setPiece(oppPiece);
        oppEnd.changeValid(false);
    }

    /**
     * Refresh the game view after a turn was submitted.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the message displayed after submitting a turn.
     */
    @Override
    public Object handle(Request request, Response response){
        LOG.finer("PostBackupMoveRoute has been invoked.");

        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        Message message;

        if(playerServices != null){
            Stack<Move> moves = httpSession.attribute("moves");
            Move previousMove = moves.pop();

            // get the information of the current user
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);

            updateCurrentPlayerBoard(currentPlayer, previousMove);
            updateOpponentPlayerBoard(currentPlayer, previousMove);

            message = Message.info("Backup Successful");
            return gson.toJson(message);
        }

        else
            return null;
    }
}
