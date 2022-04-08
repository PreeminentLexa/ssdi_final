package com.example.finalproject.controllers;

// The Controller for the frame after M, when the game is finished

import com.example.finalproject.Utility;

import java.net.URL;
import java.util.ResourceBundle;

public class N_EndGameController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void testReturnToMainMenu() {
        Utility.endGame_returnToMainMenu();
    }

    // Callable:
    // Utility.endGame_returnToMainMenu();
    //      Returns to c_mainmenu
}
