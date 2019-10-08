package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.ArrayList;

public class GameCenter {

    private ArrayList<Player> players = new ArrayList<>();

    public GameCenter(ArrayList<Player> players) {
        this.players =players;
    }

    /**
     * Add a player to the list of players
     * @param player
     * @return the list of players
     */
    public ArrayList<Player> addPlayer(Player player){
        players.add(player);
        return players;
    }

    /**
     * Get the list of the players
     * @return the list of players
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

}
