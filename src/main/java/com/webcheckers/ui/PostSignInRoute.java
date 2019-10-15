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

import static spark.Spark.*;

public class PostSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //Values to be used in the View-Model
    private static final String SUCESS_TITLE = "Home";
    private static final String FAILURE_TITLE = "Sign In Page";
    private static final String SUCCESS_VIEW_NAME = "home.ftl";
    private static final String FAILURE_VIEW_NAME = "signin.ftl";
    private static final Message SUCCESS_MESSAGE = Message.info("You have successfully signed in!");
    private static final String TAKEN_MESSAGE = "Your username was taken, enter a new username that only contains" +
            " alphanumeric characters and spaces (do not start with a space).";
    private static final String INVALID_MESSAGE = "Your username is invalid, enter a new username that only contains" +
            " alphanumeric characters and spaces (do not start with a space).";

    private final TemplateEngine templateEngine;
    private final PlayerServices playerServices;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSignInRoute(PlayerServices playerServices, TemplateEngine templateEngine){
        Objects.requireNonNull(playerServices, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
        this.playerServices = playerServices;

        LOG.config("PostSignInRoute is initialized.");
    }

    /**
     * Checks if the username is a valid name to be signed in with.
     * @param username  - the username in question
     * @return  - true if valid, false otherwise
     */
    /*public Boolean isSuccess(String username){
        // right now does not contains a list of players
        if (username.length() == 0)
            return false;
        else if (containsInvalidCharacter(username))
            return false;
        return true;
    }*/

    /**
     * Render the WebCheckers Home page after signing in.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page after being signed in.
     */
    @Override
    public Object handle(Request request, Response response){
        LOG.finer("PostSignInRoute is invoked.");
        String username = request.queryParams("username");
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();
        final Session httpSession = request.session();

        if(httpSession.attribute("name") != null) {
            response.redirect(WebServer.HOME_URL);
            return null;
        }
        vm.put("title", "TITLE");
        if (!playerServices.isValid(username)) {
            vm.put("username", username);
            vm.put(GetHomeRoute.MESSAGE_ATTR, new Message(INVALID_MESSAGE, Message.Type.ERROR));
            return templateEngine.render(new ModelAndView(vm, FAILURE_VIEW_NAME));
        } else if (!playerServices.isTaken(username)) {
            vm.put("username", username);
            vm.put(GetHomeRoute.MESSAGE_ATTR, new Message(TAKEN_MESSAGE, Message.Type.ERROR));
            return templateEngine.render(new ModelAndView(vm, FAILURE_VIEW_NAME));
        } else {
            httpSession.attribute("username", username);
            Player currentPlayer = new Player(username);
            playerServices.addPlayer(currentPlayer);
            response.redirect(WebServer.HOME_URL);
            return null;
        }
        /*if(httpSession.attribute("name") != null) {
            response.redirect(WebServer.HOME_URL)
            if (isSuccess(username)) {
                gameCenter.addPlayer(new Player(username));

                //Pass through objects to the VM for use in ftl files.
                vm.put(GetHomeRoute.TITLE_ATTR, SUCESS_TITLE);
                vm.put(GetHomeRoute.MESSAGE_ATTR, SUCCESS_MESSAGE);
                vm.put(GetHomeRoute.PLAYERS_ATTR, gameCenter.getPlayers());
                vm.put("playerName", username);
                httpSession.attribute("currentUsername", username);
                vm.put("loggedIn", 0);

                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
                //return templateEngine.render(new ModelAndView(vm, SUCCESS_VIEW_NAME));
            } else {
                vm.put(GetHomeRoute.TITLE_ATTR, FAILURE_TITLE);
                if (username.length() == 0)
                    vm.put(GetHomeRoute.MESSAGE_ATTR, Message.error(EMPTY_MESSAGE));
                else if (containsInvalidCharacter(username))
                    vm.put(GetHomeRoute.MESSAGE_ATTR, Message.error(CONTAIN_MESSAGE));

                response.redirect(WebServer.SIGNIN_URL);
                halt();
                return null;
                //return templateEngine.render(new ModelAndView(vm, FAILURE_VIEW_NAME));
            }
        }
        else{
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }*/
    }
}
