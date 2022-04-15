package com.example.finalproject.controllers;

// The Controller for frame M

import com.example.finalproject.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @author Azan
 */
public class M_ScoreBoardController extends UtilControllerBase {
    @FXML
    Label user1;
    @FXML
    Label user2;
    @FXML
    Label user3;
    @FXML
    Label user4;
    private User[] users;
    private int done = 0;
    @FXML
    private String getScoreText(User user){
        return (user.isHost()?"[Host] ":"[User] ")+
                user.getUsername()+"   "+
                user.getScore();
    }
    @FXML
    public void initialize(URL location, ResourceBundle resources){
        User[] allUsers = User.getAllUsers();
        users = new User[allUsers.length];

        //test scores
//        allUsers[0].setScore(5);
//        allUsers[1].setScore(2);
//        allUsers[2].setScore(3);


        int n = allUsers.length;
        User temp;
        // Sort allUsers into users so that score is decending
        for(int i=0; i < n-1; i++){
            for(int j=0; j < (n-i-1); j++){
                if(allUsers[j].getScore() < allUsers[j+1].getScore()){
                    //swap elements
                    temp = allUsers[j];
                    allUsers[j] = allUsers[j+1];
                    allUsers[j+1] = temp;

                }

            }
        }
        for(int i = 0;i<=n;i++){
            if(i==0){
                user1.setText(getScoreText(allUsers[i]));
            }else if(i==1){
                user2.setText(getScoreText(allUsers[i]));
            }else if(i==2){
                user3.setText(getScoreText(allUsers[i]));
            }else if(i==3){
                user4.setText(getScoreText(allUsers[i]));
            }
        }

    }
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}


    // This page has Utility.timeOnScoreboardPage seconds to animate (I set it to 10 seconds originally)
}