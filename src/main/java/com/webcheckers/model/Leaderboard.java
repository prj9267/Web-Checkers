package com.webcheckers.model;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;

import static com.webcheckers.ui.WebServer.csvFile;

public class Leaderboard {
    private HashMap<String, int[]> list;
    private HashMap<String, Integer> gamesBoard;
    private HashMap<String, Integer> wonBoard;
    private HashMap<String, Integer> lostBoard;
    private FileReader fileReader;
    private CSVReader csvReader;
    private String[] nextRecord;

    public Leaderboard() {
        list = new HashMap<>();
        gamesBoard = new HashMap<>();
        wonBoard = new HashMap<>();
        lostBoard = new HashMap<>();

        try {
            //C:\Programming\Intro to Software Engineering\WebCheckers\src\main\resources\public\Statistics.csv
            FileReader fileReader = new FileReader(csvFile);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update list of all stored players with their number of games played.
     */
    public void updateList() {
        try {
            while ((nextRecord = csvReader.readNext()) != null) {
                int i = 0;
                String username = "";
                int[] record = new int[3];
                for (String stat : nextRecord) {
                    switch (i) {
                        case 0:
                            username = stat;
                        case 1:
                            record[0] = Integer.parseInt(stat);
                        case 2:
                            record[1] = Integer.parseInt(stat);
                        case 3:
                            record[2] = Integer.parseInt(stat);
                            break;
                    }
                }
                list.put(username, record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the won leaderboard (ranks by number of wins)
     * @return board won leaderboard
     */
    public HashMap<String, Integer> getWonBoard() {
        return wonBoard;
    }

    /**
     * Return the lost leaderboard (ranks by number of losses)
     * @return lostBoard lost leaderboard
     */
    public HashMap<String, Integer> getLostBoard() {
        return lostBoard;
    }

    /**
     * Return the games leaderboard (ranks by number of gamse
     * @return gamesBoard leaderboard
     */
    public HashMap<String, Integer> getGamesBoard() {
        return gamesBoard;
    }

    /**
     * Updates the boards. TODO SORTING ALGO
     */
    public void updateAllBoards() {
        updateList();
        //TODO IDEA:
        //TODO have a hashmap<String, Integer> for each board and sort through everything and list it
        //TODO in the homepage with top 10 appearing with nums but can scroll down. On a different section
        //TODO maybe below the overflow scroll section,
        //TODO list the player rank.
        for (int i = 0; i < 3; i++) {
            for (String name : list.keySet()) {
                switch (i) {
                    case 0:
                        gamesBoard.put(name, list.get(name)[0]);
                    case 1:
                        wonBoard.put(name, list.get(name)[1]);
                    case 2:
                        lostBoard.put(name, list.get(name)[2]);
                        break;
                }
            }
        }
        // TODO sort all three boards
    }
}
