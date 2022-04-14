package com.example.finalproject.controllers;

// The Controller for frame C

import com.example.finalproject.Utility;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * @author Azan
 */
public class C_MainMenuController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}


    public void newGameAction(){
        Utility.mainMenu_newGame();
    }

    public void joinGameAction(){
        Utility.mainMenu_joinGame();
    }
    public void disconnect(ActionEvent event){
        Utility.mainMenu_disconnect();
    }

    // Callable:
    // Utility.mainMenu_NewGame();
    //      Switches to d_creategame
    // Utility.mainMenu_JoinGame();
    //      Switches to f_joingame
    // Utility.mainMenu_Disconnect();
    //      Switches to a_connectscreen

}
