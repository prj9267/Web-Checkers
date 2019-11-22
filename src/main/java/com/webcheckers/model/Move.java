package com.webcheckers.model;


public class Move {
    private Position start;
    private Position end;

    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
    }

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

    @Override
    public boolean equals(Object o){
        if(o instanceof Move){
            return ((Move)o).getStart().equals((this.getStart())) &&
                    ((Move)o).getEnd().equals(this.getEnd());
        }
        return false;
    }
}
