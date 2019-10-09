package com.webcheckers.appl;

import com.webcheckers.model.Match;


public class PlayerServices {

    private Match match;
    private final GameCenter gameCenter;

    public PlayerServices(GameCenter gameCenter){
        match = null;
        this.gameCenter = gameCenter;
    }

    public void endSession(){ match = null; }

    public Match getGameCenter(String player1, String player2){
        return gameCenter.getMatch(player1, player2);
    }
}
