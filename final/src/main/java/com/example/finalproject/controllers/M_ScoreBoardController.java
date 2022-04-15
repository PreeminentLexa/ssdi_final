package com.example.finalproject.controllers;

// The Controller for frame M

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class M_ScoreBoardController extends UtilControllerBase {
    public Button Next;
    public TextField t1;
    public TextField t2;
    public TextField t3;
    public TextField t4;
    public static Label L1;
    public static Label L2;
    public static Label L3;
    public static Label L4;
    public void initialize(URL location, ResourceBundle resources) {} // Don't have to use this
    public void Callback_connectionLost(){}
    public void Callback_gameClosed(){}

    // This page has Utility.timeOnScoreboardPage seconds to animate (I set it to 10 seconds originally)
}
