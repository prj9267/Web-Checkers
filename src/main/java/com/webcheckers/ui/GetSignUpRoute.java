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

//TODO NOT CURRENTLY IN USE FOR SPRINT 1
public class GetSignUpRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private static final Message INSTRUCTION_MSG = Message.info("Please enter a username.");
    private static final String VIEW_NAME = "signup.ftl";
    private final TemplateEngine templateEngine;

    public GetSignUpRoute(TemplateEngine templateEngine){
        this.templateEngine = templateEngine;

        LOG.config("GetSignUpRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response){
        LOG.finer("GetSignUpRoute is invoked.");

        //
        Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATTR, "Sign Up");
        vm.put("message", INSTRUCTION_MSG);

        return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
    }
}
