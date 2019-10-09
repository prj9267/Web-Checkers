package com.webcheckers.model;

import java.util.ArrayList;

public class Row {
    private static final int NUM_COL = 8;
    private Space[] col;
    private int index;

    public Row(int index){
        this.col = new Space[NUM_COL];
        this.index = index;
    }

    public int getIndex(){ return index; }

    public Space[] getRow(){ return col; }
}
