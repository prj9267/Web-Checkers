package com.webcheckers.model;

public class Game {
    // attributes
    private Player redPlayer;
    private Player whitePlayer;
    private Piece.Color currentPlayerColor;
    private Board board;

    /**
     * Constructor for the game
     * @param whitePlayer
     * @param redPlayer
     */
    public Game(Player whitePlayer, Player redPlayer) {
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.currentPlayerColor = Piece.Color.RED;
        this.board = new Board();
    }

    public Player getPlayer(Piece.Color color) {
        if(color == Piece.Color.RED)
            return redPlayer;
        return whitePlayer;
    }

    public Board getBoard() {
        return board;
    }

    public Piece.Color getCurrentPlayerColor() {
        return currentPlayerColor;
    }

    public Player getActivePlayer()
}
