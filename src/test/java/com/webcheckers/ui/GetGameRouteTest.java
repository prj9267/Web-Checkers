package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class GetGameRouteTest {
    private GetGameRoute CuT;

    // mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;

    private PlayerServices playerServices;
    private GameCenter gameCenter;
    private Message message;

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

        //create required parameters
        playerServices = mock(PlayerServices.class);
        gameCenter = mock(GameCenter.class);

        // create a unique CuT for each test
        CuT = new GetGameRoute(playerServices,gameCenter,engine);
    }
    @Test
    public void new_game(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
    }

    @Test
    public void faulty_session(){
        when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(null);

    }

}