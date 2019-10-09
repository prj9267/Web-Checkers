package com.webcheckers.model;

public class Piece {
    private Type type;
    private Color color;

    /**
     * Type of the piece
     * normal - can only go forward
     * king - can go backward and diagonally
     */
    public enum Type {NORMAL, KING}

    /**
     * COLOR of the piece
     */
    public enum Color {RED, WHITE}

    public Piece(Type status, Color color) {
        this.type = status;
        this.color = color;
    }

    public void setType(Type status) {
        this.type = status;
    }

    public Type getType(){
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
