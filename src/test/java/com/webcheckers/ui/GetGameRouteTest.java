package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;


import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        playerServices = new PlayerServices();
        gameCenter = new GameCenter(playerServices);

        // create a unique CuT for each test
        CuT = new GetGameRoute(playerServices,gameCenter,engine);
    }

    /**
     * Test that CuT redirects to the Home view when a @Linkplain(PlayerServices) object does
     * not exist, i.e. the session timed out or an illegal request on this URL was received.
     */
    @Test
    public void faulty_session() {
        // Arrange the test scenario: There is an existing session with a PlayerServices object
        when(session.attribute(GetHomeRoute.PLAYERSERVICES_KEY)).thenReturn(null);

        // Invoke the test
        try {
            CuT.handle(request, response);
            fail("Redirects invoke halt exceptions.");
        } catch (HaltException e) {
            // expected
        }

        // Analyze the results:
        //   * redirect to the Game view
        verify(response).redirect(WebServer.HOME_URL);
    }
}