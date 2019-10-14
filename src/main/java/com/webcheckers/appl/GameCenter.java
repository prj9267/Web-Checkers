package com.webcheckers.appl;

import com.webcheckers.model.Board;
import com.webcheckers.model.Match;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GameCenter {
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

    //Attributes
    private PlayerServices playerServices;
    private Map<Player, Match> inMatch;
    //private ArrayList<Match> gameList = new ArrayList<>();

    /**
     * Constructor for GameCenter Object
     * @param playerServices   - the lobby of players
     */
    public GameCenter(PlayerServices playerServices) {
        this.inMatch = new HashMap<>();
        this.playerServices = playerServices;
    }

    /**
     * adds a match if players are fighting legitimate players
     * @param redPlayerName     - name of the red player
     * @param whitePlayerName   - name of the white player
     * @return                  - true if match has been added
     */
    public synchronized boolean addMatch(String redPlayerName, String whitePlayerName) {
        Player redPlayer = playerServices.getPlayer(redPlayerName);
        Player whitePlayer = playerServices.getPlayer(whitePlayerName);
        if(redPlayer.equals(whitePlayer) || inMatch.containsKey(redPlayer) || inMatch.containsKey(whitePlayer))
            return false;
        Match match = new Match(redPlayer, whitePlayer);
        inMatch.put(redPlayer, match);
        inMatch.put(whitePlayer, match);
        return true;
    }

    /**
     * Checks if the provided player is the current player
     * @param player            - the player to check
     * @return                  - true if the player is the current player
     */
    public boolean isCurrent(Player player) {
        return inMatch.get(player).getCurrentPlayer().equals(player);
    }

    /**
     * Checks if the provided player is in a match
     * @param player            - the player to check
     * @return                  - true if the player is in a match
     */
    public boolean isInMatch(Player player) {
        return inMatch.containsKey(player);
    }

    /**
     * Gets a match that a player is in
     * @param player            - the player
     * @return                  - the match
     */
    public synchronized Match getMatch(Player player) {
        return inMatch.get(player);
    }

    /**
     * Removes a player from a game
     * @param player            - the player to remove
     * @return                  - true if the player has been removed
     */
    public synchronized boolean removePlayer(Player player) {
        if(!isInMatch(player))
            return false;
        inMatch.remove(player);
        return true;
    }

    public PlayerServices newPlayerServices(){
        LOG.fine("New player services instance created.");
        return new PlayerServices(this);
    }


}
