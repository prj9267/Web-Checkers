package com.webcheckers.model;

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
    private Player currentPlayer;
    private Player otherPlayer;
    private Piece.Color activeColor;
    private Player winner = null;
    private ArrayList<Location> redPieces = initializePieces(redBoardView);
    private ArrayList<Location> whitePieces = initializePieces(whiteBoardView);
    private Stack<Piece> piecesRemoved = new Stack<>();
    private final Map<String, Object> modeOptions;
    private boolean isGameOver = false;
    public enum STATE {resigned, finished, running}
    private STATE state;

    /**
     * Create a new match between 2 players.
     * @param redPlayer     - red player
     * @param whitePlayer   - white player
     */
    public Match(Player redPlayer, Player whitePlayer){
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.activeColor = Piece.Color.RED;
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

    /**
     * check for the existence of the other player once the game has officially started
     */
    public void checkGameOver() {
        if(otherPlayer == null) {
            isGameOver = true;
        }
    }

    /**
     * Returns the game state
     * @return          - isGameOver game state
     */
    public boolean getGameOver() {
        return isGameOver;
    }

    /**
     * End the current players turn and switch to the other player.
     */
    /*public void endTurn(){
        if(playerTurn == whitePlayer){
            playerTurn = redPlayer;
        }
        else{
            playerTurn = whitePlayer;
        }
    }*/

}