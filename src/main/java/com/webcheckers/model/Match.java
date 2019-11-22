package com.webcheckers.model;

import com.webcheckers.ui.GetGameRoute;
import com.webcheckers.ui.PostValidateMoveRoute;
import com.webcheckers.util.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

public class Match {
    private static final Logger LOG = Logger.getLogger(Match.class.getName());
    // to be implemented maybe here since game.ftl
    //private Map<String, Object> modeOptionAsJSON = null;
    private BoardView redBoardView = new BoardView(Piece.Color.RED);
    private BoardView whiteBoardView = new BoardView(Piece.Color.WHITE);
    private Player redPlayer;
    private Player whitePlayer;
    private Piece.Color activeColor;
    private Player winner = null;
    private ArrayList<Location> redPieces = initializePieces(redBoardView);
    private ArrayList<Location> whitePieces = initializePieces(whiteBoardView);
    private Stack<Piece> piecesRemoved = new Stack<>();
    private final Map<String, Object> modeOptions;
    private boolean isGameOver = false;
    public enum STATE {resigned, finished, running}
    private STATE state;
    private Stack<Move> moves;

    /**
     * Create a new match between 2 players.
     * @param redPlayer     - red player
     * @param whitePlayer   - white player
     */
    public Match(Player redPlayer, Player whitePlayer){
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.activeColor = Piece.Color.RED;
        this.state = STATE.running;
        this.modeOptions = new HashMap<>(2);
        this.modeOptions.put("isGameOver", false);
        this.modeOptions.put("gameOverMessage", null);
    }

    public ArrayList<Location> initializePieces(BoardView board){ // remove the color
        ArrayList<Location> pieces = new ArrayList<>();
        for (int y = 5; y < BoardView.NUM_ROW; y++){ // change back to 5
            Row row = board.getRow(y);
            for (int x = 0; x < BoardView.NUM_COL; x++){
                Space col = row.getCol(x);
                if (col.getPiece() != null) { // make sure there is piece
                    Location temp = new Location(y, x);
                    pieces.add(temp);
                }
            }
        }
        return pieces;
    }

    /**
     * Getter function for the red player.
     * @return  - the red player object
     */
    public Player getRedPlayer() {
        return redPlayer;
    }

    /**
     * Getter function for the white player.
     * @return  - the white player
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * Getter function for the current active color
     * @return
     */
    public Piece.Color getActiveColor() {
        return activeColor;
    }

    /**
     * Getter function for all the pieces red player has
     * @return red player's pieces as space
     */
    public ArrayList<Location> getRedPieces() {
        return redPieces;
    }

    /**
     * Getter function for all the pieces white player has
     * @return white player's pieces as space
     */
    public ArrayList<Location> getWhitePieces() {
        return whitePieces;
    }

    public Stack<Piece> getPiecesRemoved(){
        return piecesRemoved;
    }

    /**
     * Alternates the current turn of the match, between the red and white player.
     */
    public void changeActiveColor(){
        if(activeColor == Piece.Color.RED)
            activeColor = Piece.Color.WHITE;
        else
            activeColor = Piece.Color.RED;
    }

    /**
     * Getter function for current player's turn.
     * @return  - the player whose turn it is.
     */
    public Player getCurrentPlayer() {
        if(activeColor.equals(Piece.Color.RED))
            return redPlayer;
        return whitePlayer;
    }

    public Map<String, Object> getModeOptions() {
        return modeOptions;
    }

    /**
     * Getter function for red player's board view
     * @return red board view
     */
    public BoardView getRedBoardView(){ return redBoardView; }

    /**
     * Getter function for white player's board view
     * @return white board view
     */
    public BoardView getWhiteBoardView(){ return whiteBoardView; }

