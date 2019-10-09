package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Row implements Iterable {

    //Attributes
    private final static int NUM_COL = 8;
    private Space[] col;
    public int index;

    /**
     * Row constructor, sets the index value of the row, and the column to a new array of Space objects
     * @param index - the index value of the row.
     */
    public Row(int index){
        col = new Space[NUM_COL];
        this.index = index;
    }

    /**
     * Index getter function.
     * @return  - the rows index
     */
    public int getIndex(){ return index; }

    /**
     * Column getter function
     * @return  - the array of space objects representing the column.
     */
    public Space[] getCol(){ return col; }

    /**
     * Overridden iteratable function to be able to iterate over a Row object.
     * @return  - the row object as an iterable collection, the array of space objects.
     */
    @Override
    public Iterator iterator(){
        return Arrays.asList(col).iterator();
    }
}
