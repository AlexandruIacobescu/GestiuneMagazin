package com.example.gestiunemagazin;

import com.example.gestiunemagazin.entity.OrderItem;
import com.example.gestiunemagazin.entity.Product;
import com.example.gestiunemagazin.utils.Message;
import com.example.gestiunemagazin.utils.UtilityFunctions;
import com.example.gestiunemagazin.utils.ViewHelper;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EmployeeViewController implements Initializable {

    @FXML
    private TableView<Product> productsTableView;
    @FXML
    private TableView<OrderItem> orderItemsTableView;

    @FXML
    private TableColumn<Product,String> nameCol, categoryCol, expDateCol, manufacturerCol;

    @FXML
    private TableColumn<Product,Integer> idCol, unitsCol;
    @FXML
    private TableColumn<OrderItem,Integer> idCol2, orderIdCol, productsIdCol, quantityCol;

    @FXML
    private TableColumn<Product,Double> priceCol;

    @FXML
    private Button executeButton, backButton, refreshDataButton, fetchOrdersButton, backButton2, displayOrderItemsButton;
    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<String> sqlCommandCategoryComboBox;
    @FXML
    private ComboBox<Integer> ordersComboBox;
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
                sb.append("INSERT INTO Products (\n\tname, \n\tcategory, \n\tquantity, \n\texpiration_date, \n\tprice, \n\tmanufacturer\n)");
                sb.append("\nVALUES(\n\t _,\n\t _,\n\t _,\n\t _,\n\t _,\n\t _\n);");
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
        switch (sqlCommandCategoryComboBox.getValue()) {
            case "INSERT", "UPDATE", "DELETE" -> {
                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db")){
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate(sqlCommandTextArea.getText());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    UtilityFunctions.showErrorMessageDialog(
                            "Exception Dialog",
                            "Database Operation Failed",
                            "Database error. Please expand for additional details.",
                            ex.getMessage()
                    );
                } finally {
                    populateProductsTableVIewWithData();
                }
            }
        }
    }

    @FXML
    public void onFetchOrdersButtonClick() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db")){
            String sql = """
                        SELECT id
                        FROM Orders;
                    """;
            Statement stmt = conn.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while(set.next()) {
                ordersComboBox.getItems().add(set.getInt("id"));
            }
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

    @FXML
    public void onDisplayOrderItemsButtonClick() {
        if (ordersComboBox != null) {
            int id = ordersComboBox.getValue();
            idCol2.setCellValueFactory(new PropertyValueFactory<>("Id"));
            orderIdCol.setCellValueFactory(new PropertyValueFactory<>("OrderId"));
            productsIdCol.setCellValueFactory(new PropertyValueFactory<>("ProductId"));
            quantityCol.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
            ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db")){
                String sql = """
                            SELECT * 
                            FROM order_items
                            WHERE order_id = ?
                        """;
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, ordersComboBox.getValue());
                ResultSet set = stmt.executeQuery();
                while(set.next()){
                    OrderItem orderItem = new OrderItem(
                        set.getInt("id"),
                        set.getInt("order_id"),
                        set.getInt("product_id"),
                        set.getInt("quantity")
                    );
                    orderItems.add(orderItem);
                }
                orderItemsTableView.setItems(orderItems);
            } catch(SQLException ex) {
                ex.printStackTrace();
                UtilityFunctions.showErrorMessageDialog(
                        "Exception Dialog",
                        "Database Operation Failed",
                        "Database error. Please expand for additional details.",
                        ex.getMessage()
                );
            }
        } else {
            new Message(errorLabel, 3500, "No Order Selected", Color.DARKRED).show();
        }
    }

    @FXML
    public void onRefreshDataClick() {
        populateProductsTableVIewWithData();
    }

    @FXML
    public void onBackButtonClick(ActionEvent event) throws IOException {
        ViewHelper.openView(getClass(), event, "login-view.fxml", "Employee Login");
    }

    private void populateProductsTableVIewWithData() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:inventory.db")) {
            idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
            categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
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
