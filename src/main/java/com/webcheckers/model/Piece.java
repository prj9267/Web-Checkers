package com.webcheckers.model;

public class Piece {
    private Status status;
    private Color color;

    /**
     * Type of the piece
     * normal - can only go forward
     * king - can go backward and diagonally
     */
    public enum Status {NORMAL, KING}

    /**
     * COLOR of the piece
     */
    public enum Color {RED, WHITE}

    public Piece(Status status, Color color) {
        this.status = status;
        this.color = color;
    }

    public void setType(Status status) {
        this.status = status;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
