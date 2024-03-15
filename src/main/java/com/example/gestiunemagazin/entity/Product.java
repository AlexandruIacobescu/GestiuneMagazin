package com.example.gestiunemagazin.entity;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    private SimpleStringProperty name, category, expdate, manufacurer;
    private SimpleIntegerProperty id, units;
    private SimpleDoubleProperty price;

    public Product(Integer id, String name, String category, Integer units, Double price, String expdate, String manufacturer) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.units = new SimpleIntegerProperty(units);
        this.expdate = new SimpleStringProperty(expdate);
        this.manufacurer = new SimpleStringProperty(manufacturer);
        this.price = new SimpleDoubleProperty(price);
    }
    public int getID(){return this.id.get();}
    public String getName(){return this.name.get();}
    public String getCategory(){return this.category.get();}
    public int getUnits(){return this.units.get();}
    public String getExpDate(){return this.expdate.get();}
    public String getManufacturer(){return this.manufacurer.get();}
    public Double getPrice(){return this.price.get();}
}
