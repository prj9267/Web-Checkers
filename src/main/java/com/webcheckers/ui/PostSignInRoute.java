package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.Player;

import static spark.Spark.halt;
import static spark.Spark.redirect;

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




    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;
    /*private static final Message ERROR_MSG = Message.info("Sorry, wrong username/password.");
    private static final String MESSAGE = "Waiting for player...";*/

    public PostSignInRoute(GameCenter gameCenter, TemplateEngine templateEngine){
        // validation
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;
    }

    public Boolean containsInvalidCharacter(String username){
        String invalidCharacters = "~`!@#$%^&*()-_=+[]{}\\|;:',<.>/?";
        for (int i = 0; i < invalidCharacters.length(); i++){
            if (username.indexOf(invalidCharacters.charAt(i)) > 0  )
                return true;
        }
        return false;
    }

    public Boolean isSuccess(String username){
        // right now does not contains a list of players
        if (username.length() == 0)
            return false;
        else if (containsInvalidCharacter(username))
            return false;
        return true;
    }

    @Override
    public Object handle(Request request, Response response){
        String username = request.queryParams("username");
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();
        final Session httpSession = request.session();

        if(httpSession.attribute("playerServices") != null) {
            if (isSuccess(username)) {
                // right now only take care if there is no invalid usernames
                // there still can have the same name

                //TODO Add the player to the gameCenter here? - Parker
                gameCenter.addPlayer(new Player(username));

                vm.put(GetHomeRoute.TITLE_ATTR, SUCESS_TITLE);
                vm.put(GetHomeRoute.MESSAGE_ATTR, SUCCESS_MESSAGE);
                vm.put(GetHomeRoute.PLAYERS_ATTR, gameCenter.getPlayers());
                vm.put("playerName", username);

                return templateEngine.render(new ModelAndView(vm, SUCCESS_VIEW_NAME));
            } else {
                vm.put(GetHomeRoute.TITLE_ATTR, FAILURE_TITLE);
                if (username.length() == 0)
                    vm.put(GetHomeRoute.MESSAGE_ATTR, Message.error(EMPTY_MESSAGE));
                else if (containsInvalidCharacter(username))
                    vm.put(GetHomeRoute.MESSAGE_ATTR, Message.error(CONTAIN_MESSAGE));

                return templateEngine.render(new ModelAndView(vm, FAILURE_VIEW_NAME));
            }
        }
        else{
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
}
