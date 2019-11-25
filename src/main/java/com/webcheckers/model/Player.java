package com.webcheckers.model;

public class Player {

    //Attributes
    private String name;
    private int games;
    private int won;
    private int lost;
    private int piecesTaken;
    private int piecesLost;
    private String ratio;
    public enum Status {waiting, challenged, ingame}
    public Status status;
    private boolean recentlyInGame;
    private boolean recordsModified;
    private boolean signOut = false;

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
        piecesTaken = 0;
        piecesLost = 0;
        // undefined because no games played
        ratio = "undefined";
        status = Status.waiting;
        recentlyInGame = false;
        recordsModified = false;
    }

    /**
     * Player constructor
     * @param name sets name to the name provided
     * @param games number of games played
     * @param won number of games won
     * @param lost number of games lost
     */
    public Player(String name, int games, int won, int lost, int piecesTaken, int piecesLost) {
        assert(name != null);
        this.name = name;
        this.games = games;
        this.won = won;
        this.lost = lost;
        this.piecesTaken = piecesTaken;
        this.piecesLost = piecesLost;
        ratio = Float.toString((float)won/games);
        status = Status.waiting;
        recentlyInGame = false;
        recordsModified = false;
    }

    public void setSignOut(Boolean boo){
        signOut = boo;
    }

    public boolean getSignOut(){
        return signOut;
    }
                           /**
     * Sets recordsEdited to True or False
     * @param recordsModified True or False
     */
    public void setRecordsModified(Boolean recordsModified) {
        this.recordsModified = recordsModified;
    }

    /**
     * Gets boolean recordsEdited to determine if end of match modified records already.
     * @return True if records were already modified once
     */
    public Boolean getRecordsModified() {
        return recordsModified;
    }

    /**
     * Get the number of games played
     * @return games number of games played
     */
    public int getGames() {
        return games;
    }

    /**
     * Get the number of games won
     * @return won number of games won
     */
    public int getWon() {
        return won;
    }

    /**
     * Get the number of games lost
     * @return lost number of games lost
     */
    public int getLost() {
        return lost;
    }

    /**
     * Increase the number of games played and games won. Updates w/l ratio.
     */
    public void addWon() {
        games++;
        won++;
        ratio = Float.toString((float)won/games);
    }

    /**
     * Increase the number of games lost and games played. Updates w/l ratio.
     */
    public void addLost() {
        games++;
        lost++;
        ratio = Float.toString((float)won/games);
    }

    /**
     * Add to the total number of pieces taken by the player
     * @param num number to add to piecesTaken
     */
    public void addPiecesTaken(int num) {
        piecesTaken += num;
    }

    /**
     * Returns the number of pieces taken by the player
     * @return piecesTaken
     */
    public int getPiecesTaken() {
        return piecesTaken;
    }

    /**
     * Add to the total number of pieces lost by the player
     * @param num number to add to piecesLost
     */
    public void addPiecesLost(int num) {
        piecesLost += num;
    }

    /**
     * Returns the number of pieces lost by the player
     * @return piecesLost
     */
    public int getPiecesLost() {
        return piecesLost;
    }

    /**
     * Get the w/l ratio.
     * @return
     */
    public String getRatio() {
        return ratio;
    }

    /**
     * Sets the player so that they just left a match recently.
     * @param bool true if just left a match
     */
    public void changeRecentlyInGame(boolean bool) {
        recentlyInGame = bool;
    }

    /**
     * Gets the boolean on whether or not they recently were in a match.
     * @return recentlyInGame boolean
     */
    public boolean wasRecentlyInGame() {
        return recentlyInGame;
    }

    /**
     * Checks if a username contains an invalid char
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
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Status getter function
     * @return the status
     */
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

    /**
     * change the stats
     * @param status status
     */
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
