package com.example.finalproject.controllers;

// The Controller for frame G

import com.example.finalproject.Utility;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class G_InputQuestionController extends UtilControllerBase {

    @FXML
    protected TextField Question;
    @FXML
    protected Button NextButton;

    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}


    public void Callback_getQuestion(String question){ // The question that was previously entered - used to return to this stage from J
        Question.setText(question);
    }

    public void submitQuestion() {
        if(Question.getText().equals("")){return;}
        Utility.inputQuestion_pickQuestion(Question.getText());
    }

    // Callable:
    // Utility.inputQuestion_pickQuestion(String question);
    //      Switches to j_inputanswers
    //      The question is moved too (into Callback_getQuestion)
}
