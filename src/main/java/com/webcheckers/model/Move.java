package com.webcheckers.model;

public class Move {
    // attributes
    private Position start;
    private Position end;

    /**
     * Constructor for Move
     * @param start     - start position of the pending move
     * @param end       - end position of the pending move
     */
    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Gets the start position of the Move
     * @return start    - start position
     */
    public Position getStart() {
        return start;
    }

    /**
     * Gets the end position of the Move
     * @return  end     - end position
     */
    public Position getEnd() {
        return end;
    }
}
