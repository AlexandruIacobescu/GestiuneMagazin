package com.example.gestiunemagazin;

import com.example.gestiunemagazin.entity.Employee;
import com.example.gestiunemagazin.utils.Message;
import com.example.gestiunemagazin.utils.UtilityFunctions;
import com.example.gestiunemagazin.utils.ViewHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;

public class AdminViewController implements Initializable {

    @FXML
    private TableView<Employee> tableView;

    @FXML
    private TextArea sqlScriptTextArea, sqlResultSetTextArea;

    @FXML
    private Label infoLabel;

    @FXML
    private Button reloadButton, backButton, insertButton, generateButton, clearFieldsButton, executeButton;

    @FXML
    private TextField firstnameTextField, lastnameTextField, departmentTextField, idTextField, emailTextField, phoneNumberTextField, salaryTextField;

    @FXML
    private TableColumn<Employee, String> idCol, fnmCol, lnmCol, emailCol, phoneCol, deptCol;

    @FXML
    private TableColumn<Employee, Double> salCol;

    @FXML
    private boolean onTextFieldKeyTyped() {
        if(firstnameTextField.getText().length() >= 2 && lastnameTextField.getText().length() >= 2 && departmentTextField.getText().length() >= 2) {
            generateButton.setVisible(true);
            return true;
        } else {
            generateButton.setVisible(false);
            idTextField.setText("");
            return false;
        }
    }

    @FXML
    public void onGenerateButtonClick() {
        if(onTextFieldKeyTyped()) {
            String employeeId = firstnameTextField.getText().substring(0,2) +
                    lastnameTextField.getText().substring(0,2) +
                    departmentTextField.getText().substring(0,2);

            int randInt = new Random().nextInt(9000) + 1000;
            employeeId += String.valueOf(randInt);

            idTextField.setText(employeeId);
        }
    }

    @FXML
    public void onInsertButtonClick() {
        boolean show = true;
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db");
            String sql = """
                        INSERT INTO Employees(id,firstname,lastname,email,phone_number,department,salary)
                        VALUES(?,?,?,?,?,?,?);
                    """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idTextField.getText());
            stmt.setString(2, firstnameTextField.getText());
            stmt.setString(3, lastnameTextField.getText());
            stmt.setString(4, emailTextField.getText());
            stmt.setString(5, phoneNumberTextField.getText());
            stmt.setString(6, departmentTextField.getText());
            stmt.setString(7, salaryTextField.getText());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            UtilityFunctions.showErrorMessageDialog(
                    "Exception Dialog",
                    "Employee insertion operation failed.",
                    "Database error. Please expand for additional details.",
                    ex.getMessage()
            );
            show = false;
        } finally {
            if(show){
                populateTableViewWithEmployees();
                new Message(infoLabel, 5000, "Insertion Successful.", Color.DARKGREEN).show();
            }
        }
    }

    @FXML
    public void onBackButtonClick(ActionEvent event) throws IOException {
        ViewHelper.openView(getClass(), event, "login-view.fxml", "Employee Login");
    }

    @FXML
    public void onReloadButtonClick() {
        populateTableViewWithEmployees();
    }

    @FXML
    public void onClearAllFieldsButtonClick(){
        firstnameTextField.setText("");
        lastnameTextField.setText("");
        departmentTextField.setText("");
        idTextField.setText("");
        emailTextField.setText("");
        phoneNumberTextField.setText("");
        salaryTextField.setText("");
    }

    @FXML
    public void onExecuteButtonClick() {
        sqlResultSetTextArea.clear();
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db");
            String sql = sqlScriptTextArea.getText();

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet set = stmt.executeQuery();

            ResultSetMetaData metaData = set.getMetaData();
            int columnsNumber = metaData.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columnsNumber; i++) {
                sqlResultSetTextArea.appendText(String.format("%-20s", metaData.getColumnName(i)));
            }
            sqlResultSetTextArea.appendText("\n");

            // Print rows
            while (set.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = set.getString(i);
                    sqlResultSetTextArea.appendText(String.format("%-20s", columnValue));
                }
                sqlResultSetTextArea.appendText("\n");
            }

        } catch (SQLException ex) {
            sqlResultSetTextArea.setText(ex.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateTableViewWithEmployees();
    }

    private void populateTableViewWithEmployees() {
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db");
            String sql = "SELECT * FROM Employees;";

            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(sql);

            idCol.setCellValueFactory(new PropertyValueFactory<>("Eid"));
            fnmCol.setCellValueFactory(new PropertyValueFactory<>("Firstname"));
            lnmCol.setCellValueFactory(new PropertyValueFactory<>("Lastname"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
            deptCol.setCellValueFactory(new PropertyValueFactory<>("Department"));
            salCol.setCellValueFactory(new PropertyValueFactory<>("Sal"));

            ObservableList<Employee> employees = FXCollections.observableArrayList();
            Employee employee;
            while(set.next()) {
                employee = new Employee(
                        set.getString("id"),
                        set.getString("firstname"),
                        set.getString("lastname"),
                        set.getString("department"),
                        set.getDouble("salary"),
                        set.getString("email"),
                        set.getString("phone_number")
                );
                employees.add(employee);
            }
            tableView.setItems(employees);
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


}
