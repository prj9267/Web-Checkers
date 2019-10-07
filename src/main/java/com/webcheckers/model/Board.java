package com.webcheckers.model;

public class Board {
    private static final int ROW = 8;
    private static final int COL = 8;
    private Space[][] spaces;

    public Board() {
        Space space;
        Piece piece;
        spaces = new Space[ROW][COL];
        for(int i = 0; i < ROW; i++) {
            for(int j = 0; j < COL; j++) {
                space = spaces[i][j] = new Space(i + j, false, null);
                if (((i % 2) == 0) && ((j % 2) == 0) && (i < 3)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.RED);
                    spaces[i][j] = new Space(j, true, piece);
                }
                if (((i % 2) == 1) && ((j % 2) == 1) && (i < 3)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.RED);
                    spaces[i][j] = new Space(j, true, piece);
                }
                if (((i % 2) == 0) && ((j % 2) == 0) && (i > 4)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.WHITE);
                    spaces[i][j] = new Space(j, true, piece);
                }
                if (((i % 2) == 1) && ((j % 2) == 1) && (i > 4)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.WHITE);
                    spaces[i][j] = new Space(j, true, piece);
                }
            }
        }
    }

    /**
     * checks if the move is within the checkers board
     * @return true if within board
     */
    public boolean isValid(int row, int col) {
        return (row < ROW && row >= 0 && col < COL && col >= 0);
    }
}
