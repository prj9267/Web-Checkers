package com.webcheckers.appl;

import com.webcheckers.model.Match;
import com.webcheckers.model.Player;

import java.util.HashMap;
import java.util.Map;


public class PlayerServices {

    //Attributes
    private Match match;
    //playerList holding the names of players
    private static Map<String, Player> playerList;

    /**
     * Constructor for PlayerServices Object
     */
    public PlayerServices(){
        match = null;
        playerList = new HashMap<>();
    }

    /**
     * End the current match;
     */
    public void endSession() {
        match = null;
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

    /**
     * Checks if the username is taken.
     * @param username  - the user
     * @return          - true if not taken and false if taken
     */
    public boolean isTaken(String username) {
        return !playerList.containsKey(username);
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
     * Adds the username and creates a new Player if the username is valid.
     * @param username  - the user
     * @return          - true if the player has been added and false if not
     */
    public synchronized boolean confirmation(String username) {
        if(isValid(username)) {
            playerList.put(username, new Player(username));
            return true;
        }
        return false;
    }

    /**
     * Get the player
     * @param username  - the username of the player
     * @return          - the player
     */
    public Player getPlayer(String username) {
        return playerList.get(username);
    }

    public int numPlayers() {
        return playerList.size();
    }
}
