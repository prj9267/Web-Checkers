package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.Player;

import static spark.Spark.*;

public class PostSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //Values to be used in the View-Model
    private static final String TITLE = "Sign In";
    private static final Message CONTAIN_MESSAGE = Message.error("Your username containing invalid character(s).");
    private static final Message EMPTY_MESSAGE = Message.error("Your username cannot be empty.");
    private static final Message TAKEN_MESSAGE = Message.error("This username is already taken.");
    private static final String ERROR_FTL = "signin.ftl";

    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;
    /*private static final Message ERROR_MSG = Message.info("Sorry, wrong username/password.");
    private static final String MESSAGE = "Waiting for player...";*/

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSignInRoute(GameCenter gameCenter, TemplateEngine templateEngine){
        // validation
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.gameCenter = gameCenter;

        LOG.config("PostSignInRoute is initialized.");
    }

    /**
     * Checks if a username contains an invalid char
     * @param username  - the username in question
     * @return  - false if username contains no invalid chars, true otherwise.
     */
    public Boolean containsInvalidCharacter(String username){
        String invalidCharacters = "~`!@#$%^&*()-_=+[]{}\\|;:',<.>/?";
        for (int i = 0; i < invalidCharacters.length(); i++){
            if (username.indexOf(invalidCharacters.charAt(i)) > 0  )
                return true;
        }
        return false;
    }

    /**
     * Checks if the username is a valid name to be signed in with.
     * @param username  - the username in question
     * @return - 0 if it is valid
     *          1 if the username is empty
     *          2 if the username contains invalid characters
     *          3 if the username is already taken
     */
    public int verifyUsername(String username){
        // right now does not contains a list of players
        if (username.length() == 0)
            return 1;
        else if (containsInvalidCharacter(username))
            return 2;
        else if (this.gameCenter.getPlayers().contains(username))
            return 3;
        return 0;
    }

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
        int stat_code = verifyUsername(username);

        if(httpSession.attribute("playerServices") != null) {
            if (stat_code == 0) {
                gameCenter.addPlayer(username);

                httpSession.attribute("currentPlayer", username);
                httpSession.removeAttribute("numPlayers");

                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
                //return templateEngine.render(new ModelAndView(vm, SUCCESS_VIEW_NAME));
            } else {
                vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
                if (stat_code == 1)
                    vm.put(GetHomeRoute.MESSAGE_ATTR, EMPTY_MESSAGE);
                else if (stat_code == 2)
                    vm.put(GetHomeRoute.MESSAGE_ATTR, CONTAIN_MESSAGE);
                else if (stat_code == 3)
                    vm.put(GetHomeRoute.MESSAGE_ATTR, TAKEN_MESSAGE);

                return templateEngine.render(new ModelAndView(vm, ERROR_FTL));
            }
        }
        else{
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
    }
}
