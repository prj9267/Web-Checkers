package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class MoveTest {
    private Position start;
    private Position end;
    private Move CuT;

    @BeforeEach
    void setup() {
        CuT = new Move(start, end);
    }


}
