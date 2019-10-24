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
    }

    @Test
    void checkGetIndex() {
        assertEquals(index, CuT.getIndex(), "Index is not equal");
    }

    @Test
    void checkGetCol() {
        cols = new Space[NUM_COL];
        assertEquals(CuT.getCol(), cols, "Number of cols is incorrect");
    }
}
