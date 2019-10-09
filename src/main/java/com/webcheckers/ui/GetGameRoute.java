package com.webcheckers.ui;

import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Board;
import com.webcheckers.model.Match;
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
    private static final String TITLE_ATTR = "title";
    private static final String TITLE = "Let's play a game...";
    private static final Message MESSAGE = Message.info("message");

    private static final String CURRENT_USER_ATTR = "currentUser";
    private static final String VIEW_MODE_ATTR = "viewMode";
    private static final String MODE_OPTION_ATTR = "modeOption";
    private static final String RED_PLAYER_ATTR = "redPlayer";
    private static final String WHITE_PLAYER_ATTR = "whitePlayer";
    private static final String ACTIVE_COLOR_ATTR = "activeColor";
    private static final String BOARD_ATTR = "board";

    private final TemplateEngine templateEngine;

    /**
     * The constructor for the {@code GET /game} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public GetGameRoute(final TemplateEngine templateEngine){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response){
        // no session for now
        //todo UNCOMPLETE
        // build the View-Model

        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute("playerServices");

        if(playerServices != null) {
            final Map<String, Object> vm = new HashMap<>();
            vm.put(TITLE_ATTR, TITLE);

            Player player1 = new Player("Player1");
            Player player2 = new Player("Player2");
            final Map<Object, String> redPlayer = new HashMap<>();
            redPlayer.put("name", player1.getName());
            final Map<Object, String> whitePlayer = new HashMap<>();
            whitePlayer.put("name", player2.getName());

            Match match = playerServices.getMatch(player1.getName(), player2.getName());
            Board board = match.getBoard();

            vm.put(BOARD_ATTR, board.getBoard());

            vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
            vm.put(GetHomeRoute.MESSAGE_ATTR, MESSAGE);
            vm.put(CURRENT_USER_ATTR, redPlayer);
            vm.put(VIEW_MODE_ATTR, "Playing");
            vm.put(RED_PLAYER_ATTR, redPlayer);
            vm.put(WHITE_PLAYER_ATTR, whitePlayer);
            vm.put(ACTIVE_COLOR_ATTR, "Red");

            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        }
        else{
            response.redirect("/");
            halt();
            return null;
        }
    }
}
