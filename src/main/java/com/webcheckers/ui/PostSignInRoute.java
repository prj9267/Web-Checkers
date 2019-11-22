package com.webcheckers.ui;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.CSVutility;
import com.webcheckers.util.Message;
import spark.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.Player;

import static com.webcheckers.ui.WebServer.csvFile;
import static spark.Spark.halt;

public class PostSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //Values to be used in the View-Model
    public static final String TITLE = "Sign In";
    public static final String ERROR_FTL = "signin.ftl";
    public static final Message TAKEN_MESSAGE = Message.error("Your username was taken, enter a new username that only contains" +
            " alphanumeric characters and spaces.");
    public static final Message CONTAIN_MESSAGE = Message.error("Your username is invalid, enter a new username that only contains" +
            " alphanumeric characters and spaces.");
    public static final Message EMPTY_MESSAGE = Message.error("Your username cannot be empty.");
    public static final String STAT_CODE_ATTR = "statCode";

    private final TemplateEngine templateEngine;
    private final PlayerServices playerServices;
    private CSVutility csvutility;

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
     * @return - 0 if it is valid
     *          1 if the username is empty
     *          2 if the username contains invalid characters
     *          3 if the username is already taken
     */

    public int verifyUsername(String username){
        // right now does not contains a list of players
        Player player = new Player(username);
        if (username.length() == 0)
            return 1;
        else if (player.containsInvalidCharacter())
            return 2;
        else if (this.playerServices.getPlayerList().contains(player))
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
    public Object handle(Request request, Response response) {
        LOG.finer("PostSignInRoute is invoked.");
        String username = request.queryParams("username");
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();
        final Session httpSession = request.session();
        int statCode;
        if (httpSession.attribute(STAT_CODE_ATTR) == null ||
                username != null) {
            statCode = verifyUsername(username);
            httpSession.attribute(STAT_CODE_ATTR, statCode);
        }
        else
            statCode = httpSession.attribute(STAT_CODE_ATTR);

        if(httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY) != null) {
            if (statCode == 0) {
                csvutility = new CSVutility();

                Player player = csvutility.findPlayer(username);
                playerServices.addPlayer(player);

                // TODO test
                csvutility.editPlayerRecords(new Player("Yuqi", 9999, 1, 9998));

                httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY, username);
                httpSession.removeAttribute("numPlayers");
                httpSession.removeAttribute(STAT_CODE_ATTR);

                response.redirect(WebServer.HOME_URL);
                halt();
                return null;
            } else {
                vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
                if (statCode == 1)
                    vm.put(GetHomeRoute.MESSAGE_ATTR, EMPTY_MESSAGE);
                else if (statCode == 2)
                    vm.put(GetHomeRoute.MESSAGE_ATTR, CONTAIN_MESSAGE);
                else if (statCode == 3)
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
