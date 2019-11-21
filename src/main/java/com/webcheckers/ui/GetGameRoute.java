package com.webcheckers.ui;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Match;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.webcheckers.ui.WebServer.csvFile;
import static spark.Spark.halt;

public class GetGameRoute implements Route {
    // Values used in the view-model map for rendering the game view.

    public static final String TITLE = "Game";
    public static final String VIEW_NAME = "game.ftl";
    public static final String CURRENT_USER_ATTR = "currentUser";
    public static final String VIEW_MODE_ATTR = "viewMode";
    public static final String MODE_OPTION_ATTR = "modeOption";
    public static final String RED_PLAYER_ATTR = "redPlayer";
    public static final String WHITE_PLAYER_ATTR = "whitePlayer";
    public static final String ACTIVE_COLOR_ATTR = "activeColor";
    public static final String BOARD_ATTR = "board";
    public enum viewMode {PLAY, SPECTATOR, REPLAY}

    public static final String MATCH_ATTR = "match";

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final PlayerServices playerServices;

    private Gson gson;

    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public GetGameRoute(final PlayerServices playerServices,
                        final GameCenter gameCenter,
                        final TemplateEngine templateEngine){
        Objects.requireNonNull(playerServices, "playerServices must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerServices = playerServices;
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
    }

    /**
     * edits the CSV file to alter the player's stats
     * @param name name of the player to change the stats of
     */
    public synchronized void editCSV(String name) {
        try {
            FileReader fileReader = new FileReader(csvFile);
            CSVReader csvReader = new CSVReader(fileReader);

            FileWriter fileWriter = new FileWriter(csvFile);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            //TODO read to position i
            //TODO write same stuff up to i which then edit line i with new statistics
            String nextRecord[];
            int line = 0;
            // get the line number in the csv for the player
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord[0].equals(name)) {
                    Player thePlayer = playerServices.getPlayer(name);
                    String[] input = new String[4];
                    input[0] = name;
                    input[1] = Integer.toString(thePlayer.getGames());
                    input[2] = Integer.toString(thePlayer.getWon());
                    input[3] = Integer.toString(thePlayer.getLost());
                    csvWriter.writeNext(input);
                    break;
                }
                csvWriter.writeNext(nextRecord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response){
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        if(playerServices != null) {
            final Map<String, Object> vm = new HashMap<>();
            vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);

            vm.put(CURRENT_USER_ATTR, currentPlayer);

            Player redPlayer;
            Player whitePlayer;
            Match currentMatch;
            // if the button is just triggered, initialize everything
            if (request.queryParams("button") != null) {
                String opponentName = request.queryParams("button");
                redPlayer = currentPlayer;
                whitePlayer = playerServices.getPlayer(opponentName);
                // if either player is not available
                if (! gameCenter.addMatch(redPlayer, whitePlayer)){
                    httpSession.attribute("message", Message.error(whitePlayer.getName() + " is already in game!"));
                    response.redirect(WebServer.HOME_URL);
                    halt();
                    return null;
                }
                currentMatch = gameCenter.getMatch(redPlayer);
            } else { // else get the information from the match
                currentMatch = gameCenter.getMatch(currentPlayer);
                redPlayer = currentMatch.getRedPlayer();
                whitePlayer = currentMatch.getWhitePlayer();
            }

            // save it to session
            httpSession.attribute(MATCH_ATTR, currentMatch);
            httpSession.attribute(RED_PLAYER_ATTR, redPlayer);
            httpSession.attribute(WHITE_PLAYER_ATTR, whitePlayer);

            // send players to ftl
            vm.put(RED_PLAYER_ATTR, redPlayer);
            vm.put(WHITE_PLAYER_ATTR, whitePlayer);

            //Match match = gameCenter.getMatch(currentPlayer);
            BoardView whiteBoardView = currentMatch.getWhiteBoardView();
            BoardView redBoardView = currentMatch.getRedBoardView();

            if(currentPlayerName.equals(redPlayer.getName())) {
                vm.put(BOARD_ATTR, redBoardView);
                vm.put(CURRENT_USER_ATTR, redPlayer);
            } else {
                vm.put(BOARD_ATTR, whiteBoardView);
                vm.put(CURRENT_USER_ATTR, whitePlayer);
            }

            // for the nav-bar to display the signout option
            vm.put(GetHomeRoute.CURRENT_PLAYER_ATTR, currentPlayerName);

            vm.put(ACTIVE_COLOR_ATTR, currentMatch.getActiveColor());
            // right now there is only the option to play
            viewMode currentViewMode = viewMode.PLAY;
            vm.put(VIEW_MODE_ATTR, currentViewMode);

            if (currentMatch.isGameResigned() == Match.STATE.resigned) {
                // remove the player from the ingame list after exiting the game
                currentPlayer.changeRecentlyInGame(true);
                // update stats
                currentPlayer.addWon();
                Gson gson = new Gson();
                Map <String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", true);
                // produce the right message
                if (currentPlayer.equals(redPlayer)) {
                    modeOptions.put("gameOverMessage", whitePlayer.getName() + " has resigned. You won!");
                } else if (currentPlayer.equals(whitePlayer)) {
                    modeOptions.put("gameOverMessage", redPlayer.getName() + " has resigned. You won!");
                }
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                currentPlayer.changeStatus(Player.Status.waiting);
            } else if (currentMatch.getRedPieces().size() == 0) {
                // remove the player from the ingame list after exiting the game
                currentPlayer.changeRecentlyInGame(true);
                // update stats
                if (currentPlayer.equals(currentMatch.getRedPlayer())) {
                    currentPlayer.addLost();
                } else {
                    currentPlayer.addWon();
                }
                Gson gson = new Gson();
                Map <String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", true);
                modeOptions.put("gameOverMessage", whitePlayer.getName() + " has captured all opponent pieces!");
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                //gameCenter.removePlayer(currentPlayer);
                currentPlayer.changeStatus(Player.Status.waiting);
                //gameCenter.removeMatch(currentMatch);
            } else if (currentMatch.getWhitePieces().size() == 0) {
                // remove the player from the ingame list after exiting the game
                currentPlayer.changeRecentlyInGame(true);
                // update stats
                if (currentPlayer.equals(currentMatch.getRedPlayer())) {
                    currentPlayer.addWon();
                } else {
                    currentPlayer.addLost();
                }
                Gson gson = new Gson();
                Map <String, Object> modeOptions = new HashMap<>(2);
                modeOptions.put("isGameOver", true);
                modeOptions.put("gameOverMessage", redPlayer.getName() + " has captured all opponent pieces!");
                vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
                currentPlayer.changeStatus(Player.Status.waiting);
            }

            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        } else {
            response.redirect("/");
            halt();
            return null;
        }
    }
}
