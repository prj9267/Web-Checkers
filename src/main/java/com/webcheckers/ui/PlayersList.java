package com.webcheckers.ui;

import java.util.ArrayList;
import java.util.List;

//TODO NOT CURRENTLY IN USE FOR SPRINT 1
public class PlayersList {
    // name of the current user
    private String username;
    // name of the opponent user
    private String o_username;
    private static ArrayList<String> playerList = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public String getOpponentUsername() {
        return o_username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static ArrayList<String> getPlayerList() {
        return playerList;
    }

    public boolean addUsername(String username) {
        return playerList.add(username);
    }
}
