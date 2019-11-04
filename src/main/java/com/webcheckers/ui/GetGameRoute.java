package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Match;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            // TODO modeOptions check if game has ended and add modeOptions to vm to send GameOver message
            // if the button is just triggered, initialize everything
            if (request.queryParams("button") != null) {
                String opponentName = request.queryParams("button");
                redPlayer = currentPlayer;
                whitePlayer = playerServices.getPlayer(opponentName);
                gameCenter.addMatch(redPlayer, whitePlayer);
                currentMatch = gameCenter.getMatch(redPlayer);
            } else { // else get the information from the match
                currentMatch = gameCenter.getMatch(currentPlayer);
                // TODO debug
                /*if(currentMatch.isGameResigned() == Match.STATE.resigned) {
                    //vm.put("modeOptionsAsJSON", gson.toJson(currentMatch.getModeOptions()));
                    gameCenter.removePlayer(currentPlayer);
                    currentPlayer.changeStatus(Player.Status.waiting);
                }*/
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

            // remove the match at the end since the match is over
            if(currentMatch.isGameResigned() == Match.STATE.resigned) {
                Gson gson = new Gson();
                vm.put("modeOptionsAsJSON", gson.toJson(currentMatch.getModeOptions()));
                gameCenter.removePlayer(currentPlayer);
                currentPlayer.changeStatus(Player.Status.waiting);
                gameCenter.removeMatch(currentMatch);
            }

            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        } else {
            response.redirect("/");
            halt();
            return null;
        }
    }
}
