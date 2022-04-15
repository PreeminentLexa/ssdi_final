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
    //Counter to tell how many users joined

    String userID1;
    String userID2;
    String userID3;
    String userID4;
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}
    //set the code to the code from the server and display it
    @FXML
    public void Callback_getGameCode(String code){
        codeLabel.setText(code);
    } // The game code

    public void Callback_getGameSettings(GameSettings settings){} // The game settings
    //When a user joins
    @FXML
    public void Callback_userJoined(User user){
        for(int i = 0; i < 4; i++){
            if(i==0 && user1.getText()==""){
                user1.setText(user.getUsername());
                userID1 = user.getUID();
            }else if(i==1 && user2.getText()==""){
                user2.setText(user.getUsername());
                userID2 = user.getUID();
            }else if(i==2 && user3.getText()==""){
                user3.setText(user.getUsername());
                userID3 = user.getUID();
            }else if(i==3 && user4.getText()==""){
                user4.setText(user.getUsername());
                userID4 = user.getUID();
            }
        }

    } // When a user leaves
    @FXML
    public void Callback_userLeft(User user){
        for(int i = 0; i < 4;i++){
            if(i==0 && user.getUID() ==userID1){
                user1.setText("");
                userID1 = "";
            }else if(i==1 && user.getUID() ==userID2){
                user2.setText("");
                userID2 = "";
            }else if(i==2 && user.getUID() ==userID3){
                user3.setText("");
                userID3 = "";

            }else if(i==3 && user.getUID() ==userID4){
                user4.setText("");
                userID4 = "";

            }
        }


    }

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
