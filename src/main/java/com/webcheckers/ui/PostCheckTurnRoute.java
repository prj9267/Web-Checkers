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

public class PostCheckTurnRoute implements Route {
    // Values used in the view-model map for rendering the game view.
    public static final Message isYourTurn = Message.info("true");
    public static final Message notYourTurn = Message.error("false");

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final PlayerServices playerServices;

    /**
     * The constructor for the {@code POST /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public PostCheckTurnRoute(final PlayerServices playerServices,
                        final GameCenter gameCenter,
                        final TemplateEngine templateEngine){
        Objects.requireNonNull(playerServices, "playerServices must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerServices = playerServices;
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
    }

    public boolean isMyTurn(Player currentPlayer, Player red, Player white, Piece.Color activeColor){
        if (currentPlayer.equals(red) && activeColor == Piece.Color.RED ||
            currentPlayer.equals(white) && activeColor == Piece.Color.WHITE)
            return true;
        return false;
    }

    @Override
    public Object handle(Request request, Response response){
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        if(playerServices != null) {
            final Map<String, Object> vm = new HashMap<>();
            vm.put(GetHomeRoute.TITLE_ATTR, GetGameRoute.TITLE);
            // get the information of the current user
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);

            // send current user to ftl
            vm.put(GetGameRoute.CURRENT_USER_ATTR, currentPlayer);

            // Get the information from the match
            Match currentMatch = gameCenter.getMatch(currentPlayer);
            Player redPlayer = currentMatch.getRedPlayer();
            Player whitePlayer = currentMatch.getWhitePlayer();

            // give player information to ftl
            httpSession.attribute(GetGameRoute.MATCH_ATTR, currentMatch);
            vm.put(GetGameRoute.RED_PLAYER_ATTR, redPlayer);
            vm.put(GetGameRoute.WHITE_PLAYER_ATTR, whitePlayer);

            // get different board from the match
            BoardView whiteBoardView = currentMatch.getWhiteBoardView();
            BoardView redBoardView = currentMatch.getRedBoardView();

            // send the information accordingly
            if(currentPlayerName.equals(redPlayer.getName())) {
                vm.put(GetGameRoute.BOARD_ATTR, redBoardView);
                vm.put(GetGameRoute.CURRENT_USER_ATTR, redPlayer);
            } else {
                vm.put(GetGameRoute.BOARD_ATTR, whiteBoardView);
                vm.put(GetGameRoute.CURRENT_USER_ATTR, whitePlayer);
            }

            // for the nav-bar to display the signout option
            vm.put(GetHomeRoute.CURRENT_PLAYER_ATTR, currentPlayerName);

            vm.put(GetGameRoute.ACTIVE_COLOR_ATTR, currentMatch.getActiveColor());
            // right now there is only the option to play
            GetGameRoute.viewMode currentViewMode = GetGameRoute.viewMode.PLAY;
            vm.put(GetGameRoute.VIEW_MODE_ATTR, currentViewMode);

            //TODO take care of game end
            // verify turn
            if (isMyTurn(currentPlayer, redPlayer, whitePlayer, currentMatch.getActiveColor()))
                vm.put(GetHomeRoute.MESSAGE_ATTR, isYourTurn);
            else
                vm.put(GetHomeRoute.MESSAGE_ATTR, notYourTurn);

            return templateEngine.render(new ModelAndView(vm, GetGameRoute.VIEW_NAME));
        }
        else{
            //TODO take care of resign
            /*response.redirect("/");
            halt();*/
            return null;
        }
    }
}
