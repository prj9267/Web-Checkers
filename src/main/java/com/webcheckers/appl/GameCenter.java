package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.ArrayList;

public class GameCenter {

    private ArrayList<Player> PLAYERS = new ArrayList<>();


    /**
     * Add a player to the list of players
     * @param player
     * @return the list of players
     */
    public ArrayList<Player> addPlayer(Player player){
        PLAYERS.add(player);
        return PLAYERS;
    }

}
