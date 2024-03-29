package com.example.finalproject.controllers;

// The Controller for frame E

import com.example.finalproject.GameSettings;
import com.example.finalproject.User;
import com.example.finalproject.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * @author Azan
 */
public class E_AwaitingUsersController extends UtilControllerBase {
    @FXML
    public Button startButton;
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
    @FXML
    Label roundsLabel;
    @FXML
    Label roundTimeLabel;


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


    //Displays the setting on screen (Retrieved from Server)
    @FXML
    public void Callback_getGameSettings(GameSettings settings){
        roundsLabel.setText(Integer.toString(settings.getiSetting("rounds")));
        roundTimeLabel.setText(Integer.toString(settings.getiSetting("answertime")));

    }


    private int userToWrite = 1;
    //When a user joins
    @FXML
    public void Callback_userJoined(User user){
        if(user.getUID() == userID1){return;}
        if(user.getUID() == userID2){return;}
        if(user.getUID() == userID3){return;}
        if(user.getUID() == userID4){return;}
        switch(userToWrite){
            case 1:
                if(!User.getLocalUser().isHost()){
                    startButton.setVisible(false);
                }
                user1.setText(user.getUsername());
                userID1 = user.getUID();
                break;
            case 2:
                user2.setText(user.getUsername());
                userID2 = user.getUID();
                break;
            case 3:
                user3.setText(user.getUsername());
                userID3 = user.getUID();
                break;
            case 4:
                user4.setText(user.getUsername());
                userID4 = user.getUID();
                break;
        }
        userToWrite++;
//        for(int i = 0; i < 4; i++){
//            if(i==0 && user1.getText()==""){
//                user1.setText(user.getUsername());
//                userID1 = user.getUID();
//                i=4;
//            }else if(i==1 && user2.getText()==""){
//                user2.setText(user.getUsername());
//                userID2 = user.getUID();
//                i=4;
//            }else if(i==2 && user3.getText()==""){
//                user3.setText(user.getUsername());
//                userID3 = user.getUID();
//                i=4;
//
//            }else if(i==3 && user4.getText()==""){
//                user4.setText(user.getUsername());
//                userID4 = user.getUID();
//                i=4;
//
//            }
//        }

    } // When a user leaves
    @FXML
    public void Callback_userLeft(User user){
        if(user.getUID().equals(userID1)){
            user1.setText(user2.getText());
            userID1 = userID2;
            user2.setText(user3.getText());
            userID2 = userID3;
            user3.setText(user4.getText());
            userID3 = userID4;
            userID4 = "";
            userToWrite--;
        } else if(user.getUID().equals(userID2)){
            user2.setText(user3.getText());
            userID2 = userID3;
            user3.setText(user4.getText());
            userID3 = userID4;
            user4.setText("");
            userID4 = "";
            userToWrite--;
        } else if(user.getUID().equals(userID3)){
            user3.setText(user4.getText());
            userID3 = userID4;
            user4.setText("");
            userID4 = "";
            userToWrite--;
        } else if(user.getUID().equals(userID4)){
            user4.setText("");
            userID4 = "";
            userToWrite--;
        }
    } // When a user leaves

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
