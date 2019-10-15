package com.webcheckers.appl;

import com.webcheckers.model.Match;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class PlayerServices {

    //Attributes
    //private Match match;
    //playerList holding the names of players
    private ArrayList<Player> playerList;

    /**
     * Constructor for PlayerServices Object
     */
    public PlayerServices(){
        playerList = new ArrayList<>();
    }

    /**
     * End the current match;
     */
    public void endSession() {
        //match = null;
    }

    /**
     * Creates a new match between players.
     * @param player1   - the user
     * @param player2   - the opponent
     * @return          - the created match
     */
    /*public Match getMatch(String player1, String player2){
        return gameCenter.getMatch(player1, player2);
    }*/

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    /**
     * Checks if the username is taken.
     * @param username  - the user
     * @return          - true if not taken and false if taken
     */
    public boolean isTaken(String username) {
        for(Player player: playerList) {
            if(player.getName() == username) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the registered name contains only alphanumeric characters.
     * @param username  - the user
     * @return          - true if name is valid and false if not
     */
    public boolean isAlphanumeric(String username) {
        return username.matches("[a-zA-Z_0-9]+([a-zA-Z_0-9]|\\s)*$");
    }

    /**
     * Checks if the username is valid
     * @param username  - the user
     * @return          - true if valid and false if invalid
     */
    public boolean isValid(String username) {
        return isAlphanumeric(username) && isTaken(username);
    }

    /**
     * Get the player
     * @param username  - the username of the player
     * @return          - the player
     */
    public Player getPlayer(String username) {
        for (Player player : playerList) {
            if (player.getName().equals(username)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Gets the number of players
     * @return          - number int of players
     */
    public int numPlayers() {
        return playerList.size();
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

}
