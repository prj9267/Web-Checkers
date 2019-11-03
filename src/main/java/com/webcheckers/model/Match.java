package com.webcheckers.model;

import java.util.HashMap;
import java.util.Map;
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
    private final Map<String, Object> modeOptions;
    private boolean isGameOver;
    public enum STATE {resigned, running}
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
     * Getter function for current player's turn.
     * @return  - the player whose turn it is.
     */
    public Player getCurrentPlayer() {
        if(activeColor.equals(Piece.Color.RED)) {
            currentPlayer = redPlayer;
            return redPlayer;
        }
        otherPlayer = whitePlayer;
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

    /**
     * set the winner of the match
     * @param winner    - the winner of the match
     */
    public void setWinner(Player winner) {
        this.winner = winner;
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
    public void resignGame() {
        state = STATE.resigned;
        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", "Resigned game.");
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
