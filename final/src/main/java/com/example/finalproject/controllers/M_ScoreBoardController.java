package com.example.finalproject.controllers;

// The Controller for frame M

import com.example.finalproject.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class M_ScoreBoardController extends UtilControllerBase {
    private User[] users;
    private int done = 0;
    private String getScoreText(User user){
        return (user.isHost()?"[Host] ":"[User] ")+
                user.getUsername()+"   "+
                user.getScore();
    }
    public void initialize(URL location, ResourceBundle resources){
        User[] allUsers = User.getAllUsers();
        users = new User[allUsers.length];




        // Sort allUsers into users so that score is decending
        // users[0] should be top score




        int ticker = 1;
        for(User user : users){
            System.out.println(user);
            switch(ticker){
                case 1:
                    user1.setText(getScoreText(user));
                    break;
                case 2:
                    user2.setText(getScoreText(user));
                    break;
                case 3:
                    user3.setText(getScoreText(user));
                    break;
                case 4:
                    user4.setText(getScoreText(user));
                    break;
            }
            ticker++;
        }
//        user1.setText();
    }
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}
    @FXML
    Label user1;
    @FXML
    Label user2;
    @FXML
    Label user3;
    @FXML
    Label user4;
//
//    @FXML
//    public void setScoreBoard(){
//        User[] user = User.getAllUsers();
//        int n = user.length;
//        int temp = 0;
//        for(int i=0; i < n; i++){
//            for(int j=1; j < (n-i); j++){
//                if(user[j-1].getScore() > user[j].getScore()){
//                    //swap elements
//                    temp = user[j-1].getScore();
//                    user[j-1].setScore(user[j].getScore());
//                    user[j].setScore(temp);
//                }
//
//            }
//        }
//        user1.setText(Integer.toString(user[0].getScore()));
//        user2.setText(Integer.toString(user[1].getScore()));
//        user3.setText(Integer.toString(user[2].getScore()));
//        user4.setText(Integer.toString(user[3].getScore()));
//
//
//    }
//
//    public void nextButtonAction(ActionEvent actionEvent) {
//    }

    // This page has Utility.timeOnScoreboardPage seconds to animate (I set it to 10 seconds originally)
}