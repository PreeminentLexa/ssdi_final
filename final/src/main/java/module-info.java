module com.example.finalproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;

    opens com.example.finalproject to javafx.fxml;
    exports com.example.finalproject;
    exports com.example.finalproject.controllers;
    opens com.example.finalproject.controllers to javafx.fxml;
}