package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import spark.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Logger;

public class PostSubmitTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final GameCenter gameCenter;
    private final PlayerServices playerServices;
    private final Gson gson;

    /**
     * The constructor for the {@code POST /submitTurn} route handler.
     *
     * @param templateEngine
     *    The {@link TemplateEngine} used for rendering page HTML.
     */
    public PostSubmitTurnRoute(final PlayerServices playerServices,
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
        LOG.finer("PostSubmitTurnRoute has been invoked.");

        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        Message message;

        if(playerServices != null){
            // get the information of the current user
            String currentPlayerName = httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KEY);
            Player currentPlayer = playerServices.getPlayer(currentPlayerName);

            // Get the information from the match
            Match currentMatch = gameCenter.getMatch(currentPlayer);

            if (currentMatch.getHelp()) {
                return gson.toJson(Message.error("You cannot submit a turn if you clicked help."));
            }

            // If you can still jump
            BoardView currentBoardView;
            if (currentMatch.getActiveColor() == Piece.Color.RED){
                currentBoardView = currentMatch.getRedBoardView();
            }
            else {
                currentBoardView = currentMatch.getWhiteBoardView();
            }

            Move mostRecentMove = currentMatch.popMove();
            currentMatch.pushMove(mostRecentMove);
            int diffY = Math.abs(mostRecentMove.getStart().getRow() - mostRecentMove.getEnd().getRow());
            Boolean hasNextJump = false;
            // if it was a jump
            if (diffY == 2) {
                // check to see if there is another jump
                Position end = mostRecentMove.getEnd();
                hasNextJump = currentMatch.checkFourDirections(currentBoardView, end);
            }
            // if there is another jump after a jump
            if (hasNextJump) {
                message = Message.error("There is still available jump");
                return gson.toJson(message);
            }
            // regular move / no more jump
            else {
                ArrayList<Move> moves = currentMatch.getMoves();
                Move move;
                // perform all the moves
                for (int i = 0; i < moves.size(); i++){
                    move = moves.get(i);
                    int dif = Math.abs(move.getStart().getRow() - move.getEnd().getRow());
                    if (dif == 2) {
                        currentMatch.jump(move);
                    }
                    else {
                        currentMatch.move(move);
                    }
                }
                // reset the moves to nothing
                currentMatch.emptyMoves();

                //alternate turns
                currentMatch.changeActiveColor();

                message = Message.info("Your turn was submitted");
                return gson.toJson(message);
            }
        }
        else{
            return null;
        }
    }
}
