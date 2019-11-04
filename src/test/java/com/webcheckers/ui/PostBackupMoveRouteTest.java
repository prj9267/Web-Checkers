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
public class PostBackupMoveRouteTest {
    private PostBackupMoveRoute CuT;


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
        CuT = new PostBackupMoveRoute(playerServices, gameCenter, engine, gson);
    }

    @Test
    void checkPostBackupMoveRoute() {
        PostBackupMoveRoute pbmr = new PostBackupMoveRoute(playerServices, gameCenter, engine, gson);
        assertEquals(pbmr.handle(request, response), CuT.handle(request, response),"PostBackupMoveRoute not constructed correctly");
    }
}