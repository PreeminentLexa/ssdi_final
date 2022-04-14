package com.example.finalproject.controllers;

// The Controller for Frame J

import com.example.finalproject.Utility;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class J_InputAnswersController extends UtilControllerBase {
    public Label Question;
    public TextField t1;
    public TextField t2;
    public TextField t3;
    public Button Next;
    public Button Prev;



    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getQuestion(String question){tmp = question;} // The question just entered

    private String tmp;
    public void testBack() {
        Utility.inputAnswer_back();
    }
    public void testPickAnswers() {
        Utility.inputAnswer_pickAnswers(tmp, "Great", "Good", "Fine", "Amazing", 4);
    }



    // Callable:
    // Utility.inputAnswer_back();
    //      Returns to c_mainmenu
    // Utility.inputAnswer_pickAnswers(String q, String a1, String a2, String a3, String a4, int correct);
    //      Finalizes the question/answer stuff
    //      q is the question (so you have to stick it in a member variable when you get it from Callback_getQuestion)
    //      a1-4 are the answers
    //      correct is the number corresponding to the correct answer (1-4)
}
