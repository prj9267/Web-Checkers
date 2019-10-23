package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webcheckers.model.Player;
import com.webcheckers.ui.PostSignInRoute;
import com.webcheckers.ui.PlayersList;
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

import java.util.ArrayList;

/**
 * The unit test suite for the {@link com.webcheckers.ui.PostSignOutRoute} component.
 *
 */
@Tag("UI-tier")
public class PostSignInRouteTest {
    /**
     * The component-under-test (CuT).
     *
     * <p>
     * This is a stateless component so we only need one.
     * The {@link GameCenter} component is thoroughly tested so
     * we can use it safely as a "friendly" dependency.
     */
    private PostSignInRoute CuT;

    // friendly objects
    private PlayerServices playerServices;
    private GameCenter gameCenter;

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
        // the GameCenter is friendly but the engine mock will need configuration
        playerServices = new PlayerServices();
        gameCenter = new GameCenter(playerServices);
        CuT = new PostSignInRoute(playerServices, engine);
    }

    /**
     * Test that CuT shows the Home view when the username is valid
     * and the Home should contains a list of players without the
     * current player.
     */
    @Test
    public void valid_username() {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        String username = "valid username";

        when(request.queryParams("username")).thenReturn("valid username");
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerServices);
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response); }, "Redirect to ./home");

        // Now the previous condition passed, redirect to GetHomeRoute
        //when(request.session().attribute(GetHomeRoute.CURRENT_USERNAME_KEY)).thenReturn(username);
        when(request.session().attribute(GetHomeRoute.CURRENT_USERNAME_KEY)).thenReturn(username);
        GetHomeRoute redirectToHome = new GetHomeRoute(playerServices, gameCenter, engine);
        redirectToHome.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, GetHomeRoute.SIGNIN_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_USERNAME_KEY, username);

        ArrayList<Player> players = playerServices.getPlayerList();
        // the player list should not contain the current player for display
        players.remove(new Player(username));
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ATTR, players);
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }
}
