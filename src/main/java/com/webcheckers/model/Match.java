package com.webcheckers.model;

import java.util.logging.Logger;

public class Match {
    private static final Logger LOG = Logger.getLogger(Match.class.getName());


    private Board board = new Board();
    private Player redPlayer;
    private Player whitePlayer;
    private Player playerTurn;
    private Player winner = null;

    /**
     * Create a new match between 2 players.
     * @param redPlayerName     - name of player
     * @param whitePlayerName   - name of player
     */
    public Match(String redPlayerName, String whitePlayerName){
        redPlayer = new Player(redPlayerName);
        whitePlayer = new Player(whitePlayerName);
        playerTurn = redPlayer;
    }

    /**
     * Getter function for the board.
     * @return  - the board object
     */
    public Board getBoard(){ return board; }

    /**
     * Getter function for the red player.
     * @return  - the red player object
     */
    public Player getRedPlayer(){ return redPlayer; }

    /**
     * Getter function for the white player.
     * @return  - the white player
     */
    public Player getWhitePlayer(){ return whitePlayer; }

    /**
     * Getter function for current player's turn.
     * @return  - the player whose turn it is.
     */
    public Player getPlayerTurn(){ return playerTurn; }

    /**
     * Getter function for the winner of the match.
     * @return  - the winner of the match.
     */
    public Player getWinner(){ return winner; }

    /**
     * End the current players turn and switch to the other player.
     */
    public void endTurn(){
        if(playerTurn == whitePlayer){
            playerTurn = redPlayer;
        }
        else{
            playerTurn = whitePlayer;
        }
    }

}
