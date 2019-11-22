package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
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
    public void updateMove(Player currentPlayer, Move previousMove){
        Match currentMatch = gameCenter.getMatch(currentPlayer);
        BoardView board;
        BoardView opp;
        ArrayList<Position> pieces;

        //Determine which color the current player is.
        if(currentPlayer.getName().equals(currentMatch.getRedPlayer().getName())) {
            board = currentMatch.getRedBoardView();
            opp = currentMatch.getWhiteBoardView();
            pieces = currentMatch.getRedPieces();
        }
        else {
            board = currentMatch.getWhiteBoardView();
            opp = currentMatch.getRedBoardView();
            pieces = currentMatch.getWhitePieces();
        }

        Position newStart = previousMove.getEnd();
        Position newEnd = previousMove.getStart();

        //Remove the piece at the old end location
        Space myStart = board.getSpace(newStart.getRow(), newStart.getCell());
        Piece myPiece = myStart.getPiece();
        myStart.setPiece(null);
        myStart.changeValid(true);
        //Put the piece in the old start location.
        Space myEnd = board.getSpace(newEnd.getRow(), newEnd.getCell());
        myEnd.setPiece(myPiece);
        myEnd.changeValid(false);
        // Update current player's pieces
        Position startLocation = new Position(newStart.getRow(), newStart.getCell());
        Position endLocation = new Position(newEnd.getRow(), newEnd.getCell());
        pieces.remove(startLocation);
        pieces.add(endLocation);

        // updating opponent's board
        // remove the piece at the start position
        Space oppStart = opp.getSpace(7 - newStart.getRow(), 7 - newStart.getCell());
        Piece oppPiece = oppStart.getPiece();
        oppStart.setPiece(null);
        oppStart.changeValid(true);
        // adding a piece to the end position
        Space oppEnd = opp.getSpace(7 - newEnd.getRow(), 7 - newEnd.getCell());
        oppEnd.setPiece(oppPiece);
        oppEnd.changeValid(false);

        if (newStart.getRow() == 0){ // means the piece become a king
            myEnd.getPiece().setType(Piece.Type.SINGLE);
            oppEnd.getPiece().setType(Piece.Type.SINGLE);
        }

    }

    /**
     * Updates the opponent's board after the current player clicks the backup button.
     *
     * @param currentPlayer - the current player who clicks the backup button.
     * @param previousMove  - the previous move that is going to be undone.
     */
    public void updateJump(Player currentPlayer, Move previousMove){
        Match currentMatch = gameCenter.getMatch(currentPlayer);
        BoardView board;
        BoardView opp;
        ArrayList<Position> pieces;
        ArrayList<Position> oppPieces;
        Piece pieceRemoved = currentMatch.getPiecesRemoved().pop();

        //Determine which color the opponent player is.
        if(currentPlayer.getName().equals(currentMatch.getRedPlayer().getName())) {
            board = currentMatch.getRedBoardView();
            opp = currentMatch.getWhiteBoardView();
            pieces = currentMatch.getRedPieces();
            oppPieces = currentMatch.getWhitePieces();
        }
        else {
            board = currentMatch.getWhiteBoardView();
            opp = currentMatch.getRedBoardView();
            pieces = currentMatch.getWhitePieces();
            oppPieces = currentMatch.getRedPieces();
        }

        Position newStart = previousMove.getEnd();
        Position newEnd = previousMove.getStart();

        // update the position of current player's pieces
        Position startLocation = new Position(newStart.getRow(), newStart.getCell());
        Position endLocation = new Position(newEnd.getRow(), newEnd.getCell());
        pieces.remove(startLocation);
        pieces.add(endLocation);
        // update current player's board
        // remove the piece at the start position
        Space myStart = board.getSpace(newStart.getRow(), newStart.getCell());
        Piece myPiece = myStart.getPiece();
        myStart.setPiece(null);
        myStart.changeValid(true);
        // remove the piece that was jumped over
        int xDiff = (newStart.getCell() - newEnd.getCell()) / 2;
        int yDiff = (newStart.getRow() - newEnd.getRow()) / 2;

        Space myKill = board.getSpace(newStart.getRow() - yDiff, newStart.getCell() - xDiff);
        myKill.setPiece(pieceRemoved);
        myKill.changeValid(false);
        // adding a piece to the end position
        Space myEnd = board.getSpace(newEnd.getRow(), newEnd.getCell());
        myEnd.setPiece(myPiece);
        myEnd.changeValid(false);

        // updating opponent's board
        // remove the piece at the start position
        Space oppStart = opp.getSpace(7 - newStart.getRow(), 7 - newStart.getCell());
        Piece oppPiece = oppStart.getPiece();
        oppStart.setPiece(null);
        oppStart.changeValid(true);
        // remove the piece that was jumped over
        Space oppKill = opp.getSpace(7 - (newStart.getRow() - yDiff), 7 - (newStart.getCell() - xDiff));
        oppKill.setPiece(pieceRemoved);
        oppKill.changeValid(false);
        // adding a piece to the end position
        Space oppEnd = opp.getSpace(7 - newEnd.getRow(), 7 - newEnd.getCell());
        oppEnd.setPiece(oppPiece);
        oppEnd.changeValid(false);
        // remove the piece that was jumped over for the pieces array
        int deadY = 7 - (newStart.getRow() - yDiff);
        int deadX = 7 - (newStart.getCell() - xDiff);
        Location oppLocation = new Location(deadY, deadX);
        oppPieces.remove(oppLocation);

        System.out.println("opp y: " + Integer.toString(7 - (newStart.getRow() - yDiff)));
        System.out.println("opp x: " + Integer.toString(7 - (newStart.getCell() - xDiff)));

        if (newStart.getRow() == 0){ // means the piece become a king
            myEnd.getPiece().setType(Piece.Type.SINGLE);
            oppEnd.getPiece().setType(Piece.Type.SINGLE);
        }
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
            // get the information of the current user
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);
            Match currentMatch = gameCenter.getMatch(currentPlayer);

            Move previousMove = currentMatch.popMove();
            if (previousMove.getEnd().getRow() == 0) {
                currentMatch.typeSingle();
            }

            /*if (yDiff == 1 || yDiff == -1)
                updateMove(currentPlayer, previousMove);
            else
                updateJump(currentPlayer, previousMove);*/

            message = Message.info("Backup Successful");
            return gson.toJson(message);
        }

        else
            return null;
    }
}
