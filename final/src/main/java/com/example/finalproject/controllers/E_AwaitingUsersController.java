package com.example.finalproject.controllers;

// The Controller for frame E

import com.example.finalproject.GameSettings;
import com.example.finalproject.User;
import com.example.finalproject.Utility;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * @author Azan
 */
public class E_AwaitingUsersController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    public void Callback_getGameCode(String code){} // The game code
    public void Callback_getGameSettings(GameSettings settings){} // The game settings
    public void Callback_userJoined(User user){} // When a user joins
    public void Callback_userLeft(User user){} // When a user leaves

    public void testBack() {
        Utility.awaitingUsers_back();
    }
    public void testStartGame() {
        Utility.awaitingUsers_startGame();
    }


    //Previous Button in the Bottom Left
    public void prevButtonAction(ActionEvent event) throws IOException{
        Utility.awaitingUsers_back();


    }

    //Start Button
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
