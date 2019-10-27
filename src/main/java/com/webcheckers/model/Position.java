package com.webcheckers.model;

public class Position {
    private int row;
    private int col;

    public Position(int row, int col){
        if (row <= 7 && row >= 0)
            this.row = row;
        if (col <= 7 && col >= 0)
            this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
