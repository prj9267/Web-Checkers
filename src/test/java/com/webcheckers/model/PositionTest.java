package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class PositionTest {

    private Position CuT;

    @Test
    void checkPosition(){
        int row = 0;
        int col = 0;
        CuT = new Position(row, col);
        assertEquals(0, CuT.getRow(), "Row not constructed properly");
        assertEquals(0, CuT.getCell(), "Cell not constucted properly");
    }

    @Test
    void checkInvalidRow() {
        int row = -1;
        int col = 0;
        CuT = new Position(row, col);
        assertEquals(0, CuT.getRow(), "Position constructed with negative row");
    }
}
