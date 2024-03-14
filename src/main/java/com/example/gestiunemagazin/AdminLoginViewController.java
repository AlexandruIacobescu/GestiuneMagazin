package com.example.gestiunemagazin;

import com.example.gestiunemagazin.utils.Message;
import com.example.gestiunemagazin.utils.ViewHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

public class AdminLoginViewController {

    @FXML
    private Button loginButton, backButton;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    public void onLoginButtonClick(ActionEvent event) throws IOException {
        if(!usernameTextField.getText().equals("admin") || !passwordField.getText().equals("admin")) {
            new Message(errorLabel, 5000, "Invalid Login Credentials.", Color.DARKRED).show();
        } else {
            ViewHelper.openView(getClass(), event, "admin-view.fxml", "Administrator View");
        }
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        ViewHelper.openView(getClass(), event, "login-view.fxml", "Employee Login");
    }
}
