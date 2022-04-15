package com.example.finalproject.controllers;

// The Controller for frame F

import com.example.finalproject.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * @author Azan
 */
public class F_JoinGameController extends UtilControllerBase {
    public Label label1;
    @FXML
    private TextField codeField;
    @FXML
    private TextField passwordField;
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}

    public void Callback_errorPrevJoinGameInputs(String code, String password){ // If the join fails, this gives the code and password tried (so they're not wiped)
        codeField.setText(code);
        codeField.setText(password);
    }
    public void Callback_errorMessage(String message){ // for example, invalid code/invalid password
        label1.setText(message);
    }

    @FXML
    public void prevButtonAction(ActionEvent event) throws IOException{
        Utility.joinGame_back();
    }
    @FXML
    public void joinButtonAction(ActionEvent event) throws IOException{
        Utility.joinGame_join(codeField.getText(),passwordField.getText());
    }
    // Callable:
    // Utility.joinGame_back()
    //      Returns to c_mainmenu
    // Utility.joinGame_join(String code, String password)
    //      attempts to join a game with the given code
    //      If the game is unprotected, then the password doesn't matter
}
