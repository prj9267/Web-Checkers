package com.webcheckers.model;

public class Player {

    //Attributes
    private String name;
    private int games;
    private int won;
    private int lost;
    private String ratio;
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
        games = 0;
        won = 0;
        lost = 0;
        // undefined because no games played
        ratio = "undefined";
        status = Status.waiting;
        recentlyInGame = false;
    }

    /**
     * Player constructor
     * @param name sets name to the name provided
     * @param games number of games played
     * @param won number of games won
     * @param lost number of games lost
     */
    public Player(String name, int games, int won, int lost) {
        assert(name != null);
        this.name = name;
        this.games = games;
        this.won = won;
        this.lost = lost;
        ratio = Float.toString((float)won/games);
        status = Status.waiting;
        recentlyInGame = false;
    }

    public String getGames() {
        return Integer.toString(games);
    }

    public String getWon() {
        return Integer.toString(won);
    }

    public String getLost() {
        return Integer.toString(lost);
    }

    public String getRatio() {
        return ratio;
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
