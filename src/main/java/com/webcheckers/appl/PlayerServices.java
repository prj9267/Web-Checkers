package com.webcheckers.appl;

import com.webcheckers.model.Match;


public class PlayerServices {

    //Attributes
    private Match match;
    private final GameCenter gameCenter;

    /**
     * Constructor for PlayerServices Object
     * @param gameCenter    - the about to be running gameCenter
     */
    public PlayerServices(GameCenter gameCenter){
        match = null;
        this.gameCenter = gameCenter;
    }

    /**
     * End the current match;
     */
    public void endSession(){ match = null; }

    /**
     * Creates a new match between players.
     * @param player1   - the user
     * @param player2   - the opponent
     * @return          - the created match
     */
    public Match getMatch(String player1, String player2){
        return gameCenter.getMatch(player1, player2);
    }
}
