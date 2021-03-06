package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
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

        String username = "validusername";

        when(request.queryParams("username")).thenReturn(username);
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

    /**
     * Helper method that test CuT shows the SignIn view with proper error message
     * when the username is invalid
     */
    public void invalid_username(String username, Message error, PlayerServices playerServices) {
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        when(request.queryParams("username")).thenReturn(username);
        when(request.session().attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(playerServices);
        when(request.session().attribute(PostSignInRoute.STAT_CODE_ATTR)).thenReturn(null);
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, PostSignInRoute.TITLE);
        testHelper.assertViewModelAttribute(GetHomeRoute.MESSAGE_ATTR, error);
        // Since it is invalid username, it will not be stored to session
        testHelper.assertViewModelAttribute(GetHomeRoute.CURRENT_USERNAME_KEY, null);

        //   * test view name
        testHelper.assertViewName(PostSignInRoute.ERROR_FTL);
    }

    /**
     * Test that CuT will handle empty username
     */
    @Test
    public void empty_username(){
        invalid_username("", PostSignInRoute.EMPTY_MESSAGE, playerServices);
    }

    /**
     * Test that CuT will handle invalid username
     */
    @Test
    public void invalid_username(){
        invalid_username("test!", PostSignInRoute.CONTAIN_MESSAGE, playerServices);
    }

    /**
     * Test that CuT will handle username that is already taken
     */
    @Test
    public void existed_username(){
        playerServices.addPlayer(new Player("test"));
        invalid_username("test", PostSignInRoute.TAKEN_MESSAGE, playerServices);
    }

    /**
     * Test that verifyUsername() is returning 0 for valid username
     */
    @Test
    public void verify_valid1(){
        assertEquals(0, CuT.verifyUsername("valid"));
    }

    /**
     * Test that verifyUsername() is returning 0 for valid username
     */
    @Test
    public void verify_valid2(){
        assertEquals(0, CuT.verifyUsername("valid username"));
    }

    /**
     * Test that verifyUsername() is returning 0 for valid username
     */
    @Test
    public void verify_valid3(){
        assertEquals(0, CuT.verifyUsername(" valid username"));
    }

    /**
     * Test that verifyUsername() is returning 0 for valid username
     */
    @Test
    public void verify_valid4(){
        assertEquals(0, CuT.verifyUsername(" valid username "));
    }

    /**
     * Test that verifyUsername() is returning 1 for empty string
     */
    @Test
    public void verify_empty(){
        assertEquals(1, CuT.verifyUsername(""));
    }

    /**
     * Test that verifyUsername() is returning 2 for invalid username
     */
    @Test
    public void verify_invalid1(){
        assertEquals(2, CuT.verifyUsername("invalid!"));
    }

    /**
     * Test that verifyUsername() is returning 2 for invalid username
     */
    @Test
    public void verify_invalid2(){
        assertEquals(2, CuT.verifyUsername("invalid\""));
    }

    /**
     * Test that verifyUsername() is returning 2 for invalid username
     */
    @Test
    public void verify_invalid3(){
        assertEquals(2, CuT.verifyUsername("\"invalid"));
    }

    /**
     * Test that verifyUsername() is returning 2 for invalid username
     */
    @Test
    public void verify_invalid4(){
        assertEquals(2, CuT.verifyUsername("\\invalid"));
    }

    /**
     * Test that verifyUsername() is returning 2 for invalid username
     */
    @Test
    public void verify_invalid5(){
        assertEquals(2, CuT.verifyUsername("invalid \""));
    }

    /**
     * Test that verifyUsername() is returning 2 for invalid username
     */
    @Test
    public void verify_invalid6(){
        assertEquals(2, CuT.verifyUsername("invalid \"username"));
    }

    /**
     * Test that verifyUsername() is returning 2 for invalid username
     */
    @Test
    public void verify_invalid7(){
        assertEquals(2, CuT.verifyUsername("invalid \" username"));
    }

    /**
     * Test that verifyUsername() is returning 3 for taken username
     */
    @Test
    public void verify_taken(){
        playerServices.addPlayer(new Player("valid"));
        assertEquals(3, CuT.verifyUsername("valid"));
    }

}
