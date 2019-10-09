package com.webcheckers.model;

import java.util.ArrayList;

public class Board {
    private static final int ROW = 8;
    private static final int COL = 8;
    private Space[][] spaces;
    private ArrayList<Row> board = new ArrayList<>();

    public Board() {
        Space space;
        Piece piece;
        spaces = new Space[ROW][COL];

        /*for (int i = 0; i < 8; i++) {

            Row row = new Row(i);
            board.add(row);

            for (int j = 0; j < 8; j++) {
                if (((i % 2) == 0) && ((j % 2) == 0) && (i < 3)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.RED);
                } else if (((i % 2) == 1) && ((j % 2) == 1) && (i < 3)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.RED);

                } else if (((i % 2) == 0) && ((j % 2) == 0) && (i > 4)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.WHITE);

                } else if (((i % 2) == 1) && ((j % 2) == 1) && (i > 4)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.WHITE);

                } else {
                    piece = null;
                }
                space = new Space(j, i, true, piece);
                row.getRow().add(i, space);
            }
        }*/

        for(int i = 0; i < ROW; i++) {
            for(int j = 0; j < COL; j++) {
                if (((i % 2) == 0) && ((j % 2) == 0) && (i < 3)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.RED);
                    spaces[i][j] = new Space(j, i, true, piece);
                }
                else if (((i % 2) == 1) && ((j % 2) == 1) && (i < 3)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.RED);
                    spaces[i][j] = new Space(j, i, true, piece);
                }
                else if (((i % 2) == 0) && ((j % 2) == 0) && (i > 4)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.WHITE);
                    spaces[i][j] = new Space(j, i, true, piece);
                }
                else if (((i % 2) == 1) && ((j % 2) == 1) && (i > 4)) {
                    piece = new Piece(Piece.Status.NORMAL, Piece.Color.WHITE);
                    spaces[i][j] = new Space(j, i, true, piece);
                }
                else{
                    spaces[i][j] = new Space(j, i, true, null);
                }
            }
        }
    }

    public Space[][] getBoard(){ return spaces; }

    /**
     * checks if the move is within the checkers board
     *
     * @return true if within board
     */
    public boolean isValid(int row, int col) {
        return (row < ROW && row >= 0 && col < COL && col >= 0);
    }
}
