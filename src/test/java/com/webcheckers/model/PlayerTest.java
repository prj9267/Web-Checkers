package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
}