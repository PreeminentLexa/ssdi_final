package com.example.finalproject;

import com.example.finalproject.controllers.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class FinalProjectApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Utility.initialize(stage);
    }

    public static void main(String[] args) {
        Utility.createThinkHook();
        launch();
    }
}