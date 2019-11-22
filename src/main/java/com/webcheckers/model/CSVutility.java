package com.webcheckers.model;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import static com.webcheckers.ui.WebServer.csvFile;

public class CSVutility {
    private FileWriter fileWriter;
    private FileReader fileReader;
    private CSVWriter csvWriter;
    private CSVReader csvReader;

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

        Player player;

        try {
            //C:\Programming\Intro to Software Engineering\WebCheckers\src\main\resources\public\Statistics.csv
            fileReader = new FileReader(csvFile);
            csvReader = new CSVReader(fileReader);
            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null) {
                int i = 0;
                for (String stat : nextRecord) {
                    if (stat.equals(username))
                        found = true;
                    if (found) {
                        switch (i) {
                            case 1:
                                games = Integer.parseInt(stat);
                            case 2:
                                won = Integer.parseInt(stat);
                            case 3:
                                lost = Integer.parseInt(stat);
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

        // TODO test
        //playerServices.addPlayer(new Player("validusername", 555, 333, 222));
        //editCSV("validusername");


        if (!found) {
            player = new Player(username);
            try {
                // write to the csv file so that the new player data can be found next time
                fileWriter = new FileWriter(csvFile, true);
                csvWriter = new CSVWriter(fileWriter);
                String[] stats = {username, "0", "0", "0"};
                csvWriter.writeNext(stats);
                csvWriter.flush();
                csvWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            player = new Player(username, games, won, lost);
        }
        return player;
    }

    /**
     * Read through the CSV file and add a player from every row to an array list
     * @return arraylist with all existing players offline or online
     */
    public ArrayList<Player> addPlayers() {
        ArrayList<Player> list = new ArrayList<>();
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
        return list;
    }
}
