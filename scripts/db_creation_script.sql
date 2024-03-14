create table Products
(
    id              INTEGER PRIMARY KEY AUTOINCREMENT,        
    name            VARCHAR(100)   NOT NULL,
    description     TEXT,
    category        VARCHAR(50)    NOT NULL,
    quantity        INT            NOT NULL,
    expiration_date DATE,
    buy_price       DECIMAL(10, 2),
    buy_date        DATE,
    price           DECIMAL(10, 2) NOT NULL,
    manufacturer    VARCHAR(100),
    weight          DECIMAL(10, 2),
    size            VARCHAR(50),
    color           VARCHAR(50),
    rating          DECIMAL(3, 2),
    location        VARCHAR(100)
);



CREATE TABLE Customers (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name  VARCHAR(50) NOT NULL,
    last_name   VARCHAR(50) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    address     VARCHAR(255),
    phone       VARCHAR(20)
);


CREATE TABLE Orders (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_id INTEGER NOT NULL,
    order_date  DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customers(id)
);

CREATE TABLE Order_Items (
     id          INTEGER PRIMARY KEY AUTOINCREMENT,
     order_id    INTEGER NOT NULL,
     product_id  INTEGER NOT NULL,
     quantity    INTEGER NOT NULL,
     FOREIGN KEY (order_id) REFERENCES Orders(id),
     FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE IF NOT EXISTS Employees (
    id              VARCHAR(10) PRIMARY KEY,
    firstname       VARCHAR(100) NOT NULL,
    lastname        VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    phone_number    VARCHAR(10) NOT NULL,
    department      VARCHAR(50) NOT NULL,
    salary          FLOAT(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS Accounts (
    id VARCHAR(10) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    FOREIGN KEY (id) REFERENCES Employees(id)
);
