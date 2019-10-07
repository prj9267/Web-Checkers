package com.webcheckers.ui;

import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.webcheckers.model.Player;

import static spark.Spark.halt;

public class PostSignInRoute implements Route {
    private static final String TITLE_ATTR = "title";
    private static final String CURRENT_PLAYER = "currentplayer";
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private static final String TITLE = "YOU'VE LOGGED IN. CONGRATS.";
    private static final String VIEW_NAME = "lobby.ftl"; //changed from "game.ftl"
    private final TemplateEngine templateEngine;
    private Player currentPlayer;
    /*private static final Message ERROR_MSG = Message.info("Sorry, wrong username/password.");
    private static final String MESSAGE = "Waiting for player...";*/

    public PostSignInRoute(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
        this.currentPlayer = new Player("Username");
    }

    @Override
    public Object handle(Request request, Response response){
        // TODO
        // no session for now

        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();
        // TODO
        // right now everything should be true (aka, any username/password
        // combination should be correct.

        /*Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        final Map<Object, String> redPlayer = new HashMap<>();
        redPlayer.put("name", player1.getName());
        final Map<Object, String> whitePlayer = new HashMap<>();
        whitePlayer.put("name", player2.getName());*/
        /*
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
        vm.put(GetHomeRoute.MESSAGE_ATTR, MESSAGE);
        vm.put(GetHomeRoute.CURRENT_USER_ATTR, redPlayer);
        vm.put(GetHomeRoute.VIEW_MODE_ATTR, "Playing");
        vm.put(GetHomeRoute.RED_PLAYER_ATTR, redPlayer);
        vm.put(GetHomeRoute.WHITE_PLAYER_ATTR, whitePlayer);
        vm.put(GetHomeRoute.ACTIVE_COLOR_ATTR, "Red");*/
        // right now everything just go th
        String usernames = request.queryParams("username");
        //if(!())
        response.redirect(CURRENT_PLAYER);
        return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
        /*response.redirect(WebServer.GAME_URL);
        halt();
        return null;*/
    }
}
