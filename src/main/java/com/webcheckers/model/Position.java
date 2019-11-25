package com.webcheckers.model;

public class Position {
    // Attributes
    private int row;
    private int cell;

    /**
     * Constructor
     * @param row the row
     * @param cell the col
     */
    public Position(int row, int cell){
        if (row <= 7 && row >= 0)
            this.row = row;
        if (cell <= 7 && cell >= 0)
            this.cell = cell;
    }

    /**
     * Getter function for row
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter function for col
     * @return
     */
    public int getCell() {
        return cell;
    }

    /**
     * Override the equals function
     * @param o Should be class Position
     * @return boolean
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Position){
            return ((Position)o).getRow() == ((this.getRow())) &&
                    ((Position)o).getCell() == ((this.getCell()));
        }
        return false;
    }

    /**
     * Override the toString function
     * @return a string representation of the object
     */
    @Override
    public String toString(){
        return "row: " + row + ", cell: " + cell;
    }
}
