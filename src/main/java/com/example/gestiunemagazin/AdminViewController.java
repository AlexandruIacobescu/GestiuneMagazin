package com.example.gestiunemagazin;

import com.example.gestiunemagazin.entity.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.FileNotFoundException;
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
    private Button reloadButton, backButton, insertButton, generateButton;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setHeaderText("Employee insertion operation failed.");
            alert.setContentText("Database error. Please expand for additional details.");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.write(ex.getMessage());
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();
        }
    }

    @FXML
    public void onReloadButtonClick() {
        populateTableViewWithEmployees();
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
