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
    public enum boardState {games, won, lost, piecesTaken, piecesLost}
    private boardState state;

    public Leaderboard() {
        csvutility = new CSVutility();
        list = new ArrayList<>();
        state = boardState.games;
        // order by games played
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
        // order by games won
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
        // order by games lost
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
        // order by opponent pieces taken
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
        // order by friendly pieces lost
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
     * Change the state of the leaderboard so that different order is displayed.
     * @param state state of the leaderboard (games, won, lost, piecesTaken, piecesLost)
     */
    public void changeState(boardState state) {
        this.state = state;
    }

    /**
     * Get the state of the leaderboard so that different order is displayed.
     * @return
     */
    public boardState getState() {
        return state;
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
        // add them to TreeSets with custom comparators
        for (Player player : list) {
            gamesBoard.add(player);
            wonBoard.add(player);
            lostBoard.add(player);
            piecesTakenBoard.add(player);
            piecesLostBoard.add(player);
        }
    }

    // TODO test
    public ArrayList<String> getNamesOnly(TreeSet<Player> board) {
        ArrayList<String> names = new ArrayList<>();
        for (Player player: board) {
            names.add(player.getName());
        }
        return names;
    }
    public ArrayList<Integer> getGamesOnly(TreeSet<Player> board) {
        ArrayList<Integer> games = new ArrayList<>();
        for (Player player: board) {
            games.add(player.getGames());
        }
        return games;
    }
    public ArrayList<Integer> getWonOnly(TreeSet<Player> board) {
        ArrayList<Integer> won = new ArrayList<>();
        for (Player player: board) {
            won.add(player.getWon());
        }
        return won;
    }
    public ArrayList<Integer> getLostOnly(TreeSet<Player> board) {
        ArrayList<Integer> lost = new ArrayList<>();
        for (Player player: board) {
            lost.add(player.getLost());
        }
        return lost;
    }
    public ArrayList<Integer> getPiecesTakenOnly(TreeSet<Player> board) {
        ArrayList<Integer> piecesTaken = new ArrayList<>();
        for (Player player: board) {
            piecesTaken.add(player.getPiecesTaken());
        }
        return piecesTaken;
    }
    public ArrayList<Integer> getPiecesLostOnly(TreeSet<Player> board) {
        ArrayList<Integer> piecesLost = new ArrayList<>();
        for (Player player: board) {
            piecesLost.add(player.getPiecesLost());
        }
        return piecesLost;
    }
}
