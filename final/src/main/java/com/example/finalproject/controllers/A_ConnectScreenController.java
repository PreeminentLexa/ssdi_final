package com.example.finalproject.controllers;

// The Controller for frame A

import com.example.finalproject.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class A_ConnectScreenController extends UtilControllerBase {
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this

    public void Callback_errorMessage(String message){} // for example, failed to connect
    public void Callback_previousConnectInputs(String ip, String username, int imageIndex){}

    public void testJoinServer() {
        Utility.joinServer("localhost:8081", "Lexa", 1);
    }

    // Callable:
    // Utility.joinServer(String IP, String Username, int imageNumber);
    //      Attempts to join the server, the IP is the IP and port, like localhost:8081
    //      The username is just a username
    //      The image number isn't implemented, but there should be a list of images or something, and the image number is just the index of the selected one
}