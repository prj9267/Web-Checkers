package com.webcheckers.ui;

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
    private static final String VIEW_NAME = "game.ftl";
    private static final String TITLE = "Game";
    private static final Message MESSAGE = Message.info("message");
    private static final String CURRENT_USER_ATTR = "currentUser";
    private static final String CURRENT_PLAYER_ATTR = "currentPlayer";
    private static final String VIEW_MODE_ATTR = "viewMode";
    private static final String MODE_OPTION_ATTR = "modeOption";
    private static final String RED_PLAYER_ATTR = "redPlayer";
    private static final String WHITE_PLAYER_ATTR = "whitePlayer";
    private static final String ACTIVE_COLOR_ATTR = "activeColor";
    private static final String BOARD_ATTR = "board";
    public enum viewMode {PLAY, SPECTATOR, REPLAY}

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final PlayerServices playerServices;

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

            // if the button is just triggered, initialize everything
            if (request.queryParams("button") != null) {
                String opponentName = request.queryParams("button");
                redPlayer = currentPlayer;
                whitePlayer = playerServices.getPlayer(opponentName);
                gameCenter.addMatch(redPlayer, whitePlayer);
                currentMatch = gameCenter.getMatch(redPlayer);
            } else { // else get the information from the match
                currentMatch = gameCenter.getMatch(currentPlayer);
                redPlayer = currentMatch.getRedPlayer();
                whitePlayer = currentMatch.getWhitePlayer();
            }

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
            vm.put(CURRENT_PLAYER_ATTR, currentPlayerName);

            vm.put(ACTIVE_COLOR_ATTR, currentMatch.getActiveColor());
            // right now there is only the option to play
            viewMode currentViewMode = viewMode.PLAY;
            vm.put(VIEW_MODE_ATTR, currentViewMode);

            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        } else {
            response.redirect("/");
            halt();
            return null;
        }
    }
}
