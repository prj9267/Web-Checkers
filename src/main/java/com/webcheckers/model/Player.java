package com.webcheckers.model;

public class Player {

    //Attributes
    private String name;
    private int games;
    private int won;
    private int lost;
    private float ratio;
    public enum Status {waiting, challenged, ingame}
    public Status status;
    private boolean recentlyInGame;

    /**
     * Player constructor
     * @param name  - sets name to the string provided
     */
    public Player(String name) {
        assert(name != null);
        this.name = name;
        status = Status.waiting;
        recentlyInGame = false;
    }

    public void changeRecentlyInGame(boolean bool) {
        recentlyInGame = bool;
    }

    public boolean wasRecentlyInGame() {
        return recentlyInGame;
    }

    /**
     * CHecks if a username contains an invalid char
     * @return  - false if username contains no invalid chars, true otherwise.
     */
    public Boolean containsInvalidCharacter() {
        String invalidRegex = "^[a-zA-Z0-9 ]+$";
        if(name.matches(invalidRegex))
            return false;
        return true;
    }

    /**
     * Name getter function
     * @return  - the name
     */
    public String getName(){
        return name;
    }

    public Status getStatus() {
        return status;
    }


    /**
     * Check if the player is playing a game or not
     * @return true if  the player is playing a game, else false
     */
    public boolean isInGame(){
        if (status == Status.ingame || status == Status.challenged)
            return true;
        return false;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    /**
     * Overridden equals function to determine if players are equal.
     * @param o - the object to be compared to
     * @return  - true if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Player){
            return((Player)o).getName().equals((this.getName()));
        }
        return false;
    }

}
