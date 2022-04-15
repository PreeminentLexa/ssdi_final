package com.example.finalproject.controllers;

// The Controller for frame A

import com.example.finalproject.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Azan
 */
public class A_ConnectScreenController extends UtilControllerBase {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField ipField;
    @FXML
    private Button goButton;
    @FXML
    private Label label1;
    String username;
    String IP;

    //Action for when the button in Scene A is pressed
    @FXML
    public void goButtonAction(ActionEvent event){
        try{
            //Get Username from Textfield
            username = usernameField.getText();
            //Get ip from textfield
            IP = ipField.getText();
            //join server using inputs (Default Image is 1 for now)
            Utility.joinServer(IP, username, 1);
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this

    public void Callback_errorMessage(String message){
        label1.setText(message);
    } // for example, failed to connect
    public void Callback_previousConnectInputs(String ipAdd, String username, int imageIndex){
        ipField.setText(ipAdd);
        usernameField.setText(username);
    }


    // Callable:
    // Utility.joinServer(String IP, String Username, int imageNumber);
    //      Attempts to join the server, the IP is the IP and port, like localhost:8081
    //      The username is just a username
    //      The image number isn't implemented, but there should be a list of images or something, and the image number is just the index of the selected one
}