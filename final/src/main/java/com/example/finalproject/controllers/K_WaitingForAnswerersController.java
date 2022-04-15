package com.example.finalproject.controllers;

// The Controller for frame K

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class K_WaitingForAnswerersController extends UtilControllerBase {
    @FXML
    public Label Question;
    @FXML
    protected Label AnswerCount1;
    private int answerCountTicker1 = 0;
    @FXML
    protected Label AnswerCount2;
    private int answerCountTicker2 = 0;
    @FXML
    protected Label AnswerCount3;
    private int answerCountTicker3 = 0;
    @FXML
    protected Label AnswerCount4;
    private int answerCountTicker4 = 0;

    public Button Button1;
    public Button Button2;
    public Button Button3;
    public Button Button4;

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
        switch(answerNumber){
            case 1:
                answerCountTicker1++;
                AnswerCount1.setText("X"+answerCountTicker1);
                break;
            case 2:
                answerCountTicker2++;
                AnswerCount1.setText("X"+answerCountTicker2);
                break;
            case 3:
                answerCountTicker3++;
                AnswerCount1.setText("X"+answerCountTicker3);
                break;
            case 4:
                answerCountTicker4++;
                AnswerCount1.setText("X"+answerCountTicker4);
                break;
        }
    }
}

