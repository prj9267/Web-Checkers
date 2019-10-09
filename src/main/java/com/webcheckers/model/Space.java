package com.webcheckers.model;

public class Space {
    private int cellIdx;
    private int cellIdy;
    private boolean isValid;
    private Piece piece;

    public Space(int cellIdx, int cellIdy, boolean isValid, Piece piece) {
        this.cellIdx = cellIdx;
        this.cellIdy = cellIdy;
        this.isValid = isValid;
        this.piece = piece;
    }

    public int getCellIdx() {
        return cellIdx;
    }

    public boolean isValid() {
        return isValid;
    }

    public void changeValid(boolean isValid) {
        this.isValid = isValid;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
