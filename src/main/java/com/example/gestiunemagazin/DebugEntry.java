package com.example.gestiunemagazin;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DebugEntry {
    public static void main(String[] args) {
            createDB();
    }

    public static void createDB() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:inventory.db");
            Statement statement = connection.createStatement();
            String sqlCreateProducts = """
                    CREATE TABLE IF NOT EXISTS Products
                    (
                        id              integer
                            primary key autoincrement,
                        name            VARCHAR(100) not null,
                        description     TEXT,
                        category        VARCHAR(50)  not null,
                        quantity        INT          not null,
                        expiration_date DATE,
                        buy_price       DECIMAL(10, 2),
                        buy_date        DATE,
                        price           DECIMAL(10, 2) not null,
                        manufacturer    VARCHAR(100),
                        weight          DECIMAL(10, 2),
                        size            VARCHAR(50),
                        color           VARCHAR(50),
                        rating          DECIMAL(3, 2),
                        location        VARCHAR(100)
                    );
                    """;
            statement.executeUpdate(sqlCreateProducts);
            statement.close();

            String sqlCreateCustomers = """
                    CREATE TABLE IF NOT EXISTS Customers (
                           id          INTEGER PRIMARY KEY AUTOINCREMENT,
                           first_name  VARCHAR(50) NOT NULL,
                           last_name   VARCHAR(50) NOT NULL,
                           email       VARCHAR(100) NOT NULL UNIQUE,
                           address     VARCHAR(255),
                           phone       VARCHAR(20)
                    );""";
            statement.executeUpdate(sqlCreateCustomers);
            statement.close();

            String sqlCreateOrders = """
                    CREATE TABLE IF NOT EXISTS Orders (
                            id          INTEGER PRIMARY KEY AUTOINCREMENT,
                            customer_id INTEGER NOT NULL,
                            order_date  DATE NOT NULL,
                            FOREIGN KEY (customer_id) REFERENCES Customers(id)
                    );
                    """;
            statement.executeUpdate(sqlCreateOrders);
            statement.close();

            String sqlCreateOrderItems = """
                    CREATE TABLE IF NOT EXISTS Order_Items (
                               id          INTEGER PRIMARY KEY AUTOINCREMENT,
                               order_id    INTEGER NOT NULL,
                               product_id  INTEGER NOT NULL,
                               quantity    INTEGER NOT NULL,
                               FOREIGN KEY (order_id) REFERENCES Orders(id),
                               FOREIGN KEY (product_id) REFERENCES Products(id)
                      );
                    """;
            statement.executeUpdate(sqlCreateOrderItems);
            statement.close();

            String sqlCreateEmployees = """
                    CREATE TABLE IF NOT EXISTS Employees (
                        id              VARCHAR(10) PRIMARY KEY,
                        firstname       VARCHAR(100) NOT NULL,
                        lastname        VARCHAR(100) NOT NULL,
                        email           VARCHAR(100) NOT NULL,
                        phone_number    VARCHAR(10) NOT NULL,
                        department      VARCHAR(50) NOT NULL,
                        salary          FLOAT(10,2) NOT NULL
                    );
                    """;
            statement.executeUpdate(sqlCreateEmployees);
            statement.close();

            String sqlCreateAccounts = """
                    CREATE TABLE IF NOT EXISTS Accounts (
                          id VARCHAR(10) PRIMARY KEY,
                          username VARCHAR(100) NOT NULL,
                          password_hash VARCHAR(100) NOT NULL,
                          FOREIGN KEY (id) REFERENCES Employees(id)
                      );
                    """;
            statement.executeUpdate(sqlCreateAccounts);
            statement.close();

        }
        catch (SQLException ex) {

        }
        catch (Exception ex) {

        }
    }
}