    /**
     * Getter function for the winner of the match.
     * @return  - the winner of the match.
     */
    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
        state = STATE.finished;
        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", winner.getName() + "captured all of the pieces.");
    }

    /**
     * returns the state of the game
     * @return
     */
    public STATE isGameResigned() {
        return state;
    }

    /**
     * sets the state of the game to resigned
     */
    public void resignGame(Player winner, Player loser) {
        this.winner = winner;
        state = STATE.resigned;
        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", loser.getName() + " has resigned.");
    }

    public Stack<Move> getMoves(){ return this.moves; }

    public void pushMove(Move move) { this.moves.push(move); }

    public Move popMove() { return this.moves.pop(); }

    public boolean checkFourDirections(BoardView board, Move move) {
        Position start = move.getStart();
        int row = start.getRow();
        int col = start.getCell();
        Piece piece = board.getSpace(row, col).getPiece();
        Piece.Color color = piece.getColor();

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

    public ArrayList<Object> validateMove(Player currentPlayer, Move move){
        ArrayList<Object> ret = new ArrayList<>();
        // Get the information from the match
        Player redPlayer = this.getRedPlayer();

        // get different board from the match
        BoardView currentBoardView;

        ArrayList<Location> pieces;
        ArrayList<Location> oppPieces;
        Piece.Color color;
        // set the information accordingly
        if (activeColor == Piece.Color.RED){
            currentBoardView = redBoardView;
            pieces = redPieces;
        }
        else{
            currentBoardView = whiteBoardView;
            pieces = whitePieces;
        }

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
                message = PostValidateMoveRoute.MULTIPLE_ERROR;
                // you can only make jump from the previous piece
            else if (row != previousEnd.getRow() &&
                    col != previousEnd.getCell())
                message = PostValidateMoveRoute.DIFFERENT_ERROR;
                // after a jump, you can only jump
            else if (row - end.getRow() == 1 ||
                    row - end.getRow() == -1)
                message = PostValidateMoveRoute.MOVE_ERROR;


                // dealing with jump
            else if (row - end.getRow() == 2 ||
                    row - end.getRow() == -2){
                // there is no more possible jump
                if (! checkFourDirections(currentBoardView, row, col, piece, activeColor))
                    message = PostValidateMoveRoute.END_ERROR;
                    // normal piece can only jump forward
                else if (row - end.getRow() == -2 && type != Piece.Type.KING)
                    message = PostValidateMoveRoute.FORWARD_JUMP_ERROR;
                else { // can jump forward by two rows
                    int xDiff = col - end.getCell();
                    int yDiff = row - end.getRow();
                    Space space = currentBoardView.getSpace(start.getRow() - (yDiff / 2),
                            start.getCell() - (xDiff / 2));
                    if (xDiff == 2 || xDiff == -2) { // can only jump left or right by two columns
                        if (space.getPiece().getColor() != activeColor) {
                            message = PostValidateMoveRoute.VALID_JUMP_MESSAGE; // valid jump
                            isJump = true;
                        }
                        else
                            message = PostValidateMoveRoute.OPPONENT_JUMP_ERROR; // you cannot jump over your own piece
                    }
                    else
                        message = PostValidateMoveRoute.ADJACENT_JUMP_ERROR; // jump is larger than 2 cols in magnitude
                }
            }
            else
                message = PostValidateMoveRoute.MAX_ROW_MESSAGE;
            moves.push(previousMove);
        }

        // done with multiple jump check
        // do this is it is regular move
        else{
            // dealing with move
            if (row - end.getRow() == 1 ||
                    row - end.getRow() == -1) {
                // you are not suppose to move if you can jump
                if (optionToJump(currentBoardView, pieces, activeColor))
                    message = PostValidateMoveRoute.JUMP_OPTION_ERROR;
                else if (row - end.getRow() == -1 && ! isKing)
                    message = PostValidateMoveRoute.FORWARD_MOVE_ERROR; // you can only move forward

                else {
                    int diff = col - end.getCell();
                    if (diff == 1 || diff == -1) {
                        message = PostValidateMoveRoute.VALID_MOVE_MESSAGE; // valid move
                        isMove = true;
                    }
                    else
                        message = PostValidateMoveRoute.ADJACENT_MOVE_ERROR; // move is larger than one col
                }
            }
            // dealing with jump
            else if (row - end.getRow() == 2 ||
                    row - end.getRow() == -2){
                // dealing with jump
                if (row - end.getRow() == -2 && type != Piece.Type.KING)
                    message = PostValidateMoveRoute.FORWARD_JUMP_ERROR; // normal piece can only jump forward

                else { // can jump forward by two rows
                    int xDiff = col - end.getCell();
                    int yDiff = row - end.getRow();
                    Space space = currentBoardView.getSpace(start.getRow() - (yDiff / 2),
                            start.getCell() - (xDiff / 2));
                    if (xDiff == 2 || xDiff == -2) { // can only jump left or right by two columns
                        if (space.getPiece().getColor() != activeColor) {
                            message = PostValidateMoveRoute.VALID_JUMP_MESSAGE; // valid jump
                            isJump = true;
                        }
                        else
                            message = PostValidateMoveRoute.OPPONENT_JUMP_ERROR; // you cannot jump over your own piece
                    }
                    else
                        message = PostValidateMoveRoute.ADJACENT_JUMP_ERROR; // jump is larger than 2 cols in magnitude
                }
            }
            else
                message = PostValidateMoveRoute.MAX_ROW_MESSAGE;
        }

        // pack up all the information and return it
        ret.add(message);
        ret.add(isMove);
        ret.add(isJump);
        return ret;
    }

}