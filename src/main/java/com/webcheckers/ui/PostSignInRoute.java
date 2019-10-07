package com.webcheckers.ui;

import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class PostSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private static final Message ERROR_MSG = Message.info("Sorry, wrong username/password.");
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
        vm.put("title", "Game");
        vm.put("message", "Waiting for player...");


        response.redirect(WebServer.GAME_URL);
        return null;
    }
}
