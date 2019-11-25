package com.webcheckers.model;

import com.webcheckers.ui.PostValidateMoveRoute;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class MatchTest {

    private Player redPlayer = new Player("user");
    private Player whitePlayer = new Player("user2");
    private Piece.Color activeColor = Piece.Color.RED;
    private Player winner = null;
    private Match match = new Match(redPlayer, whitePlayer);
    private ArrayList<Move> moves = new ArrayList<>();


    @Test
    public void constructorNullArgRedPlayer(){
        assertNotNull(redPlayer, "Red Player is null.");
    }

    @Test
    public void constructorNullArgWhitePlayer(){
        assertNotNull(whitePlayer, "White Player is null.");
    }

    @Test
    public void testCurrentPlayer(){
        assertEquals(match.getCurrentPlayer(), redPlayer, "getCurrentPlayer functions incorrectly.");
    }

    @Test
    public void testGetRedPlayer(){
        assertEquals(match.getRedPlayer(), redPlayer, "getRedPlayer functions incorrectly.");
    }

    @Test
    public void testGetWhitePlayer(){
        assertEquals(match.getWhitePlayer(), whitePlayer, "getWhitePlayer functions incorrectly.");
    }

    @Test
    public void testActiveColor(){
        assertEquals(match.getActiveColor(), activeColor, "getActiveColor functions incorrectly.");
    }

    @Test
    public void testWinner(){
        assertEquals(match.getWinner(), winner, "getWinner functions incorrectly.");
    }

//    @Test
//    public void testValidateMoveJump() {
//        Piece piece = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
//        Move moveOne = new Move(new Position(0,0), new Position(1, 1));
//
//
//        assertEquals(match.validateMove(moveOne), PostValidateMoveRoute.VALID_MOVE_MESSAGE,"Proper move broken");
//    }

//    @Test
//    public void testPopMove() {
//        moves.add(new Move(new Position(0,0), new Position(1,1)));
//        assertEquals();
//    }
}