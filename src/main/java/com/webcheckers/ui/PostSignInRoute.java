package com.webcheckers.ui;

import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.webcheckers.model.Player;

import static spark.Spark.halt;

public class PostSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private static final String SUCESS_TITLE = "Lobby";
    private static final String SUCCESS_VIEW_NAME = "lobby.ftl";
    private static final String SUCCESS_MESSAGE = "You have successfully signin!";

    private static final String FAILURE_TITLE = "Sign In Page";
    private static final String FAILURE_VIEW_NAME = "signin.ftl";
    private static final String CONTAIN_MESSAGE = "Your username containing invalid character(s).";
    private static final String MISS_MESSAGE = "Your username missing at least one alphanumeric character(s).";
    private static final String EMPTY_MESSAGE = "Your username cannot be empty.";





    private final TemplateEngine templateEngine;
    private Player currentPlayer;
    /*private static final Message ERROR_MSG = Message.info("Sorry, wrong username/password.");
    private static final String MESSAGE = "Waiting for player...";*/

    public PostSignInRoute(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
        this.currentPlayer = new Player("Username");
    }

    public Boolean containsInvalidCharcter(String username){
        String invalidCharacters = "~`!@#$%^&*()-_=+[]{}\\|;:',<.>/?";
        for (int i = 0; i < invalidCharacters.length(); i++){
            if (invalidCharacters.indexOf(invalidCharacters.charAt(i)) > 0  )
                return true;
        }
        return false;
    }

    public Boolean isSuccess(String username){
        // right now does not contains a list of players
        if (username == "")
            return false;
        else if (containsInvalidCharcter(username))
            return false;
        return true;
    }

    @Override
    public Object handle(Request request, Response response){
        String username = request.queryParams("username");
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();

        if (isSuccess(username)){
            vm.put(GetHomeRoute.TITLE_ATTR, SUCESS_TITLE);
            vm.put(GetHomeRoute.MESSAGE_ATTR, SUCCESS_MESSAGE);


        }




        // right now everything just go th
        //if(!())
        return templateEngine.render(new ModelAndView(vm , SUCCESS_VIEW_NAME));
        /*response.redirect(WebServer.GAME_URL);
        halt();
        return null;*/
    }
}
