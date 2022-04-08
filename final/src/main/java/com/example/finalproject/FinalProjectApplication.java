package com.example.finalproject;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class FinalProjectApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Utility.stage = stage;
        Utility.swapToPage(Utility.initialScreen);
    }

    public static void main(String[] args) {
        launch();
    }
}