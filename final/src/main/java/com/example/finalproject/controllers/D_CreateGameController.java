package com.example.finalproject.controllers;

// The Controller for frame D

import com.example.finalproject.Utility;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * @author Azan
 */
public class D_CreateGameController extends UtilControllerBase {
    private TextField roundsField;
    private TextField roundTimeField;
    private TextField passwordField;
    private Button prevButton;
    private Button nextButton;
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}


    public void nextButtonAction(ActionEvent event) throws IOException{
        String[] settings = new String[]{
                "rounds|i|" + roundsField.getText(),
                "answertime|i|" + roundTimeField.getText(),
        };
        String password = passwordField.getText();
        Utility.createGame_create(settings,password);
    }

    public void prevButtonAction(ActionEvent event) throws IOException{
        Utility.createGame_back();
    }

    // Callable:
    // Utility.createGame_back();
    //      Returns to c_mainmenu
    // Utility.createGame_create(String[] settings, String password);
    //      Settings are in the format "key|type|value" where the type is "s", "i" or "f" for string, int, float
    //      E.G. {"rounds|i|5", "gametitle|s|Super game!"}
    //      Any setting key is permitted, it's just an index that can be read from later
    //      Empty password can be "" or null
}
