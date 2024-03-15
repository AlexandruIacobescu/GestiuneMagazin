package com.example.gestiunemagazin;

import com.example.gestiunemagazin.entity.Product;
import com.example.gestiunemagazin.utils.UtilityFunctions;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EmployeeViewController implements Initializable {

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product,String> nameCol, categoryCol, expDateCol, manufacturerCol;

    @FXML
    private TableColumn<Product,Integer> idCol, unitsCol;

    @FXML
    private TableColumn<Product,Double> priceCol;

    @FXML
    private Button executeButton, backButton, refreshDataButton;

    @FXML
    private ComboBox<String> sqlCommandCategoryComboBox;
    @FXML
    private TextArea sqlCommandTextArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateProductsTableVIewWithData();
        loadSqlCommandCategoryComboBox();
    }

    @FXML
    public void onSqlCommandCategoryComboBoxIndexChanged() {
        StringBuilder sb = new StringBuilder();
        switch (sqlCommandCategoryComboBox.getValue()) {
            case "INSERT" -> {
                sb.append("INSERT INTO Products (\n\tname, \n\tdescription, \n\tcategory, \n\tquantity, \n\texpiration_date, \n\tbuy_price, \n\tbuy_date, \n\tprice, \n\tmanufacturer, \n\tweight, \n\tsize, \n\tcolor, \n\trating, \n\tlocation\n)");
                sb.append("\nVALUES(\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _\n);");
                sqlCommandTextArea.setText(sb.toString());
            }
            case "UPDATE" -> {
                sb.append("UPDATE Products SET \n");
                sb.append("name = _ , \nquantity = _ \n");
                sb.append("WHERE id = _;");
                sqlCommandTextArea.setText(sb.toString());
            }
            case "DELETE" -> {
                sb.append("DELETE FROM Products WHERE id = _ ;");
                sqlCommandTextArea.setText(sb.toString());
            }
        }
    }

    @FXML
    public void onExecuteButtonClick() {
        Statement stmt;
        switch (sqlCommandCategoryComboBox.getValue()) {
            case "INSERT", "UPDATE", "DELETE" -> {
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db")) {
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sqlCommandTextArea.getText());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    UtilityFunctions.showErrorMessageDialog(
                            "Exception Dialog",
                            "Database Operation Failed",
                            "Database error. Please expand for additional details.",
                            ex.getMessage()
                    );
                }
            }
        }
    }

    @FXML
    public void onRefreshDataClick() {
        populateProductsTableVIewWithData();
    }

    private void populateProductsTableVIewWithData() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db")) {
            idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
            unitsCol.setCellValueFactory(new PropertyValueFactory<>("Units"));
            expDateCol.setCellValueFactory(new PropertyValueFactory<>("ExpDate"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
            manufacturerCol.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
            ObservableList<Product> products = FXCollections.observableArrayList();

            String sql = """
                        SELECT id,name,category,quantity,expiration_date,price,manufacturer
                        FROM Products;
                    """;
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            Product product;
            while(set.next()) {
                product = new Product(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getString("category"),
                    set.getInt("quantity"),
                    set.getDouble("price"),
                    set.getString("expiration_date"),
                    set.getString("manufacturer")
                );
                products.add(product);
            }
            productsTableView.setItems(products);
        } catch (SQLException ex) {
            UtilityFunctions.showErrorMessageDialog(
                    "Exception Dialog",
                    "Database Operation Failed",
                    "Database error. Please expand for additional details.",
                    ex.getMessage()
            );
        }
    }

    private void loadSqlCommandCategoryComboBox() {
        sqlCommandCategoryComboBox.getItems().add("INSERT");
        sqlCommandCategoryComboBox.getItems().add("DELETE");
        sqlCommandCategoryComboBox.getItems().add("UPDATE");
    }
}
