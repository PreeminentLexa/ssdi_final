package com.example.finalproject.controllers;

// The Controller for frame E

import com.example.finalproject.GameSettings;
import com.example.finalproject.User;
import com.example.finalproject.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * @author Azan
 */
public class E_AwaitingUsersController extends UtilControllerBase {
    @FXML
    Label codeLabel;
    @FXML
    Label user1;
    @FXML
    Label user2;
    @FXML
    Label user3;
    @FXML
    Label user4;
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getGameCode(String code){
        codeLabel.setText(code);
    } // The game code
    public void Callback_getGameSettings(GameSettings settings){} // The game settings
    public void Callback_userJoined(User user){
        user1.setText(user.getUsername());
    } // When a user joins
    public void Callback_userLeft(User user){} // When a user leaves

    //Previous Button in the Bottom Left
    @FXML
    public void prevButtonAction(ActionEvent event) throws IOException{
        Utility.awaitingUsers_back();


    }
    //Start Button
    @FXML
    private void startButtonAction(ActionEvent event) throws IOException{
        Utility.awaitingUsers_startGame();
    }



    // Callable:
    // Utility.awaitingUsers_back();
    //      Returns to c_mainmenu
    // Utility.awaitingUsers_startGame();
    //      Prompts the server to start the first round
    //      Only works for the host


}
