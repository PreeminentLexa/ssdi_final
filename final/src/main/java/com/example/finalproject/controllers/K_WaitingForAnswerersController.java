package com.example.finalproject.controllers;

// The Controller for frame K

import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class K_WaitingForAnswerersController extends UtilControllerBase {
    public Label Question;
    public Label L1;
    public Label L2;
    public Label L3;
    public Label L4;
    public Button Button1;
    public Button Button2;
    public Button Button3;
    public Button Button4;

    int l1=0;
    int l2=0;
    int l3=0;
    int l4=0;
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getQuestion(String question){
        Question.setText(question);
    } // The question
    public void Callback_getAnswers(String a1, String a2, String a3, String a4, int correct){
        Button1.setText(a1);
        Button2.setText(a2);
        Button3.setText(a3);
        Button4.setText(a4);
    } // The answers
    public void Callback_someoneAnswered(int answerNumber){
        if (answerNumber==1){
            l1+=1;
            L1.setText("X"+(l1));
        }
        if (answerNumber==2){
            l2+=1;
            L2.setText("X"+(l2));
        }
        if (answerNumber==3){
            l3+=1;
            L3.setText("X"+(l3));
        }
        if (answerNumber==4){
            l4+=1;
            L4.setText("X"+(l4));
        }
    }
}

