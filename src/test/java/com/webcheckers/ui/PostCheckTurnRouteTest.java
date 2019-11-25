package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class PostCheckTurnRouteTest {
    private PostCheckTurnRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private Gson gson = new Gson();

    private PlayerServices playerServices;
    private GameCenter gameCenter;

    @BeforeEach
    void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);


        //create required parameters
        playerServices = new PlayerServices();
        gameCenter = new GameCenter(playerServices);

        // create a unique CuT for each test
        CuT = new PostCheckTurnRoute(playerServices, gameCenter, engine, gson);
    }

    /*@Test
    void checkPostCheckTurnRoute() {
        PostCheckTurnRoute pctr = new PostCheckTurnRoute(playerServices, gameCenter, engine, gson);
        assertEquals(pctr.handle(request, response), CuT.handle(request, response), "PostCheckTurnRoute not constructed correctly");
    }*/

//    @Test
//    void checkIsMyTurn() {
//        PostCheckTurnRoute pctr = new PostCheckTurnRoute(playerServices, gameCenter, engine, gson);
//        Player playerOne = new Player("One");
//        Player playerTwo = new Player("Two");
//
//        assertEquals(pctr.isMyTurn(playerOne, playerOne, playerTwo,));
//    }
}
