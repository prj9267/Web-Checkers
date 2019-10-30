package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.*;

import java.util.*;

public class PostValidateMoveRoute implements Route {
    // attributes
    private PlayerServices playerServices;
    private GameCenter gameCenter;
    private TemplateEngine templateEngine;
    private final Gson gson;

    // Values used in the view-model map for rendering the game view.
    // move
    public static final Message VALID_MOVE_MESSAGE = Message.info("This is a valid move.");
    public static final Message ADJACENT_MOVE_MESSAGE = Message.error("Your move must be adjacent to you.");
    public static final Message FORWARD_MOVE_MESSAGE = Message.error("Normal piece can only move forward.");
    public static final Message JUMP_OPTION_MESSAGE = Message.error("You must jump instead of move.");
    // jump
    public static final Message VALID_JUMP_MESSAGE = Message.info("This is a valid jump.");
    public static final Message ADJACENT_JUMP_MESSAGE = Message.error("Your jump must be adjacent to you.");
    public static final Message FORWARD_JUMP_MESSAGE = Message.error("Normal piece can only jump forward.");
    public static final Message OPPONENT_JUMP_MESSAGE = Message.error("You can only jump over your opponent.");

    // param name
    public static final String ACTION_DATA = "actionData";

    public PostValidateMoveRoute(PlayerServices playerServices,
                                 GameCenter gameCenter,
                                 TemplateEngine templateEngine, Gson gson){
        this.gson = gson;
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
    public Message canMoveForward(Position start, Position end){
        //TODO cases for king
        // base case when there is an option to jump
        if (optionToJump())
            return JUMP_OPTION_MESSAGE; // you are not suppose to move if you can jump

        if (start.getRow() - end.getRow() == 1) { // can only move forward by one row
            int diff = start.getCell() - end.getCell();
            if (diff == 1 || diff == -1)
                return VALID_MOVE_MESSAGE; // valid move
            return ADJACENT_MOVE_MESSAGE; // move is larger than one row
        }
        else if (start.getRow() - end.getRow() < 0)
            return FORWARD_MOVE_MESSAGE; // you can only move forward

        return ADJACENT_MOVE_MESSAGE; // move is larger than one row
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
        Space myStart = board.getSpace(start.getRow(), start.getCell());
        Piece myPiece = myStart.getPiece();
        myStart.setPiece(null);
        myStart.changeValid(true);
        // adding a piece to the end position
        Space myEnd = board.getSpace(end.getRow(), end.getCell());
        myEnd.setPiece(myPiece);
        myEnd.changeValid(false);

        // updating opponent's board
        // remove the piece at the start position
        Space oppStart = opp.getSpace(7 - start.getRow(), 7 - start.getCell());
        Piece oppPiece = oppStart.getPiece();
        oppStart.setPiece(null);
        oppStart.changeValid(true);
        // adding a piece to the end position
        Space oppEnd = opp.getSpace(7 - end.getRow(), 7 - end.getCell());
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
    public Message canJumpForward(Piece.Color color, BoardView board, Position start, Position end){
        // TODO cases for king
        if (start.getRow() - end.getRow() == 2) { // can only jump forward by two rows
            int diff = start.getCell() - end.getCell();
            if (diff == 2 || diff == -2) { // can only jump left or right by two columns
                Space space = board.getSpace(start.getRow() - 1, start.getCell() + (diff / 2));
                if (space.getPiece().getColor() != color)
                    return VALID_JUMP_MESSAGE; // valid move
                else
                    return OPPONENT_JUMP_MESSAGE; // you cannot jump over your own piece
            }
            else
                return ADJACENT_JUMP_MESSAGE; // jump is larger than 2 cols in magnitude
        }
        else if (start.getRow() - end.getRow() < 0)
            return FORWARD_JUMP_MESSAGE; // normal piece can only jump forward

        return ADJACENT_JUMP_MESSAGE; // jump is larger than 2 rows in magnitude
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
        Space myStart = board.getSpace(start.getRow(), start.getCell());
        Piece myPiece = myStart.getPiece();
        myStart.setPiece(null);
        myStart.changeValid(true);
        // remove the piece that was jumped over
        int myDiff = start.getCell() - end.getCell();
        Space myKill = board.getSpace(start.getRow() - 1, start.getCell() + (myDiff / 2));
        myKill.setPiece(null);
        myKill.changeValid(true);
        // adding a piece to the end position
        Space myEnd = board.getSpace(end.getRow(), end.getCell());
        myEnd.setPiece(myPiece);
        myEnd.changeValid(false);


        // updating opponent's board
        // remove the piece at the start position
        Space oppStart = opp.getSpace(7 - start.getRow(), 7 - start.getCell());
        Piece oppPiece = oppStart.getPiece();
        oppStart.setPiece(null);
        oppStart.changeValid(true);
        // remove the piece that was jumped over
        int oppDiff = start.getCell() - end.getCell();
        Space oppKill = board.getSpace(7 - (start.getRow() - 1), 7 - (start.getCell() + (oppDiff / 2)));
        oppKill.setPiece(null);
        oppKill.changeValid(true);
        // adding a piece to the end position
        Space oppEnd = opp.getSpace(7 - end.getRow(), 7 - end.getCell());
        oppEnd.setPiece(oppPiece);
        oppEnd.changeValid(false);
    }

    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        if (playerServices != null) {
            final Map<String, Object> vm = new HashMap<>();
            vm.put(GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
            // get the information of the current user
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);

            // send current user to ftl
            vm.put(GetGameRoute.CURRENT_USER_ATTR, currentPlayer);

            // Get the information from the match
            Match currentMatch = gameCenter.getMatch(currentPlayer);
            Piece.Color activeColor = currentMatch.getActiveColor();
            Player redPlayer = currentMatch.getRedPlayer();
            Player whitePlayer = currentMatch.getWhitePlayer();

            // give player information to ftl
            httpSession.attribute(GetGameRoute.MATCH_ATTR, currentMatch);
            vm.put(GetGameRoute.RED_PLAYER_ATTR, redPlayer);
            vm.put(GetGameRoute.WHITE_PLAYER_ATTR, whitePlayer);

            // get different board from the match
            BoardView currentBoardView;
            BoardView whiteBoardView = currentMatch.getWhiteBoardView();
            BoardView redBoardView = currentMatch.getRedBoardView();

            // send the information accordingly
            if(currentPlayerName.equals(redPlayer.getName())) {
                currentBoardView = redBoardView;
            } else {
                currentBoardView = whiteBoardView;
            }

            Move move = gson.fromJson(request.queryParams(ACTION_DATA), Move.class);
            Position start = move.getStart();
            Position end = move.getEnd();

            System.out.println("action data: " + request.queryParams(ACTION_DATA));
            System.out.println("start: " + start);
            System.out.println("end: " + end);


            Message moveMessage = canMoveForward(start, end);
            Message jumpMessage = canJumpForward(activeColor, currentBoardView, start, end);
            boolean validMove = moveMessage.getType() == Message.Type.INFO;
            boolean validJump = jumpMessage.getType() == Message.Type.INFO;

            Message message;
            // a stack of moves before the player hit submit
            Stack<Move> moves;
            if (httpSession.attribute("moves") == null)
                moves = new Stack<>();
            else
                moves = httpSession.attribute("moves");
            // if it is valid
            if (validMove || validJump){
                if (validMove) {
                    if (activeColor == Piece.Color.RED)
                        moveForward(redBoardView, whiteBoardView, start, end);
                    else
                        moveForward(whiteBoardView, redBoardView, start, end);
                    message = moveMessage;
                }
                else if (validJump){
                    if (activeColor == Piece.Color.RED)
                        jumpForward(redBoardView, whiteBoardView, start, end);
                    else
                        jumpForward(whiteBoardView, redBoardView, start, end);
                    message = jumpMessage;
                }
                // add this move to the stack of move
                // so when we implement backup, we know the exact order
                moves.push(move);
                message = moveMessage;
            }
            //TODO need to implement fail message
            else {
                message = Message.error("You have to either move or jump.");
            }

            return gson.toJson(message);

        }
        return null;
    }
}
