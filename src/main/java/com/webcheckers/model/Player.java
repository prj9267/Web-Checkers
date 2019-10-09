package com.webcheckers.model;

public class Player {

    //Attributes
    private String name;
    private String password;

    /**
     * Player constructor
     * @param name  - sets name to the string provided
     */
    public Player(String name){
        this.name = name;
    }

    /**
     * Name getter function
     * @return  - the name
     */
    public String getName(){
        return name;
    }

    /**
     * Password Getter function
     * @return  - the player's password
     */
    public String getPassword(){ return password; }

    /**
     * Overridden equals function to determine if players are equal.
     * @param o - the object to be compared to
     * @return  - true if they are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Player){
            if(((Player) o).name == name && ((Player) o).password == password){
                return true;
            }
        }
        return false;
    }
}
