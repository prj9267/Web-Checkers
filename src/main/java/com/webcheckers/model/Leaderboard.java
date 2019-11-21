package com.webcheckers.model;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.*;

import static com.webcheckers.ui.WebServer.csvFile;

public class Leaderboard {
    private ArrayList<Player> list;
    private TreeSet<Player> gamesBoard;
    private TreeSet<Player> wonBoard;
    private TreeSet<Player> lostBoard;

    public Leaderboard() {
        list = new ArrayList<>();
        gamesBoard = new TreeSet<>(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getGames() > o2.getGames()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        wonBoard = new TreeSet<>(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getWon() > o2.getWon()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        lostBoard = new TreeSet<>(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getLost() > o2.getLost()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    /**
     * Update list of all stored players with their number of games played.
     */
    public void updateList() {
        try {
            FileReader fileReader = new FileReader(csvFile);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                String username = nextRecord[0];
                int games = Integer.parseInt(nextRecord[1]);
                int won = Integer.parseInt(nextRecord[2]);
                int lost = Integer.parseInt(nextRecord[3]);

                list.add(new Player(username, games, won, lost));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // #TODO debug
        //for (Player player: list)
        //    System.out.println(player.getName() + " Number of games: " + Integer.toString(player.getGames()));
    }

    /**
     * Return the won leaderboard (ranks by number of wins)
     * @return board won leaderboard
     */
    public TreeSet<Player> getWonBoard() {
        return wonBoard;
    }

    /**
     * Return the lost leaderboard (ranks by number of losses)
     * @return lostBoard lost leaderboard
     */
    public TreeSet<Player> getLostBoard() {
        return lostBoard;
    }

    /**
     * Return the games leaderboard (ranks by number of gamse
     * @return gamesBoard leaderboard
     */
    public TreeSet<Player> getGamesBoard() {
        return gamesBoard;
    }

    /**
     * Updates the boards.
     */
    public synchronized void updateAllBoards() {
        updateList();
        for (Player player : list) {
            gamesBoard.add(player);
            wonBoard.add(player);
            lostBoard.add(player);
        }
    }
}
