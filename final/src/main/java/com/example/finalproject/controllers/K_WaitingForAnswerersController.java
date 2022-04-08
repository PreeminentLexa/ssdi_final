package com.example.finalproject.controllers;

// The Controller for frame K

import java.net.URL;
import java.util.ResourceBundle;

public class K_WaitingForAnswerersController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getQuestion(String question){} // The question
    public void Callback_getAnswers(String a1, String a2, String a3, String a4, int correct){} // The answers
    public void Callback_someoneAnswered(int answerNumber){}
}
