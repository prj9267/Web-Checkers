package com.webcheckers.model;

public class Move {
    // private attributes
    private Position start;
    private Position end;

    /**
     * Constructor
     * @param start start position
     * @param end end position
     */
    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
    }

    /**
     * Constructor
     * @param move string representation from JSON
     */
    public Move(String move){
        String replace = move.replaceAll("[\\D]", "");
        this.start = new Position(replace.indexOf(0), replace.indexOf(1));
        this.end = new Position(replace.indexOf(2), replace.indexOf(3));
    }

    public Position getStart(){
        return start;
    }

    public Position getEnd(){
        return end;
    }

    /**
     * Override the equals method
     * @param o the object that is compared to (should be class Move)
     * @return boolean
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Move){
            return ((Move)o).getStart().equals((this.getStart())) &&
                    ((Move)o).getEnd().equals(this.getEnd());
        }
        return false;
    }

    /**
     * Override the toString method
     * @return a string representation of this class
     */
    @Override
    public String toString(){
        return "Start: " + this.getStart() + " End: " + this.getEnd();
    }
}
