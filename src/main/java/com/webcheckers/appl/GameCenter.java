package com.webcheckers.appl;

import com.webcheckers.model.Match;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GameCenter {
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Match> gameList = new ArrayList<>();

    public GameCenter(ArrayList<Player> players) {
        this.players =players;
    }

    /**
     * Add a player to the list of players
     * @param player
     * @return the list of players
     */
    public ArrayList<Player> addPlayer(Player player){
        players.add(player);
        return players;
    }

    /**
     * Get the list of the players
     * @return the list of players
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

    public Match getMatch(String player1, String player2) {
        Match match = new Match(player1, player2);
        gameList.add(match);
        return match;
    }

    public void endMatch(Match match){
        gameList.remove(match);
    }
    public PlayerServices newPlayerServices(){
        LOG.fine("New player services instance created.");
        return new PlayerServices(this);
    }

}
