package com.example.finalproject.controllers;

// The Controller for Frame J

import com.example.finalproject.Utility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class J_InputAnswersController extends UtilControllerBase {
    @FXML
    public Label Question;
    @FXML
    public TextField t1;
    @FXML
    public TextField t2;
    @FXML
    public TextField t3;
    @FXML
    public TextField t4;




    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getQuestion(String question){ // The question just entered
        Question.setText(question);
        saveAnswer = question;
    }

    private String saveAnswer;
    public void back() {
        Utility.inputAnswer_back();
    }

    private void submit(int correct){
        if(null == t1.getText()){return;}
        if(null == t2.getText()){return;}
        if(null == t3.getText()){return;}
        if(null == t4.getText()){return;}
        if(t1.getText().equals("")){return;}
        if(t2.getText().equals("")){return;}
        if(t3.getText().equals("")){return;}
        if(t4.getText().equals("")){return;}
        Utility.inputAnswer_pickAnswers(saveAnswer, t1.getText(), t2.getText(), t3.getText(), t4.getText(), correct); 
    }

    public void submit1(){
        submit(1);
    }
    public void submit2(){
        submit(2);
    }
    public void submit3(){
        submit(3);
    }
    public void submit4(){
        submit(4);
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
