package com.webcheckers.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class BoardViewTest {
    private BoardView CuT;
    private static final int NUM_ROW = 8;
    private static final int NUM_COL = 8;
    private Piece.Color activeColor;
    private Row[] rows = new Row[NUM_ROW];

    @BeforeEach
    void setup() {
        CuT = new BoardView(activeColor);
    }

//    @Test
//    void checkBoardView() {
//        assertEquals(CuT, new BoardView(activeColor), "Board view not constructed correctly");
//    }
//
//    @Test
//    void checkGetRow() {
//        assertEquals(new BoardView(activeColor).getRow(3), CuT.getRow(3), "Rows not equal");
//    }
}
