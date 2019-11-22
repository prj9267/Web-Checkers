package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Match;
import com.webcheckers.model.Move;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Logger;

public class PostRequestHelpRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final PlayerServices playerServices;
    private final Gson gson;

    /**
     * The constructor for the {@code POST /requestHelp} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public PostRequestHelpRoute(final PlayerServices playerServices,
                               final GameCenter gameCenter,
                               final TemplateEngine templateEngine, Gson gson){
        Objects.requireNonNull(playerServices, "playerServices must not be null");
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.playerServices = playerServices;
        this.gameCenter = gameCenter;
        this.templateEngine = templateEngine;
        this.gson = gson;
    }

    /**
     * Refresh the game view after a turn was submitted.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the message displayed after submitting a turn.
     */
    @Override
    public Object handle(Request request, Response response){
        LOG.finer("PostRequestHelp has been invoked.");

        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        Message message;

        if(playerServices != null){
            // get the information of the current user
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);

            // Get the information from the match
            Match currentMatch = gameCenter.getMatch(currentPlayer);

            Boolean help = currentPlayer.changeHelp();

            System.out.println("===============Help btn is clicked===============");

            message = Message.info("Help Status Changed!");
            return gson.toJson(message);
        }
        else{
            return null;
        }
    }
}
