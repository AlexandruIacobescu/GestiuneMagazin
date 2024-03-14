package com.example.gestiunemagazin;

import com.example.gestiunemagazin.entity.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminViewController implements Initializable {

    @FXML
    private TableView<Employee> tableView;

    @FXML
    private Button reloadButton, backButton, insertButton, generateButton;

    @FXML
    private TextField firstnameTextField, lastnameTextField, departmentTextField, idTextField, emailtextField, phoneNumberTextField, salarytextField;

    @FXML
    private TableColumn<Employee, String> idCol, fnmCol, lnmCol, emailCol, phoneCol, deptCol;

    @FXML
    private TableColumn<Employee, Double> salCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
