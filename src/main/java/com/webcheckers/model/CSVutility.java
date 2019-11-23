package com.webcheckers.model;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import static com.webcheckers.ui.WebServer.csvFile;

public class CSVutility {

    /**
     * Edits the player records in case of an extra win or loss.
     * @param player player to change records of
     */
    public synchronized void editPlayerRecords(Player player) {
        try {
            FileReader fileReader = new FileReader(csvFile);
            CSVReader csvReader = new CSVReader(fileReader);
            // read file
            List<String[]> lines = csvReader.readAll();
            int i = 0;
            String[] input = new String[6];
            for (String[] line : lines) {
                for (String str : line)
                    System.out.print(str + " ");
                if (line[0].equals(player.getName())) {
                    input[0] = player.getName();
                    input[1] = Integer.toString(player.getGames());
                    input[2] = Integer.toString(player.getWon());
                    input[3] = Integer.toString(player.getLost());
                    input[4] = Integer.toString(player.getPiecesTaken());
                    input[5] = Integer.toString(player.getPiecesLost());
                    lines.remove(i);
                    lines.add(input);
                    break;
                }
                i++;
            }
            csvReader.close();
            // write to file
            FileWriter fileWriter = new FileWriter(csvFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            csvWriter.writeAll(lines);
            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a new player to the CSV file that holds all player records.
     * @param username name of player to store
     */
    public synchronized void addPlayerToCSV(String username) {
        try {
            // write to the csv file so that the new player data can be found next time
            FileWriter fileWriter = new FileWriter(csvFile, true);
            CSVWriter csvWriter = new CSVWriter(fileWriter);
            String[] stats = {username, "0", "0", "0", "0", "0"};
            csvWriter.writeNext(stats);
            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Find the player's stat and return a new player with those attributes.
     * @param username name of the player to find
     * @return player
     */
    public Player findPlayer(String username) {
        boolean found = false;
        int games = 0;
        int won = 0;
        int lost = 0;
        int piecesTaken = 0;
        int piecesLost = 0;

        Player player;
        try {
            FileReader fileReader = new FileReader(csvFile);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;
            // find the username, if found get the attributes games, won, lost
            while ((nextRecord = csvReader.readNext()) != null) {
                int i = 0;
                for (String stat : nextRecord) {
                    if (stat.equals(username)) {
                        found = true;
                    } else {
                        break;
                    }
                    if (found) {
                        switch (i) {
                            case 1:
                                games = Integer.parseInt(stat);
                            case 2:
                                won = Integer.parseInt(stat);
                            case 3:
                                lost = Integer.parseInt(stat);
                            case 4:
                                piecesTaken = Integer.parseInt(stat);
                            case 5:
                                piecesLost = Integer.parseInt(stat);
                        }
                        i++;
                    }
                }
                if (found)
                    break;
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // if the player is not found in the csv file, add them to the csv file
        if (!found) {
            player = new Player(username);
            addPlayerToCSV(username);
        } else {
            // create a new player with found attributes
            player = new Player(username, games, won, lost, piecesTaken, piecesLost);
        }
        System.out.println(player.getName() + " found: " + Boolean.toString(found));
        return player;
    }

    /**
     * Read through the CSV file and add a player from every row to an array list
     * @return arraylist with all existing players offline or online
     */
    public synchronized ArrayList<Player> readPlayers() {
        ArrayList<Player> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(csvFile);
            CSVReader csvReader = new CSVReader(fileReader);
            String[] nextRecord;
            // grab the attributes of each player and add them to a list so that we can sort them and put
            // them into a leaderboard via TreeSets
            while ((nextRecord = csvReader.readNext()) != null) {
                String username = nextRecord[0];
                int games = Integer.parseInt(nextRecord[1]);
                int won = Integer.parseInt(nextRecord[2]);
                int lost = Integer.parseInt(nextRecord[3]);
                int piecesTaken = Integer.parseInt(nextRecord[4]);
                int piecesLost = Integer.parseInt(nextRecord[5]);
                list.add(new Player(username, games, won, lost, piecesTaken, piecesLost));
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
