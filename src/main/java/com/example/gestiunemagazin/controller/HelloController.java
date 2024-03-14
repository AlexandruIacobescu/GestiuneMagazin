package com.example.gestiunemagazin.controller;

import com.example.gestiunemagazin.utils.Message;
import com.example.gestiunemagazin.utils.ViewHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class HelloController {
    @FXML
    private Label errorLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void onLoginButtonClick(ActionEvent event) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db");
            String sql = """
                        SELECT username, password_hash
                        FROM Accounts
                        WHERE username = ?
                    """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usernameTextField.getText());
            ResultSet set = stmt.executeQuery();

            if(set.next()) {
                String username = set.getString("username");
                String passwordHash = set.getString("password_hash");
                if(BCrypt.checkpw(passwordField.getText(), passwordHash)) {
                    Parent root = FXMLLoader.load(getClass().getResource("C:\\Users\\alexi\\IdeaProjects\\GestiuneMagazin\\src\\main\\resources\\com\\example\\gestiunemagazin\\employee-view.fxml"));
                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setTitle("Products Manager Toolkit");
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                } else {
                    Message msg = new Message(errorLabel, 5000, "Login Credentials Not Valid.", Color.RED);
                    msg.show();
                }
            } else {
                Message msg = new Message(errorLabel, 5000, "Login Credentials Not Valid.", Color.RED);
                msg.show();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            Message msg = new Message(errorLabel, 5000, "Database Connection Error. " +
                    "\nPlease Contact Administrator.", Color.RED);
            msg.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}