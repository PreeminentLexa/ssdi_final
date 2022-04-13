package com.example.finalproject.controllers;

// The Controller for frame G

import com.example.finalproject.Utility;

import java.net.URL;
import java.util.ResourceBundle;

public class G_InputQuestionController extends UtilControllerBase {
    
    public Button NextButton;
    public Label Question;

    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}


    public void Callback_getQuestion(String question){
        Question.setText(question);
    } // The question that was previously entered - used to return to this stage from J

    public void testPickQuestion() {
        Utility.inputQuestion_pickQuestion("How is your day going?");
    }

    // Callable:
    // Utility.inputQuestion_pickQuestion(String question);
    //      Switches to j_inputanswers
    //      The question is moved too (into Callback_getQuestion)
}
