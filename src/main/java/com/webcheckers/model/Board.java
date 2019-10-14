package com.webcheckers.model;

import java.util.*;

public class Board implements Iterable {
    private static final int NUM_ROW = 8;
    private static final int NUM_COL = 8;
    private Piece.Color currentColor = Piece.Color.RED;
    private Space spaces[][];
    //private Row[] board = new Row[NUM_ROW];

    /**
     * Initializes the board representation as an array of Row objects.  Fills in the row objects with Space objects.
     */
    public Board() {
        Piece piece;
        currentColor = Piece.Color.RED;
        spaces = new Space[NUM_ROW][NUM_COL];

        for(int i = 0; i < NUM_ROW; i++) {
            //board[i] = new Row(i);
            //Row row = board[i];
            //Space[] col = row.getCol();

            for(int j = 0; j < NUM_COL; j++) {
                Space space = new Space(j);
                space.changeValid(false);
                piece = null;
                if (((i % 2) == 0) && ((j % 2) == 0)) {
                    if (i < 4) {
                        piece = new Piece(Piece.Type.NORMAL, Piece.Color.RED);
                    } else if (i > 3) {
                        piece = new Piece(Piece.Type.NORMAL, Piece.Color.WHITE);
                    } else {
                        space.changeValid(true);
                    }
                }
                else if (((i % 2) == 1) && ((j % 2) == 1)) {
                    if (i < 4) {
                        piece = new Piece(Piece.Type.NORMAL, Piece.Color.RED);
                    } else if (i > 3) {
                        piece = piece = new Piece(Piece.Type.NORMAL, Piece.Color.WHITE);
                    } else {
                        space.changeValid(true);
                    }
                }
                /*if (piece != null)
                    col[j] = new Space(j, i, true, piece);
                else
                    col[j] = new Space(j, i, false, null);
                */
                space.setPiece(piece);
                spaces[i][j] = space;
            }
        }
    }

    /**
     * Getter function for the board object;
     * @return  - this board
     */
    public Board getBoard() {
        return this;
    }

    /**
     * checks if the move is within the checkers board
     *
     * @return true if within board
     */
    public boolean isValid(int row, int col) {
        return (row < NUM_ROW && row >= 0 && col < NUM_COL && col >= 0);
    }

    @Override
    public Iterator iterator() {

        LinkedList<Row> rows = new LinkedList<Row>();
        for(int numOfRow = 0; numOfRow < NUM_ROW; numOfRow++) {
            Row row = new Row(numOfRow, Arrays.asList(spaces[numOfRow]));
            rows.add(row);
        }

        return rows.iterator();
    }
}
