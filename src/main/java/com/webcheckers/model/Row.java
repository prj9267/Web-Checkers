package com.webcheckers.model;


import java.util.*;

public class Row implements Iterable {

    //Attributes
    private final static int NUM_COL = 8;
    private Space[] cols;
    public int index;

    /**
     * Row constructor, sets the index value of the row, and the column to a new array of Space objects
     * @param index - the index value of the row.
     */
    public Row(int index) {
        this.index = index;
        this.cols = new Space[NUM_COL];
    }

    /**
     * Index getter function.
     * @return  - the rows index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Column getter function
     * @return  - the array of space objects representing the column.
     */
    public Space[] getCol(){ return cols; }

    /**
     * Set one of the column
     * @param j       - the column index
     * @param space   - space for that column
     */
    public void setCol(int j, Space space){
        cols[j] = space;
    }

    /**
     * Set all of the columns
     * @param cols the array of columns
     */
    public void setCols(Space[] cols){
        this.cols = cols;
    }

    /**
     * Overridden iteratable function to be able to iterate over a Row object.
     * @return  - the row object as an iterable collection, the array of space objects.
     */
    @Override
    public Iterator iterator(){
        return Arrays.asList(cols).iterator();
    }
}
