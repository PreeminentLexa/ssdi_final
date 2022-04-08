package com.example.finalproject.controllers;

// The Controller for frame I

import com.example.finalproject.Utility;

import java.net.URL;
import java.util.ResourceBundle;

public class I_AnsweringController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getQuestion(String question){}
    public void Callback_getAnswers(String a1, String a2, String a3, String a4, int correct){} // correct will be -1, because it shouldn't be used here

    public void testPick() {
        Utility.answering_pick(1);
    }

    // Callable:
    // Utility.answering_pick(int number);
    //      Used to pick a specific answer. This is sent to the server
    //      number corresponds to the picked answer 1-4
}
