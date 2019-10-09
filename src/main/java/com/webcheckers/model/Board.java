package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board implements Iterable {
    private static final int NUM_ROW = 8;
    private static final int NUM_COL = 8;
    private Row[] board = new Row[NUM_ROW];

    public Board() {
        Piece piece;

        for(int i = 0; i < NUM_ROW; i++) {
            board[i] = new Row(i);
            Row row = board[i];
            Space[] col = row.getCol();

            for(int j = 0; j < NUM_COL; j++) {
                if (((i % 2) == 0) && ((j % 2) == 0) && (i < 4)) {
                    piece = new Piece(Piece.Type.NORMAL, Piece.Color.RED);
                }
                else if (((i % 2) == 1) && ((j % 2) == 1) && (i < 4)) {
                    piece = new Piece(Piece.Type.NORMAL, Piece.Color.RED);
                }
                else if (((i % 2) == 0) && ((j % 2) == 0) && (i > 3)) {
                    piece = new Piece(Piece.Type.NORMAL, Piece.Color.WHITE);
                }
                else if (((i % 2) == 1) && ((j % 2) == 1) && (i > 3)) {
                    piece = new Piece(Piece.Type.NORMAL, Piece.Color.WHITE);
                }
                else{
                    piece = null;
                }
                if (piece != null)
                    col[j] = new Space(j, i, true, piece);
                else
                    col[j] = new Space(j, i, false, null);

            }
        }
    }

    public Board getBoard(){ return this; }

    /**
     * checks if the move is within the checkers board
     *
     * @return true if within board
     */
    public boolean isValid(int row, int col) {
        return (row < NUM_ROW && row >= 0 && col < NUM_COL && col >= 0);
    }

    @Override
    public Iterator iterator(){
        return Arrays.asList(board).iterator();
    }
}
