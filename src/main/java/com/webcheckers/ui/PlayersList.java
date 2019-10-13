package com.webcheckers.ui;

import java.util.ArrayList;
import java.util.List;

//TODO NOT CURRENTLY IN USE FOR SPRINT 1
public class PlayersList {
    private static ArrayList<String> playerList = new ArrayList<>();

    public static ArrayList<String> getPlayerList() {
        return playerList;
    }

    public boolean addUsername(String username) {
        return playerList.add(username);
    }
}
