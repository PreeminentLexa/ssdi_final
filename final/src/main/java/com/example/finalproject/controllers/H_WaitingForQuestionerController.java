package com.example.finalproject.controllers;

// The Controller for frame H

import com.example.finalproject.User;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
//Coded by: Bridget
public class H_WaitingForQuestionerController extends UtilControllerBase {
    public Label Starting;
    public Label waiting;
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_waitingForQuestioner(User user){
        Starting.setText("Starting.\n"+user.getUsername()+"is the questionner");
    } // The user object of the questioner
}
