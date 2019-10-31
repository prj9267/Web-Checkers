package com.webcheckers.model;

import java.util.*;

public class BoardView implements Iterable {
    private static final int NUM_ROW = 8;
    private static final int NUM_COL = 8;
    private Piece.Color activeColor;
    private Row[] rows = new Row[NUM_ROW];

    /**
     * Initializes the board representation as an array of Row objects.  Fills in the row objects with Space objects.
     */
    public BoardView(Piece.Color currentPlayerColor) {
        Piece piece;
        Piece.Color opponentPlayerColor;
        activeColor = currentPlayerColor;
        if (currentPlayerColor == Piece.Color.RED)
            opponentPlayerColor = Piece.Color.WHITE;
        else
            opponentPlayerColor = Piece.Color.RED;


        for(int i = 0; i < NUM_ROW; i++) {
            rows[i] = new Row(i);
            Row row = rows[i];

            for(int j = 0; j < NUM_COL; j++) {
                Space space = new Space(j);
                space.changeValid(false);
                piece = null;
                if (((i % 2) == 0) && ((j % 2) == 1)) {
                    if (i < 3) {
                        piece = new Piece(Piece.Type.SINGLE, opponentPlayerColor);
                    } else if (i > 4) {
                        piece = new Piece(Piece.Type.SINGLE, currentPlayerColor);
                    } else {
                        space.changeValid(true);
                    }
                } else if (((i % 2) == 1) && ((j % 2) == 0)) {
                    if (i < 3) {
                        piece = new Piece(Piece.Type.SINGLE, opponentPlayerColor);
                    } else if (i > 4) {
                        piece = new Piece(Piece.Type.SINGLE, currentPlayerColor);
                    } else {
                        space.changeValid(true);
                    }
                }
                space.setPiece(piece);
                row.setCol(space);
            }
            rows[i] = row;
        }
    }

    public BoardView getWhiteBoard() {
        return new BoardView(Piece.Color.WHITE);
    }

    public BoardView getRedBoard() {
        return new BoardView(Piece.Color.RED);
    }

    /**
     * Getter function for the board object;
     * @return  - this board
     */
    public BoardView getBoardView() {
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

    public Space getSpace(int row, int col){
        return rows[row].getCol(col);
    }

    public void movePiece(int row, Space space){
        rows[row].setCol(space);
    }

    public void removePiece(int row, int col){
        Space empty = new Space(col);
        rows[row].setCol(empty);
    }

    @Override
    public Iterator iterator() {
        return Arrays.asList(rows).iterator();
    }
}
