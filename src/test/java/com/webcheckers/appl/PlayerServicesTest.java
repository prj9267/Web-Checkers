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
    void setup() {
        CuT = new PlayerServices();
    }

    @Test
    void checkGetPlayerList() {
        playerList.add(playerOne);
        CuT.addPlayer(playerOne);
        assertEquals(playerList, CuT.getPlayerList(), "PLayerList not updated properly");
    }

    @Test
    void checkAddPlayer() {
        playerList.add(playerOne);
        CuT.addPlayer(playerOne);
        assertEquals(playerList, CuT.getPlayerList(), "PlayerOne not added to list properly");
        playerList.add(playerTwo);
        CuT.addPlayer(playerTwo);
        assertEquals(playerList, CuT.getPlayerList(), "PlayerTwo not added to list properly");
    }

    @Test
    void checkPlayerService(){
        PlayerServices playerSrv = new PlayerServices();
        assertEquals(playerSrv.getPlayerList(), CuT.getPlayerList(), "PlayerServices Built incorrectly");
    }

    @Test
    void checkGetPlayer() {
        CuT.addPlayer(playerOne);
        assertEquals(playerOne, CuT.getPlayer("One"), "PlayerOne not gotten correctly");
        assertNull(CuT.getPlayer("Two"), "PlayerTwo found but not added");
    }

    @Test
    void checkRemovePlayers() {
        CuT.addPlayer(playerOne);
        CuT.addPlayer(playerTwo);
        playerList.add(playerOne);
        playerList.add(playerTwo);
        assertEquals(playerList, CuT.getPlayerList(), "Player list not constructed correctly");

        CuT.removePlayer(playerOne);
        playerList.remove(playerOne);
        assertEquals(playerList, CuT.getPlayerList(), "PlayerOne not removed correctly");

        CuT.removePlayer(playerTwo);
        assertNotEquals(playerList, CuT.getPlayerList(), "PlayerTwo still in player list");
    }

    @Test
    void checkNumPlayers() {
        assertEquals(0, CuT.numPlayers(), "Player list is not size 0");
        CuT.addPlayer(playerOne);
        assertEquals(1, CuT.numPlayers(), "Player list is not size 1");
        CuT.addPlayer(playerTwo);
        assertEquals(2, CuT.numPlayers(), "Player list is not size 2");
        CuT.removePlayer(playerTwo);
        assertEquals(1, CuT.numPlayers(), "Player list is not size 1");
        CuT.removePlayer(playerOne);
        assertEquals(0, CuT.numPlayers(), "Player list is not size 0");
    }

    @Test
    void checkIsAvailable() {
        CuT.addPlayer(playerOne);
        assertTrue(CuT.isAvailable(playerOne), "PlayerOne should be available");

        assertFalse(CuT.isAvailable(playerTwo), "PlayerTwo should not be available");
    }
}
