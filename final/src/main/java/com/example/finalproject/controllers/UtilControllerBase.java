package com.example.finalproject.controllers;

import com.example.finalproject.GameSettings;
import com.example.finalproject.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;

public class UtilControllerBase implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    public void Callback_connectionLost(){} // Any Controller. Called before swapping to a_connectscreen.fxml
    public void Callback_gameClosed(){} // Any Controller. Called before swapping to c_mainmenu.fxml
    public void Callback_errorMessage(String message){} // Controllers A, F. Called after swapping to A or F

    public void Callback_previousConnectInputs(String ip, String username, int imageIndex){} // A
    public void Callback_errorPrevJoinGameInputs(String code, String password){} // F
    public void Callback_getGameCode(String code){} // E
    public void Callback_getGameSettings(GameSettings settings){} // E
    public void Callback_userJoined(User user){} // E
    public void Callback_userLeft(User user){} // E
    public void Callback_waitingForQuestioner(User user){} // H
    public void Callback_getQuestion(String question){} // G, J, K, I, L
    public void Callback_getAnswers(String a1, String a2, String a3, String a4, int correct){} // K, I, L
    public void Callback_someoneAnswered(int picked){} // K
    public void Callback_getAnswerCount(int a1count, int a2count, int a3count, int a4Count){} // L

    public void Util_CountDownTimeLeft(long millisecondsLeft, Instant targetTime){} // Util Countdown
}
