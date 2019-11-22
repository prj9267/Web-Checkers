package com.webcheckers.model;

import java.util.*;

public class Leaderboard {
    private ArrayList<Player> list;
    private TreeSet<Player> gamesBoard;
    private TreeSet<Player> wonBoard;
    private TreeSet<Player> lostBoard;
    private TreeSet<Player> piecesTakenBoard;
    private TreeSet<Player> piecesLostBoard;
    private CSVutility csvutility;

    public Leaderboard() {
        csvutility = new CSVutility();
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
        piecesTakenBoard = new TreeSet<>(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getPiecesTaken() > o2.getPiecesTaken()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        piecesLostBoard = new TreeSet<>(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getPiecesLost() > o2.getPiecesLost()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
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
     * Return the pieces taken leaderboard (ranks by number of opponent pieces taken)
     * @return piecesTakenBaoard leaderboard
     */
    public TreeSet<Player> getPiecesTakenBoard() {
        return piecesTakenBoard;
    }

    /**
     * Return the pieces lost leaderboard (ranks by number of pieces lost)
     * @return piecesLostBoard leaderboard
     */
    public TreeSet<Player> getPiecesLostBoard() {
        return piecesLostBoard;
    }

    /**
     * Updates the boards.
     */
    public synchronized void updateAllBoards() {
        list = csvutility.readPlayers();
        for (Player player : list) {
            gamesBoard.add(player);
            wonBoard.add(player);
            lostBoard.add(player);
            piecesTakenBoard.add(player);
            piecesLostBoard.add(player);
        }
    }
}
