package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class RowTest {

    private final static int NUM_COL = 8;
    private Space[] cols;
    private Row CuT;
    public int index;

    @BeforeEach
    public void setup() {
        CuT = new Row(index);
        cols = new Space[NUM_COL];
    }

    @Test
    void checkGetIndex() {
        assertEquals(index, CuT.getIndex(), "Index is not equal");
    }

    @Test
    void checkGetCol() {
        assertEquals(cols.length, CuT.getCols().length, "Number of cols is incorrect");
    }

    @Test
    void checkRow() {
        Row row = new Row(index);
        assertEquals(row.getIndex(), CuT.getIndex(), "Row constructor not working");
        assertEquals(row.getCols().length, CuT.getCols().length, "Row constructor not working");
    }
}
