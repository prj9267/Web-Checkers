package com.webcheckers.model;

public class Player {

    //Attributes
    private String name;
    private int hash;
    public enum Status {waiting, challenged, ingame}
    public Status status;

    /**
     * Player constructor
     * @param name  - sets name to the string provided
     */
    public Player(String name) {
        assert(name != null);
        this.name = name;
        status = Status.waiting;
        hash = name.hashCode();
    }

    /**
     * Checks if a username contains an invalid char
     * @return  - false if username contains no invalid chars, true otherwise.
     */
    /*public Boolean containsInvalidCharacter(){
        String invalidCharacters = "~`!@#$%^&*()-_=+[]{}\\|;:',<.>/?\"";
        for (int i = 0; i < invalidCharacters.length(); i++) {
            if (name.indexOf(invalidCharacters.charAt(i)) >= 0)
                return true;
        }
        return false;
    }*/

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

    public int getHash() {
        return hash;
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

    /**
     * hashcode function for testing
     * @return  - the hashcode of the Player
     */
    public int hashcode(String name) {
        return name.hashCode();
    }
}
