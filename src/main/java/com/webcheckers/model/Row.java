package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Row implements Iterable {
    private final static int NUM_COL = 8;
    private Space[] col;
    public int index;

    public Row(int index){
        col = new Space[NUM_COL];
        this.index = index;
    }

    public int getIndex(){ return index; }

    public Space[] getCol(){ return col; }

    @Override
    public Iterator iterator(){
        return Arrays.asList(col).iterator();
    }
}
