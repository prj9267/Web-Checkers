package com.webcheckers.model;

public class Position {
    private int row;
    private int cell;

    public Position(int row, int cell){
        if (row <= 7 && row >= 0)
            this.row = row;
        if (cell <= 7 && cell >= 0)
            this.cell = cell;
    }

    public int getRow() {
        return row;
    }

    public int getCell() {
        return cell;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Position){
            return ((Position)o).getRow() == ((this.getRow())) &&
                    ((Position)o).getCell() == ((this.getCell()));
        }
        return false;
    }
    @Override
    public String toString(){
        return "row: " + row + ", cell: " + cell;
    }
}
