package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-Tier")
public class SpaceTest {
    private Piece piece =  new Piece(Piece.Type.SINGLE, Piece.Color.RED);
    private Space space = new Space(0, true, piece);

    @Test
    public void testGetCellIDX(){
        assertEquals(space.getCellIdx(), 0, "getCellIDx functions incorrectly.");
    }

    @Test
    public void testIsValid(){
        assertEquals(space.isValid(), true, "isValid functions incorrectly.");
    }

    @Test
    public void testChangeValid(){
        space.changeValid(false);
        assertEquals(space.isValid(), false, "changeValid functions incorrectly.");
    }

    @Test
    public void testGetPiece(){
        assertEquals(space.getPiece(), piece, "getPiece functions incorrectly.");
    }

    @Test
    public void testSetPiece(){
        Piece piece2 = new Piece(Piece.Type.KING, Piece.Color.WHITE);
        space.setPiece(piece2);
        assertEquals(space.getPiece(), piece2, "setPiece functions incorrectly.");
    }


}
