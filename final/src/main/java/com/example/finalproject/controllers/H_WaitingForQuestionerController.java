package com.example.finalproject.controllers;

// The Controller for frame H

import com.example.finalproject.User;

import java.net.URL;
import java.util.ResourceBundle;

public class H_WaitingForQuestionerController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_waitingForQuestioner(User user){} // The user object of the questioner
}
