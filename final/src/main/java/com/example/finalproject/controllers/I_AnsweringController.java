package com.example.finalproject.controllers;

// The Controller for frame I

import com.example.finalproject.Utility;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
//Coded by: Bridget
public class I_AnsweringController extends UtilControllerBase {
    public Label Question;
    public Button Button1;
    public Button Button2;
    public Button Button3;
    public Button Button4;


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

    } // correct will be -1, because it shouldn't be used here

    private void disableButtons(){
        Button1.setDisable(true);
        Button2.setDisable(true);
        Button3.setDisable(true);
        Button4.setDisable(true);
    }

    public void answer1(){
        Utility.answering_pick(1);
        disableButtons();
    }
    public void answer2(){
        Utility.answering_pick(2);
        disableButtons();
    }
    public void answer3(){
        Utility.answering_pick(3);
        disableButtons();
    }
    public void answer4(){
        Utility.answering_pick(4);
        disableButtons();
    }

    // Callable:
    // Utility.answering_pick(int number);
    //      Used to pick a specific answer. This is sent to the server
    //      number corresponds to the picked answer 1-4
}

