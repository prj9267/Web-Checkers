package com.webcheckers.model;

import java.util.ArrayList;
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
    private Piece.Color activeColor;
    private Player winner = null;
    private ArrayList<HashMap> redPieces = initializePieces(redBoardView);
    private ArrayList<HashMap> whitePieces = initializePieces(whiteBoardView);

    /**
     * Create a new match between 2 players.
     * @param redPlayer     - red player
     * @param whitePlayer   - white player
     */
    public Match(Player redPlayer, Player whitePlayer){
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        activeColor = Piece.Color.RED;
    }

    public ArrayList<HashMap> initializePieces(BoardView board){
        ArrayList<HashMap> pieces = new ArrayList<>();
        for (int y = 5; y < BoardView.NUM_ROW; y++){
            Row row = board.getRow(y);
            for (int x = 0; x < BoardView.NUM_COL; x++){
                Space col = row.getCol(x);
                if (col.getPiece() != null) { // make sure there is piece
                    HashMap<String, Integer> temp = new HashMap<>();
                    temp.put("row", y);
                    temp.put("col", x);
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
    public ArrayList<HashMap> getRedPieces() {
        return redPieces;
    }

    /**
     * Getter function for all the pieces white player has
     * @return white player's pieces as space
     */
    public ArrayList<HashMap> getWhitePieces() {
        return whitePieces;
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
