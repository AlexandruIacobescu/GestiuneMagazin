package com.example.gestiunemagazin.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewHelper {

    public static void openView(Class<?> runtimeClass, ActionEvent event, String fxmlResourcePath, String windowTitle) throws IOException {
        Parent root = FXMLLoader.load(runtimeClass.getResource(fxmlResourcePath));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
