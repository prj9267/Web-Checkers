package com.webcheckers.ui;

//import com.sun.tools.javac.comp.Todo;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.*;
//import javafx.geometry.Pos;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostValidateMoveRoute implements Route {
    // attributes
    private PlayerServices playerServices;
    private GameCenter gameCenter;
    private TemplateEngine templateEngine;

    // param name
    public static final String ACTION_DATA = "actionData";

    public PostValidateMoveRoute(PlayerServices playerServices,
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
     * Check if there is an option to jump. American rule states you have to jump
     * if you can jump.
     * @return boolean
     */
    public boolean optionToJump(){
        //TODO
        return false;
    }

    /**
     * Check if the piece moved forward one row
     * @param start initial position
     * @param end final position
     * @return integer representation of the validation of the move
     */
    public int canMoveForward(Position start, Position end){
        // base case when there is an option to jump
        if (optionToJump())
            return 3; // you are not suppose to move if you can jump

        if (start.getRow() - end.getRow() == 1) { // can only move forward by one row
            int diff = start.getCol() - end.getCol();
            if (diff == 1 || diff == -1)
                return 0; // valid move
            return 2; // move is larger than one row
        }
        return 1; // move is larger than one row
    }

    /**
     * After validating the move, move the piece
     * @param board current player's board
     * @param opp opponent player's board
     * @param start start position
     * @param end final position
     */
    public void moveForward(BoardView board, BoardView opp,
                            Position start, Position end){
        // updating current player's board
        // remove the piece at the start position
        Space myStart = board.getSpace(start.getRow(), start.getCol());
        Piece myPiece = myStart.getPiece();
        myStart.setPiece(null);
        myStart.changeValid(true);
        // adding a piece to the end position
        Space myEnd = board.getSpace(end.getRow(), end.getCol());
        myEnd.setPiece(myPiece);
        myEnd.changeValid(false);

        // updating opponent's board
        // remove the piece at the start position
        Space oppStart = opp.getSpace(7 - start.getRow(), 7 - start.getCol());
        Piece oppPiece = oppStart.getPiece();
        oppStart.setPiece(null);
        oppStart.changeValid(true);
        // adding a piece to the end position
        Space oppEnd = opp.getSpace(7 - end.getRow(), 7 - end.getCol());
        oppEnd.setPiece(oppPiece);
        oppEnd.changeValid(false);
    }

    /**
     * Check if the piece jumped over an opponent
     * @param board player's board
     * @param start initial position
     * @param end final position
     * @return integer value representing why the move is invalid
     */
    public int canJumpForward(Piece.Color color, BoardView board, Position start, Position end){
        if (start.getRow() - end.getRow() == 2) { // can only jump forward by two rows
            int diff = start.getCol() - end.getCol();
            if (diff == 2 || diff == -2) { // can only jump left or right by two columns
                Space space = board.getSpace(start.getRow() - 1, start.getCol() + (diff / 2));
                if (space.getPiece().getColor() != color)
                    return 0; // valid move
                return 2; // move is larger than 1 col
            }
        }
        return 1; // move is larger than 1 row
    }

    /**
     * After validating the jump, jump over
     * @param board current player's board
     * @param opp opponent's board
     * @param start start position
     * @param end final position
     * @return
     */
    public void jumpForward(BoardView board, BoardView opp,
                            Position start, Position end){

        // updating current player's board
        // remove the piece at the start position
        Space myStart = board.getSpace(start.getRow(), start.getCol());
        Piece myPiece = myStart.getPiece();
        myStart.setPiece(null);
        myStart.changeValid(true);
        // remove the piece that was jumped over
        int myDiff = start.getCol() - end.getCol();
        Space myKill = board.getSpace(start.getRow() - 1, start.getCol() + (myDiff / 2));
        myKill.setPiece(null);
        myKill.changeValid(true);
        // adding a piece to the end position
        Space myEnd = board.getSpace(end.getRow(), end.getCol());
        myEnd.setPiece(myPiece);
        myEnd.changeValid(false);


        // updating opponent's board
        // remove the piece at the start position
        Space oppStart = opp.getSpace(7 - start.getRow(), 7 - start.getCol());
        Piece oppPiece = oppStart.getPiece();
        oppStart.setPiece(null);
        oppStart.changeValid(true);
        // remove the piece that was jumped over
        int oppDiff = start.getCol() - end.getCol();
        Space oppKill = board.getSpace(7 - (start.getRow() - 1), 7 - (start.getCol() + (oppDiff / 2)));
        oppKill.setPiece(null);
        oppKill.changeValid(true);
        // adding a piece to the end position
        Space oppEnd = opp.getSpace(7 - end.getRow(), 7 - end.getCol());
        oppEnd.setPiece(oppPiece);
        oppEnd.changeValid(false);
    }

    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        Move actionMove = httpSession.attribute(ACTION_DATA);

        if (playerServices != null) {

            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);

            Match match = httpSession.attribute(GetGameRoute.MATCH_ATTR);
            Player redPlayer = match.getRedPlayer();

            BoardView board;
            BoardView opp;
            if (currentPlayer.equals(redPlayer)) {
                board = match.getRedBoardView();
                opp = match.getWhiteBoardView();
            } else {
                board = match.getWhiteBoardView();
                opp = match.getRedBoardView();
            }

            //TODO continue...



        }
        return null;
    }
}
