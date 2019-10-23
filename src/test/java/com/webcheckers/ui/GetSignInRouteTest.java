package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webcheckers.ui.GetSignInRoute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;

import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

/**
 * The unit test suite for the {@link com.webcheckers.ui.GetSignInRoute} component.
 *
 */
@Tag("UI-tier")
public class GetSignInRouteTest {

    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link GameCenter} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */

    private GetSignInRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        // create a unique CuT for each test
        CuT = new GetSignInRoute(engine);
    }

    /**
     * Test that CuT shows the Home view when the session is brand new.
     */
    @Test
    public void new_session() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        PlayerServices playerServices = new PlayerServices();
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerServices);
        when(request.session().attribute(GetHomeRoute.CURRENT_USERNAME_KET)).thenReturn("username");
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetSignInRoute.TITLE);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, GetSignInRoute.INSTRUCTION_MSG);
        //   * test view name
        testHelper.assertViewName(GetSignInRoute.VIEW_NAME);
        //   * verify that a player service object and the session timeout watchdog are stored
        //   * in the session.
    }

}
