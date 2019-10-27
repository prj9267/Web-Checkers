package com.webcheckers.model;

public class Position {
    // row
    private int row;
    // cell (column)
    private int cell;

    /**
     * Constructor for Position
     * @param row   - row of position {0 to 7}
     * @param cell  - cell of position {0 to 7}
     */
    public Position(int row, int cell) {
        assert((row >= 0 && row <= 7) && (cell >=0 && cell <= 7));
        this.row = row;
        this.cell = cell;
    }

    /**
     * Gets the row number
     * @return row  - row of position {0 to 7}
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the cell number (column)
     * @return cell - cell of position {0 to 7}
     */
    public int getCell() {
        return cell;
    }
}
