package com.webcheckers.appl;

public class Tile {
    private final int ROW;
    private final int COL;
    private boolean isEmpty;

    public Tile(int row, int col, boolean isEmpty) {
        this.ROW = row;
        this.COL = col;
        this.isEmpty = isEmpty;
    }

    public int getRow() {
        return ROW;
    }

    public int getCol() {
        return COL;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
