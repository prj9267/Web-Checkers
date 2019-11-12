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
    public static final Message ADJACENT_MOVE_ERROR = Message.error("Your move must be adjacent to you.");
    public static final Message FORWARD_MOVE_ERROR = Message.error("Normal piece can only move forward.");
    public static final Message JUMP_OPTION_ERROR = Message.error("You must jump instead of move.");
    // jump
    public static final Message VALID_JUMP_MESSAGE = Message.info("This is a valid jump.");
    public static final Message ADJACENT_JUMP_ERROR = Message.error("Your jump must be adjacent to you.");
    public static final Message FORWARD_JUMP_ERROR = Message.error("Normal piece can only jump forward.");
    public static final Message OPPONENT_JUMP_ERROR = Message.error("You can only jump over your opponent.");
    // neither
    public static final Message MAX_ROW_MESSAGE = Message.error("The maximum number of rows you can move is 2");
    // multiple jump
    public static final Message MOVE_ERROR = Message.error("After a jump, you can only jump.");
    public static final Message DIFFERENT_ERROR = Message.error("After a jump, you can move the previous piece");
    public static final Message END_ERROR = Message.error("There is no more jump can be made from this piece.\n" +
                                                            "You have to either submit or backup.");
    public static final Message MULTIPLE_ERROR = Message.error("You cannot jump if you just moved.");


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

    public boolean checkFourDirections(BoardView board, int row, int col,
                                       Piece piece, Piece.Color color){
        Space topLeft = board.getSpace(row - 1, col - 1);
        Space topLeftJump = board.getSpace(row - 2, col - 2);
        Space topRight = board.getSpace(row - 1, col + 1);
        Space topRightJump = board.getSpace(row - 2, col + 2);
        // case for normal piece
        if (spaceForJump(topLeft, topLeftJump, color))
            return true;
        else if (spaceForJump(topRight, topRightJump, color))
             return true;
         // case for king piece
        if (Piece.Type.KING == piece.getType()) {
            Space bottomLeft = board.getSpace(row + 1, col - 1);
            Space bottomLeftJump = board.getSpace(row + 2, col - 2);
            Space bottomRight = board.getSpace(row + 1, col + 1);
            Space bottomRightJump = board.getSpace(row + 2, col + 2);
            if (spaceForJump(bottomLeft, bottomLeftJump, color))
                return true;
            else if (spaceForJump(bottomRight, bottomRightJump, color))
                return true;
        }
        return false;
    }

    /**
     * Check if there is an option to jump. American rule states you have to jump
     * if you can jump.
     * @return boolean
     */
    public boolean optionToJump(BoardView board, ArrayList<Location> pieces, Piece.Color color){
        for (int i = 0; i < pieces.size(); i++){
            int row = pieces.get(i).getRow();
            int col = pieces.get(i).getCol();
            Space space = board.getSpace(row, col);
            Piece piece = space.getPiece();

            if (checkFourDirections(board, row, col, piece, color))
                return true;
        }
        return false;
    }

    /**
     * Check if you can jump to target piece
     * @param space space that will be jump over
     * @param target space that will end up if can jump
     * @param color color of the current player
     * @return boolean
     */
    public boolean spaceForJump(Space space, Space target, Piece.Color color){
        if (target != null && space != null){
            // there is a space to jump to
            // and there is space to jump over
            if (space.getPiece() == null)
                return false; // there is no opponent piece there
            if (space.getPiece().getColor() != color && // not ally
                target.getPiece() == null) { // that space is empty
                return true; // there is option for a jump
            }
        }
        return false;
    }

    /**
     * After validating the move, move the piece
     * @param board current player's board
     * @param opp opponent player's board
     * @param start start position
     * @param end final position
     */
    public void moveForward(BoardView board, BoardView opp,
                            Position start, Position end,
                            ArrayList<Location> pieces){
        // update the position of player's pieces
        Location startLocation = new Location(start.getRow(), start.getCell());
        Location endLocation = new Location(end.getRow(), end.getCell());
        pieces.remove(startLocation);
        pieces.add(endLocation);
        // update current player's board
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

        if (end.getRow() == 0){ // means the piece become a king
            myEnd.getPiece().setType(Piece.Type.KING);
            oppEnd.getPiece().setType(Piece.Type.KING);
        }
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
                            Position start, Position end,
                            ArrayList<Location> pieces,
                            ArrayList<Location> oppPieces,
                            Match match){
        // update the position of current player's pieces
        Location startLocation = new Location(start.getRow(), start.getCell());
        Location endLocation = new Location(end.getRow(), end.getCell());
        pieces.remove(startLocation);
        pieces.add(endLocation);
        // update current player's board
        // remove the piece at the start position
        Space myStart = board.getSpace(start.getRow(), start.getCell());
        Piece myPiece = myStart.getPiece();
        myStart.setPiece(null);
        myStart.changeValid(true);
        // remove the piece that was jumped over
        int xDiff = (start.getCell() - end.getCell()) / 2;
        int yDiff = (start.getRow() - end.getRow()) / 2;

        System.out.println("jumped y: " + (start.getRow() - yDiff));
        System.out.println("jumped x: " + (start.getCell() - xDiff));
        Space myKill = board.getSpace(start.getRow() - yDiff, start.getCell() - xDiff);
        match.getPiecesRemoved().push(myKill.getPiece());
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
        Space oppKill = opp.getSpace(7 - (start.getRow() - yDiff), 7 - (start.getCell() - xDiff));
        oppKill.setPiece(null);
        oppKill.changeValid(true);
        // adding a piece to the end position
        Space oppEnd = opp.getSpace(7 - end.getRow(), 7 - end.getCell());
        oppEnd.setPiece(oppPiece);
        oppEnd.changeValid(false);
        // remove the piece that was jumped over for the pieces array
        int deadY = 7 - (start.getRow() - yDiff);
        int deadX = 7 - (start.getCell() - xDiff);
        Location oppLocation = new Location(deadY, deadX);
        oppPieces.remove(oppLocation);

        System.out.println("opp y: " + Integer.toString(7 - (start.getRow() - yDiff)));
        System.out.println("opp x: " + Integer.toString(7 - (start.getCell() - xDiff)));

        if (end.getRow() == 0){ // means the piece become a king
            myEnd.getPiece().setType(Piece.Type.KING);
            oppEnd.getPiece().setType(Piece.Type.KING);
        }
    }

    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        if (playerServices != null) {
            // get the information of the current user
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);

            // Get the information from the match
            Match currentMatch = gameCenter.getMatch(currentPlayer);
            Piece.Color activeColor = currentMatch.getActiveColor();
            Player redPlayer = currentMatch.getRedPlayer();

            // give player information to ftl
            httpSession.attribute(GetGameRoute.MATCH_ATTR, currentMatch);

            // get different board from the match
            BoardView currentBoardView;
            BoardView whiteBoardView = currentMatch.getWhiteBoardView();
            BoardView redBoardView = currentMatch.getRedBoardView();

            ArrayList<Location> pieces;
            ArrayList<Location> oppPieces;
            Piece.Color color;
            // set the information accordingly
            if(currentPlayerName.equals(redPlayer.getName())) {
                currentBoardView = redBoardView;
                pieces = currentMatch.getRedPieces();
                oppPieces = currentMatch.getWhitePieces();
                color = Piece.Color.RED;
            } else {
                currentBoardView = whiteBoardView;
                pieces = currentMatch.getWhitePieces();
                oppPieces = currentMatch.getRedPieces();
                color = Piece.Color.WHITE;
            }

            Move move = gson.fromJson(request.queryParams(ACTION_DATA), Move.class);
            Position start = move.getStart();
            Position end = move.getEnd();
            int row = start.getRow();
            int col = start.getCell();
            Piece piece = currentBoardView.getSpace(row, col).getPiece();
            Piece.Type type = currentBoardView.getSpace(row, col).getPiece().getType();
            boolean isKing = (type == Piece.Type.KING);

            Message message;
            boolean isMove = false;
            boolean isJump = false;

            // a stack of moves before the player hit submit
            Stack<Move> moves;
            if (httpSession.attribute("moves") == null)
                moves = new Stack<>();
            else
                moves = httpSession.attribute("moves");

            // dealing with multiple jump
            if (moves.size() != 0){
                // check the previous move
                Move previousMove = moves.pop();
                Position previousStart = previousMove.getStart();
                Position previousEnd = previousMove.getEnd();
                System.out.println("previous y: " + previousEnd.getRow());
                System.out.println("previous x: " + previousEnd.getCell());

                // you cannot jump if you just moved
                if (previousStart.getRow() - previousEnd.getRow() == 1 ||
                    previousStart.getRow() - previousEnd.getRow() == -1)
                    message = MULTIPLE_ERROR;
                // you can only make jump from the previous piece
                else if (row != previousEnd.getRow() &&
                        col != previousEnd.getCell())
                    message = DIFFERENT_ERROR;
                // after a jump, you can only jump
                else if (row - end.getRow() == 1 ||
                    row - end.getRow() == -1)
                    message = MOVE_ERROR;


                // dealing with jump
                else if (row - end.getRow() == 2 ||
                        row - end.getRow() == -2){
                    // there is no more possible jump
                    if (! checkFourDirections(currentBoardView, row, col, piece, color))
                        message = END_ERROR;
                    // normal piece can only jump forward
                    else if (row - end.getRow() == -2 && type != Piece.Type.KING)
                        message = FORWARD_JUMP_ERROR;
                    else { // can jump forward by two rows
                        int xDiff = col - end.getCell();
                        int yDiff = row - end.getRow();
                        Space space = currentBoardView.getSpace(start.getRow() - (yDiff / 2),
                                start.getCell() - (xDiff / 2));
                        if (xDiff == 2 || xDiff == -2) { // can only jump left or right by two columns
                            if (space.getPiece().getColor() != color) {
                                message = VALID_JUMP_MESSAGE; // valid jump
                                isJump = true;
                            }
                            else
                                message = OPPONENT_JUMP_ERROR; // you cannot jump over your own piece
                        }
                        else
                            message = ADJACENT_JUMP_ERROR; // jump is larger than 2 cols in magnitude
                    }
                }
                else
                    message = MAX_ROW_MESSAGE;
                moves.push(previousMove);
            }

            // done with multiple jump check
            // do this is it is regular move
            else{
                // dealing with move
                if (row - end.getRow() == 1 ||
                        row - end.getRow() == -1) {
                    // you are not suppose to move if you can jump
                    System.out.println(pieces);
                    if (optionToJump(currentBoardView, pieces, color))
                        message = JUMP_OPTION_ERROR;
                    else if (row - end.getRow() == -1 && ! isKing)
                        message = FORWARD_MOVE_ERROR; // you can only move forward

                    else {
                        int diff = col - end.getCell();
                        if (diff == 1 || diff == -1) {
                            message = VALID_MOVE_MESSAGE; // valid move
                            isMove = true;
                        }
                        else
                            message = ADJACENT_MOVE_ERROR; // move is larger than one col
                    }
                }
                // dealing with jump
                else if (row - end.getRow() == 2 ||
                        row - end.getRow() == -2){
                    // dealing with jump
                    if (row - end.getRow() == -2 && type != Piece.Type.KING)
                        message = FORWARD_JUMP_ERROR; // normal piece can only jump forward

                    else { // can jump forward by two rows
                        int xDiff = col - end.getCell();
                        int yDiff = row - end.getRow();
                        Space space = currentBoardView.getSpace(start.getRow() - (yDiff / 2),
                                start.getCell() - (xDiff / 2));
                        if (xDiff == 2 || xDiff == -2) { // can only jump left or right by two columns
                            if (space.getPiece().getColor() != color) {
                                message = VALID_JUMP_MESSAGE; // valid jump
                                isJump = true;
                            }
                            else
                                message = OPPONENT_JUMP_ERROR; // you cannot jump over your own piece
                        }
                        else
                            message = ADJACENT_JUMP_ERROR; // jump is larger than 2 cols in magnitude
                    }
                }
                else
                    message = MAX_ROW_MESSAGE;
            }



            // if it is valid
            if (isMove) {
                if (activeColor == Piece.Color.RED)
                    moveForward(redBoardView, whiteBoardView, start, end, pieces);
                else
                    moveForward(whiteBoardView, redBoardView, start, end, pieces);
                // add this move to the stack of move
                // so when we implement backup, we know the exact order
                moves.push(move);
            }
            else if (isJump){
                if (activeColor == Piece.Color.RED)
                    jumpForward(redBoardView, whiteBoardView, start, end, pieces, oppPieces, currentMatch);
                else
                    jumpForward(whiteBoardView, redBoardView, start, end, pieces, oppPieces, currentMatch);
                piece = currentBoardView.getSpace(end.getRow(), end.getCell()).getPiece();
                Boolean hasNextJump = checkFourDirections(currentBoardView,
                        end.getRow(), end.getCell(),
                        piece, color);
                System.out.println("Position after jump: row = " + end.getRow() + " col = " + end.getCell());
                if (hasNextJump) {
                    System.out.println("there is a next jump.");
                    httpSession.attribute("hasNextJump", hasNextJump);
                }
                else
                    httpSession.removeAttribute("hasNextJump");
                // add this move to the stack of move
                // so when we implement backup, we know the exact order
                moves.push(move);
            }

            httpSession.attribute("moves", moves);
            return gson.toJson(message);

        }
        return null;
    }
}
