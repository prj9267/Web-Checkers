
package com.webcheckers.model;





import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



@Tag("Model-tier")
public class PieceTest {


    private Piece.Type single = Piece.Type.SINGLE;
    private Piece.Type king = Piece.Type.KING;
    private Piece.Color red = Piece.Color.RED;
    private Piece.Color white = Piece.Color.WHITE;


    public Piece singleWhite = new Piece(single, red);
    public Piece kingRed = new Piece(king, white);


    @Test
    public void createPiece() {
        assertSame(singleWhite.getType(), single);
        assertSame(singleWhite.getColor(), white);
        assertSame(kingRed.getColor(), red);
        assertSame(kingRed.getType(), king);

    }
}
