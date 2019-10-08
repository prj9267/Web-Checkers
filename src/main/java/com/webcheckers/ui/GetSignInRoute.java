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
    private static final Message INSTRUCTION_MSG = Message.info("Please enter your username.");
    private static final String VIEW_NAME = "signin.ftl";
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

    @Override
    public Object handle(Request request, Response response){
        LOG.finer("GetSignInRoute is invoked.");

        //
        final Session httpSession = request.session();
        final PlayerServices playerServices = httpSession.attribute("playerServices");
        if(playerServices == null){
            response.redirect(WebServer.HOME_URL);
            halt();
            return null;
        }
        else {
            Map<String, Object> vm = new HashMap<>();
            vm.put(GetHomeRoute.TITLE_ATTR, "Sign In");
            vm.put("message", INSTRUCTION_MSG);
            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        }
    }

}
