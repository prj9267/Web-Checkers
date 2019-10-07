package com.webcheckers.appl;

public class Board {
    private static final int ROW = 8;
    private static final int COL = 8;
    private Tile[][] Tiles;
    //private Player activePlayer;



    public Board() {
        //todo
        /*Tiles = new Tile[ROW][COL];
        for(int i = 0; i < ROW; i++) {
            for(int j = 0; j < COL; j++) {

            }
        }*/
    }

    /**
     * checks if the move is within the checkers board
     * @return true if within board
     */
    public boolean isValid(int row, int col) {
        return (row < ROW && row >= 0 && col < COL && col >= 0);
    }
}
