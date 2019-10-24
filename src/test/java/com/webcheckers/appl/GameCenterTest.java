package com.webcheckers.appl;

import com.webcheckers.model.Match;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
public class GameCenterTest {
//    private PlayerServices playerSrv;
    private GameCenter CuT;
    private HashMap<Player, Match> inMatch = new HashMap<>();
    private ArrayList<Match> gameList = new ArrayList<>();
    private Player playerOne = new Player("One");
    private Player playerTwo = new Player("Two");
    private PlayerServices playerSrv = new PlayerServices();

    @BeforeEach
    public void setup() {
//        playerSrv = mock(PlayerServices.class);
        CuT = new GameCenter(playerSrv);
    }

    @Test
    void checkMatchAdded() {
        assertTrue(CuT.addMatch(playerOne, playerTwo), "Did not add match properly");
    }

    @Test
    void checkOpponentGotten() {
        assertSame(null, CuT.getOpponent("Two"), "Game is not null");

        CuT.addMatch(playerOne, playerTwo);
        playerSrv.addPlayer(playerOne);
        playerSrv.addPlayer(playerTwo);

        assertEquals("Two", CuT.getOpponent("One"), "One's opponent is not Two");
        assertEquals("One", CuT.getOpponent("Two"), "Two's opponent is not One");

    }

    @Test
    void checkIsInMatch() {
        CuT.addMatch(playerOne, playerTwo);
        assertTrue(CuT.isInMatch(playerOne), "PlayerOne not in match");
    }

    @Test
    void checkIsCurrent() {
        CuT.addMatch(playerOne, playerTwo);
        assertTrue(CuT.isCurrent(playerOne), "Player is not the current Player");
    }

//    @Test
    void checkGetMatch() {
        Match match = new Match(playerOne, playerTwo);
        inMatch.put(playerOne, match);
        inMatch.put(playerTwo, match);
        playerSrv.addPlayer(playerOne);
        playerSrv.addPlayer(playerTwo);

        CuT.addMatch(playerOne, playerTwo);
        assertEquals(match, CuT.getMatch(playerOne), "Player is not in current match");
    }

    @Test
    void checkRemovePlayer() {
        CuT.addMatch(playerOne, playerTwo);
        assertTrue(CuT.isInMatch(playerOne), "PlayerOne not in match");
        assertTrue(CuT.isInMatch(playerTwo), "PlayerTwo not in match");

        CuT.removePlayer(playerOne);
        assertFalse(CuT.isInMatch(playerOne), "PlayerOne still in match");
        CuT.removePlayer(playerTwo);
        assertFalse(CuT.isInMatch(playerTwo), "PlayerTwo still in match");
    }
}
