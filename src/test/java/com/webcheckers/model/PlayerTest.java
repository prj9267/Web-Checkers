package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.MissingFormatArgumentException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class PlayerTest {
    private String name = "User";
    private Player player = new Player(name);

    @Test
    public void checkEquality() {
        Player player2 = new Player(name);
        assertTrue(player.equals(player2), "Players are not equal");
    }

    @Test
    public void checkSelf() {
        assertTrue(player.equals(player));
    }

    @Test
    public void checkHash() {
        Player player2 = new Player(name);
        assertEquals(player.getName().hashCode(), player2.getName().hashCode(), "Hashcode are not equal.");
    }

    @Test
    public void constructorNullArg() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Player(null); }, "Player with null name allowed.");
    }

    @Test
    public void testInvalidName() {
        Player player2 = new Player("!TrackStaR***美国");
        assertFalse(player2.containsInvalidCharacter(), "Invalid characters are accepted.");
    }
}