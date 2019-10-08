package com.webcheckers.appl;

import com.webcheckers.model.Match;


public class PlayerServices {

    private Match match;
    private final GameCenter gameCenter;

    public PlayerServices(GameCenter gameCenter){
        match = null;
        this.gameCenter = gameCenter;
    }

    /*public synchronized Match currentMatch(){
        if(match == null){
            match = gameCenter.
        }
    }*/

    public void endSession(){ match = null; }
}
