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
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class RegisterViewController {

    @FXML
    private Button backButton, registerButton;

    @FXML
    private TextField employeeIdTextField, usernameTextField;

    @FXML
    private PasswordField passwordPasswordField, confirmPasswordPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void onBackButtonClick(ActionEvent event) throws IOException {
        ViewHelper.openView(getClass(), event, "login-view.fxml", "Employee Login");
    }


    public void onRegisterButtonClick() {
        boolean ok = true;
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db");
            String sql;
            PreparedStatement stmt;

            if(passwordPasswordField.getText().length() <= 7) {
                new Message(errorLabel, 5000, "Password must be at least 8 characters long.", Color.DARKRED).show();
                ok = false;
                return;
            }
            if(!passwordPasswordField.getText().equals(confirmPasswordPasswordField.getText())) {
                new Message(errorLabel, 5000, "Passwords do not match.", Color.DARKRED).show();
                ok = false;
                return;
            }
            if(!employeeIdExistsInDb(conn, employeeIdTextField.getText())) {
                new Message(errorLabel, 5000, "Employee ID not in the database.", Color.DARKRED).show();
                ok = false;
                return;
            }
            if(employeeAlreadyHasAccount(conn, employeeIdTextField.getText())) {
                new Message(errorLabel, 5000, "Your ID is already associated with an account.", Color.DARKRED).show();
                ok = false;
                return;
            }
            sql = """
                    INSERT INTO Accounts(id,username,password_hash)
                    VALUES(?,?,?);
                """;
            String hashedPassword = BCrypt.hashpw(passwordPasswordField.getText(), BCrypt.gensalt());
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, employeeIdTextField.getText());
            stmt.setString(2, usernameTextField.getText());
            stmt.setString(3, hashedPassword);
            stmt.executeUpdate();
            stmt.close();
        }catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if(ok)
                new Message(errorLabel, 5000, "Account Created Successfully.", Color.GREEN).show();
        }
    }

    private boolean employeeAlreadyHasAccount(Connection conn, String id) throws SQLException {
        String sql = """
                            SELECT COUNT(*) 
                            FROM Accounts
                            WHERE Id = ?;
                            """;
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        ResultSet set = stmt.executeQuery();
        set.next();
        return set.getString(1).equals("1");
    }

    private boolean employeeIdExistsInDb(Connection conn, String id) throws SQLException {
        String sqlQuery = """
                    SELECT COUNT(*)
                    FROM Employees
                    WHERE ID = ?;
                    """;
        PreparedStatement stmt = conn.prepareStatement(sqlQuery);
        stmt.setString(1, id);
        ResultSet set = stmt.executeQuery();
        set.next();
        return set.getString(1).equals("1");
    }
}
