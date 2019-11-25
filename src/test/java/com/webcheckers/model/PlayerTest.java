package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class PlayerTest {
    private String name = "User";
    private String name2 = "User2";
    private Player player = new Player(name);

    @Test
    public void checkEquality() {
        Player player2 = new Player(name);
        assertTrue(player.equals(player2), "Players are not equal");
    }

    @Test
    public void checkSelf() {
        assertTrue(player.equals(player), "Player is somehow not itself?");
    }

    @Test
    public void checkHash() {
        Player player2 = new Player(name);
        assertEquals(player.getName().hashCode(), player2.getName().hashCode(), "Hashcode are not equal.");
    }

    @Test
    public void constructorNullArg() {
        assertThrows(AssertionError.class, () -> {
            new Player(null); }, "Player with null name allowed.");
    }

    @Test
    public void testInvalidName() {
        Player player2 = new Player("!TrackStaR***美国");
        assertTrue(player2.containsInvalidCharacter(), "Invalid characters are accepted.");
    }

    @Test
    public void changeStatus() {
        Player player2 = new Player(name2);
        player2.changeStatus(Player.Status.ingame);
        assertEquals(player2.getStatus(), Player.Status.ingame, "Player status wasn't changed.");
    }

    @Test
    public void checkInGame() {
        Player player2 = new Player(name2);
        player2.changeStatus(Player.Status.ingame);
        assertTrue(player2.isInGame(), "Player is not considered in game when they should be.");
    }

    @Test
    public void checkAddWon() {
        Player player2 = new Player(name2);
        player2.addWon();
        player.addWon();
        assertEquals(player.getWon(), player2.getWon(), "Win not added correctly");
    }

    @Test
    public void checkAddLost() {
        Player player2 = new Player(name2);
        player2.addLost();
        player.addLost();
        assertEquals(player.getLost(), player2.getLost(), "Win not added correctly");
    }
}