package com.example.finalproject.controllers;

// The Controller for frame D

import com.example.finalproject.Utility;

import java.net.URL;
import java.util.ResourceBundle;

public class D_CreateGameController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void testBack() {
        Utility.createGame_back();
    }
    public void testCreate() {
        String[] settings = new String[]{
                "rounds|i|5",
                "answertime|i|30",
        };
        Utility.createGame_create(settings, "superPassword");
    }

    // Callable:
    // Utility.createGame_back();
    //      Returns to c_mainmenu
    // Utility.createGame_create(String[] settings, String password);
    //      Settings are in the format "key|type|value" where the type is "s", "i" or "f" for string, int, float
    //      E.G. {"rounds|i|5", "gametitle|s|Super game!"}
    //      Any setting key is permitted, it's just an index that can be read from later
    //      Empty password can be "" or null
}
