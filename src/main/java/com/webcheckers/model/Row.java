package com.webcheckers.model;

import java.util.ArrayList;

public class Row {
    private ArrayList<Space> row = new ArrayList<>();
    private int index;

    public Row(int index){
        for(int i = 0; i < 8; i++){
            row.add(null);
        }

        this.index = index;
    }

    public int getIndex(){ return index; }

    public ArrayList<Space> getRow(){ return row; }
}
