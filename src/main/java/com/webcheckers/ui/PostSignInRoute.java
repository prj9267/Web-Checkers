package com.webcheckers.ui;

import com.webcheckers.util.Message;
import spark.*;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.webcheckers.appl.Player;

import static spark.Spark.halt;

public class PostSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private static final Message ERROR_MSG = Message.info("Sorry, wrong username/password.");
    private static final String TITLE = "Game!";
    private static final String MESSAGE = "Waiting for player...";
    private static final String VIEW_NAME = "game.ftl";
    private final TemplateEngine templateEngine;

    public PostSignInRoute(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;


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
        Player player1 = new Player("Player1");
        Player player2 = new Player("Player2");
        vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
        vm.put(GetHomeRoute.MESSAGE_ATTR, MESSAGE);
        vm.put(GetHomeRoute.CURRENT_USER_ATTR, player1);
        vm.put(GetHomeRoute.VIEW_MODE_ATTR, "Playing");
        vm.put(GetHomeRoute.RED_PLAYER_ATTR, player1);
        vm.put(GetHomeRoute.WHITE_PLAYER_ATTR, player2);
        vm.put(GetHomeRoute.ACTIVE_COLOR_ATTR, "Red");
        // right now everything just go th
        return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
        /*response.redirect(WebServer.GAME_URL);
        halt();
        return null;*/
    }
}
