package com.webcheckers.model;

import java.util.logging.Logger;

public class Match {
    private static final Logger LOG = Logger.getLogger(Match.class.getName());


    private Board board = new Board();
    private Player redPlayer;
    private Player whitePlayer;
    private Piece.Color currrentPlayerColor;
    private Player winner = null;

    /**
     * Create a new match between 2 players.
     * @param redPlayer     - red player
     * @param whitePlayer   - white player
     */
    public Match(Player redPlayer, Player whitePlayer){
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        currrentPlayerColor = Piece.Color.RED;
    }

    /**
     * Getter function for the board.
     * @return  - the board object
     */
    public Board getBoard() {
        return board;
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

    public Piece.Color getCurrrentPlayerColor() {
        return currrentPlayerColor;
    }

    /**
     * Getter function for current player's turn.
     * @return  - the player whose turn it is.
     */
    public Player getCurrentPlayer() {
        if(currrentPlayerColor.equals(Piece.Color.RED))
            return redPlayer;
        return whitePlayer;
    }

    /**
     * Getter function for the winner of the match.
     * @return  - the winner of the match.
     */
    public Player getWinner() {
        return winner;
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
