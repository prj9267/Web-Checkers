package com.webcheckers.model;

public class Player {
    private String name;
    private String password;

    public Player(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getPassword(){ return password; }

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
