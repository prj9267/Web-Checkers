package com.webcheckers.model;

public class Location {
    int row;
    int col;

    public Location(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Location){
            return ((Location)o).getRow() == ((this.getRow())) ||
                    ((Location)o).getCol() == ((this.getCol()));
        }
        return false;
    }

    @Override
    public String toString(){
        return "{row: " + row + "col: " + col + "}";
    }
}
