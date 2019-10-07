package com.webcheckers.ui;

import com.webcheckers.util.Message;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetSignInRoute implements Route{
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private static final Message INSTRUCTION_MSG = Message.info("Please enter your username.");
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
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, "Sign In");

        vm.put("message", INSTRUCTION_MSG);


        response.redirect(WebServer.POST_SINGIN_URL);
        halt();
        return null;
    }

}
