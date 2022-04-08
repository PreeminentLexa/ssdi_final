package com.example.finalproject.controllers;

// The Controller for frame L

import java.net.URL;
import java.util.ResourceBundle;

public class L_CorrectAnswerRevealController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getQuestion(String question){}
    public void Callback_getAnswers(String a1, String a2, String a3, String a4, int correct){}
    public void Callback_getAnswerCount(int a1count, int a2count, int a3count, int a4Count){}

    // This page has Utility.timeOnCorrectAnswerPage seconds to animate (I set it to 10 seconds originally)
}
