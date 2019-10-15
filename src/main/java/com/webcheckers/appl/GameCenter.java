package com.webcheckers.appl;

import com.webcheckers.model.Match;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.logging.Logger;

public class GameCenter {
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());

    //Attributes
    private ArrayList<String> players = new ArrayList<>();
    private ArrayList<Match> gameList = new ArrayList<>();

    /**
     * Constructor for GameCenter Object
     * @param players   - the list of players signed into the web app.
     */
    public GameCenter(ArrayList<String> players) {
        this.players = players;
    }

    /**
     * Add a player to the list of players
     * @param player
     * @return the list of players
     */
    public ArrayList<String> addPlayer(String player){
        players.add(player);
        return players;
    }

    /**
     * Get the list of the players
     * @return the list of players
     */
    public ArrayList<String> getPlayers(){
        ArrayList<String> ret = new ArrayList<>();
        ret.addAll(players);
        return ret;
    }

    public boolean removePlayer(String player){
        return players.remove(player);
    }

    /**
     * Create a new match
     * @param player1   - the user
     * @param player2   - their opponent
     * @return          - the newly created match object
     */
    public Match getMatch(String player1, String player2) {
        Match match = new Match(player1, player2);
        gameList.add(match);
        return match;
    }

    /**
     * Removes the match from the current gameList.
     * @param match - the match to be removed.
     */
    public void endMatch(Match match){
        gameList.remove(match);
    }

    public PlayerServices newPlayerServices(){
        LOG.fine("New player services instance created.");
        return new PlayerServices(this);
    }

}
