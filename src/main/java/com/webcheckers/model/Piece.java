package com.webcheckers.model;

public class Piece {
    private Type type;
    private Color color;

    /**
     * Type of the piece
     * normal - can only go forward
     * king - can go backward and diagonally
     */
    public enum Type {SINGLE, KING}

    /**
     * COLOR of the piece
     */
    public enum Color {RED, WHITE}

    /**
     * Constructor for piece
     * @param type    - KING OR NORMAL
     * @param color     - RED OR WHITE
     */
    public Piece(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    /**
     * Set the piece type to a specified status.
     * @param status    - the new piece type
     */
    public void setType(Type status) {
        this.type = status;
    }

    /**
     * Getter function for the piece type.
     * @return  - the piece type
     */
    public Type getType(){
        return type;
    }

    /**
     * Getter function for the piece color
     * @return  - the piece color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter function for the piece color
     * @param color - the color to be set.
     */
    public void setColor(Color color) {
        this.color = color;
    }
}
