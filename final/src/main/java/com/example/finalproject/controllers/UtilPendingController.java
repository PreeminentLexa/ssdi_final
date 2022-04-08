package com.example.finalproject.controllers;

import com.example.finalproject.Utility;

import java.net.URL;
import java.util.ResourceBundle;

public class UtilPendingController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}

    public void testReturnToMainMenu() {
        Utility.endGame_returnToMainMenu();
    }
}
