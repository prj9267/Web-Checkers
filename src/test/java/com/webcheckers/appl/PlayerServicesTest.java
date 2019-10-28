package com.webcheckers.appl;


import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;


@Tag("Application-tier")
public class PlayerServicesTest {
    private ArrayList<Player> playerList = new ArrayList<>();
    private Player playerOne = new Player("One");
    private Player playerTwo = new Player("Two");
    private PlayerServices CuT;

    @BeforeEach
    public void setup() {
        CuT = new PlayerServices();
    }

    @Test
    public void checkGetPlayerList() {
        playerList.add(playerOne);

        CuT.addPlayer(playerOne);

        assertEquals(playerList, CuT.getPlayerList(), "PLayerList not updated properly");
    }

    @Test
    public void checkAddPlayer() {
        playerList.add(playerOne);

        CuT.addPlayer(playerOne);

        assertEquals(playerList, CuT.getPlayerList(), "Player not added to list properly");

    }


    @Test
    public void checkPlayerService(){
        PlayerServices playerSrv = new PlayerServices();

        assertEquals(playerSrv.getPlayerList(), CuT.getPlayerList(), "PlayerServices Built incorrectly");
    }
}
