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
    private ArrayList<Position> redPieces = initializePieces(redBoardView);
    private ArrayList<Position> whitePieces = initializePieces(whiteBoardView);
    private Stack<Piece> piecesRemoved = new Stack<>();
    private final Map<String, Object> modeOptions;
    private boolean isGameOver = false;
    public enum STATE {resigned, finished, running}
    private STATE state;
    private ArrayList<Move> moves = new ArrayList<>();
    private boolean hasNextJump = false;
    private Piece.Type currentType = Piece.Type.SINGLE;

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

    public ArrayList<Position> initializePieces(BoardView board){ // remove the color
        ArrayList<Position> pieces = new ArrayList<>();
        for (int y = 5; y < BoardView.NUM_ROW; y++){ // change back to 5
            Row row = board.getRow(y);
            for (int x = 0; x < BoardView.NUM_COL; x++){
                Space col = row.getCol(x);
                if (col.getPiece() != null) { // make sure there is piece
                    Position temp = new Position(y, x);
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
    public ArrayList<Position> getRedPieces() {
        return redPieces;
    }

    /**
     * Getter function for all the pieces white player has
     * @return white player's pieces as space
     */
    public ArrayList<Position> getWhitePieces() {
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

    public boolean hasNextJump(){
        return hasNextJump;
    }

    public void typeSingle() { this.currentType = Piece.Type.SINGLE; }

    public ArrayList<Move> getMoves(){ return this.moves; }

    public void pushMove(Move move) { this.moves.add(move); }

    public Move popMove() { return this.moves.remove(moves.size() - 1); }

    public void emptyMoves() { this.moves = new ArrayList<>(); }

    public boolean topLeftJump(BoardView board, Position pos) {
        int row = pos.getRow();
        int col = pos.getCell();
        Piece.Color color = activeColor;
        Space topLeft = board.getSpace(row - 1, col - 1);
        Space topLeftJump = board.getSpace(row - 2, col - 2);
        Position target = new Position(row - 2, col - 2);
        Move moveTopLeft = new Move(pos, target);
        Move backward = new Move(target, pos);
        return (! moves.contains(moveTopLeft)) &&
                (! moves.contains(backward)) &&
                spaceForJump(topLeft, topLeftJump, color);
    }

    public boolean topRightJump(BoardView board, Position pos) {
        int row = pos.getRow();
        int col = pos.getCell();
        Piece.Color color = activeColor;
        Space topRight = board.getSpace(row - 1, col + 1);
        Space topRightJump = board.getSpace(row - 2, col + 2);
        Position target = new Position(row - 2, col + 2);
        Move moveTopRight = new Move(pos, target);
        Move backward = new Move(target, pos);
        return (! moves.contains(moveTopRight)) &&
                (! moves.contains(backward)) &&
                spaceForJump(topRight, topRightJump, color);
    }

    public boolean botLeftJump(BoardView board, Position pos) {
        if (currentType != Piece.Type.KING) {
            return false;
        }
        Piece.Color color = activeColor;
        int row = pos.getRow();
        int col = pos.getCell();
        Space bottomLeft = board.getSpace(row + 1, col - 1);
        Space bottomLeftJump = board.getSpace(row + 2, col - 2);
        Position target = new Position(row + 2, col - 2);
        Move moveBotLeft = new Move(pos, target);
        Move backward = new Move(target, pos);
        return (! moves.contains(moveBotLeft)) &&
                (! moves.contains(backward)) &&
                spaceForJump(bottomLeft, bottomLeftJump, color);
    }

    public boolean botRightJump(BoardView board, Position pos) {
        if (currentType != Piece.Type.KING) {
            return false;
        }
        int row = pos.getRow();
        int col = pos.getCell();
        Piece.Color color = activeColor;
        Space bottomRight = board.getSpace(row + 1, col + 1);
        Space bottomRightJump = board.getSpace(row + 2, col + 2);
        Position target = new Position(row + 2, col + 2);
        Move moveBotRight = new Move(pos, target);
        Move backward = new Move(target, pos);
        return (! moves.contains(moveBotRight)) &&
                (! moves.contains(backward)) &&
                spaceForJump(bottomRight, bottomRightJump, color);
    }

    public boolean checkFourDirections(BoardView board, Position pos) {
        if (topLeftJump(board, pos)) {
            System.out.println("has top left jump");
            return true;
        }
        else if (topRightJump(board, pos)) {
            System.out.println("has top right jump");
            return true;
        }
        else if (botLeftJump(board, pos)) {
            System.out.println("has bottom left jump");
            return true;
        }
        else if (botRightJump(board, pos)) {
            System.out.println("has bottom right jump");
            return true;
        }
        else
            return false;

        /*return topLeftJump(board, pos) || topRightJump(board, pos) ||
                botLeftJump(board, pos) || botRightJump(board, pos);*/
    }

    /**
     * Check if there is an option to jump. American rule states you have to jump
     * if you can jump.
     * @return boolean
     */
    public boolean optionToJump(BoardView board, ArrayList<Position> pieces){
        for (int i = 0; i < pieces.size(); i++){
            int row = pieces.get(i).getRow();
            int col = pieces.get(i).getCell();
            Position pos = new Position(row, col);

            if (checkFourDirections(board, pos))
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
     * @param move the move made
     */
    public void move(Move move){
        // update the position of player's pieces
        Position start = move.getStart();
        Position end = move.getEnd();
        ArrayList<Position> pieces;
        BoardView board, opp;
        if (activeColor == Piece.Color.RED) {
            pieces = redPieces;
            board = redBoardView;
            opp = whiteBoardView;
        }
        else {
            pieces = whitePieces;
            board = whiteBoardView;
            opp = redBoardView;
        }
        pieces.remove(start);
        pieces.add(end);
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
     * @param move the move made by player
     * @return
     */
    public void jump(Move move){
        // update the position of current player's pieces
        Position start = move.getStart();
        Position end = move.getEnd();
        ArrayList<Position> pieces, oppPieces;
        BoardView board, opp;
        if (activeColor == Piece.Color.RED){
            pieces = redPieces;
            board = redBoardView;
            opp = whiteBoardView;
            oppPieces = whitePieces;
        }
        else {
            pieces = whitePieces;
            board = whiteBoardView;
            opp = redBoardView;
            oppPieces = redPieces;
        }
        pieces.remove(start);
        pieces.add(end);
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
        this.getPiecesRemoved().push(myKill.getPiece());
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
        Position oppLocation = new Position(deadY, deadX);
        oppPieces.remove(oppLocation);

        /*System.out.println("opp y: " + Integer.toString(7 - (start.getRow() - yDiff)));
        System.out.println("opp x: " + Integer.toString(7 - (start.getCell() - xDiff)));*/

        if (end.getRow() == 0){ // means the piece become a king
            myEnd.getPiece().setType(Piece.Type.KING);
            oppEnd.getPiece().setType(Piece.Type.KING);
        }
    }

    public Message validateMove(Player currentPlayer, Move move){
        // get different board from the match
        BoardView currentBoardView;

        ArrayList<Position> pieces;
        ArrayList<Position> oppPieces;
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
        if (moves.size() == 0) {
            Piece piece = currentBoardView.getSpace(row, col).getPiece();
            currentType = piece.getType();
        }
        else {
            Move temp = this.popMove();
            if (! temp.getEnd().equals(start)) {
                this.emptyMoves();
            }
            else {
                this.pushMove(temp);
            }
        }

        boolean isKing = (currentType == Piece.Type.KING);

        Message message;
        boolean isMove = false;
        boolean isJump = false;

        System.out.println("pieces: ==============");
        for (int i = 0; i < pieces.size(); i++) {
            System.out.println(pieces.get(i));
        }
        System.out.println("======================");

        // dealing with multiple jump
        if (moves.size() != 0){
            // check the previous move
            Move previousMove = this.popMove();
            Position previousStart = previousMove.getStart();
            Position previousEnd = previousMove.getEnd();

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
                if (! checkFourDirections(currentBoardView, start))
                    message = PostValidateMoveRoute.END_ERROR;
                // normal piece can only jump forward
                else if (row - end.getRow() == -2 && currentType != Piece.Type.KING) {
                    message = PostValidateMoveRoute.FORWARD_JUMP_ERROR;
                }
                // can jump forward by two rows
                else {
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
            this.pushMove(previousMove);
        }

        // done with multiple jump check
        // do this is it is regular move
        else{
            // dealing with move
            if (row - end.getRow() == 1 ||
                    row - end.getRow() == -1) {
                // you are not suppose to move if you can jump
                if (optionToJump(currentBoardView, pieces))
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
                if (row - end.getRow() == -2 && currentType != Piece.Type.KING)
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

        if (isMove) {
            this.pushMove(move);
            if (end.getRow() == 0) {
                currentType = Piece.Type.KING;
            }
        }
        else if (isJump){
            // add this move to the stack of move
            // so when we implement backup, we know the exact order
            this.pushMove(move);
            if (end.getRow() == 0) {
                currentType = Piece.Type.KING;
            }
            hasNextJump = checkFourDirections(currentBoardView, end);
            System.out.println("from position: " + end);
            System.out.println("there is next jump: " + hasNextJump);
        }

        return message;
    }

}