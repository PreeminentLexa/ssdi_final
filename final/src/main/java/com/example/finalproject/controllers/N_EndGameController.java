package com.example.finalproject.controllers;

// The Controller for the frame after M, when the game is finished

import com.example.finalproject.User;
import com.example.finalproject.Utility;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class N_EndGameController extends UtilControllerBase {
    public Label text;
    public Label subtext;

    public void initialize(URL location, ResourceBundle resources) {
        int topScore = User.topScore();
        int myScore = User.getLocalUser().getScore();
        if(myScore == topScore){
            text.setText("YOU WIN!");
        } else {
            text.setText("You lose :c");
        }
        subtext.setText("With "+myScore+" points.");
    }
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void returnToMainMenu() {
        Utility.endGame_returnToMainMenu();
    }

    // Callable:
    // Utility.endGame_returnToMainMenu();
    //      Returns to c_mainmenu
}
