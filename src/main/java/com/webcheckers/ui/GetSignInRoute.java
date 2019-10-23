package com.webcheckers.ui;

import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.*;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetSignInRoute implements Route{
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //Values to be used in the View-Model
    public static final String TITLE = "Sign In";
    public static final Message INSTRUCTION_MSG = Message.info("Please enter your username.");
    public static final String VIEW_NAME = "signin.ftl";

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /signin} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSignInRoute(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;

        LOG.config("GetSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign In page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign In page
     */
    @Override
    public Object handle(Request request, Response response){
        LOG.finer("GetSignInRoute is invoked.");
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute(GetHomeRoute.PLAYERSERVICES_KEY);

        if(playerServices == null || httpSession.attribute(GetHomeRoute.CURRENT_USERNAME_KET) == null) {
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        } else {
            Map<String, Object> vm = new HashMap<>();
            vm.put(GetHomeRoute.TITLE_ATTR, TITLE);
            vm.put(GetHomeRoute.MESSAGE_ATTR, INSTRUCTION_MSG);
            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        }
    }

}
