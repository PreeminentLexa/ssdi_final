package com.example.finalproject.controllers;

import com.example.finalproject.controllers.K_WaitingForAnswerersController;
import com.example.finalproject.controllers.UtilControllerBase;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class L_CorrectAnswerRevealController extends UtilControllerBase {
    public Label Question;

    public Label L1;
    public Label L2;
    public Label L3;
    public Label L4;
    public Button Button1;
    public Button Button2;
    public Button Button3;
    public Button Button4;

    private static final String wrongAnswerStyle = "-fx-text-fill: #ff0000;"; /// add a strike through
    private static final String rightAnswerStyle = "-fx-text-fill: #00ff00";


    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getQuestion(String question){
        Question.setText(question);
    }
    public void Callback_getAnswers(String a1, String a2, String a3, String a4, int correct){
        Button1.setText(a1);
        Button2.setText(a2);
        Button3.setText(a3);
        Button4.setText(a4);
        Button1.setStyle(1==correct?rightAnswerStyle:wrongAnswerStyle);
        Button2.setStyle(2==correct?rightAnswerStyle:wrongAnswerStyle);
        Button3.setStyle(3==correct?rightAnswerStyle:wrongAnswerStyle);
        Button4.setStyle(4==correct?rightAnswerStyle:wrongAnswerStyle);
    }
    public void Callback_getAnswerCount(int a1count, int a2count, int a3count, int a4count){
        L1.setText(0==a1count?"":"X"+a1count);
        L2.setText(0==a2count?"":"X"+a2count);
        L3.setText(0==a3count?"":"X"+a3count);
        L4.setText(0==a4count?"":"X"+a4count);
    }

    // This page has Utility.timeOnCorrectAnswerPage seconds to animate (I set it to 10 seconds originally)
}
