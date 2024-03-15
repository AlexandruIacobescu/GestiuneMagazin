package com.example.gestiunemagazin.entity;

import javafx.beans.property.SimpleIntegerProperty;

public class OrderItem {
    private SimpleIntegerProperty id, orderId, productId, quantity;

    public OrderItem(int id, int orderId, int productId, int quantity){
        this.id = new SimpleIntegerProperty(id);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.productId = new SimpleIntegerProperty(productId);
        this.quantity = new SimpleIntegerProperty(quantity);
    }
    public Integer getId(){return id.get();}
    public Integer getOrderId(){return orderId.get();}
    public Integer getProductId(){return productId.get();}
    public Integer getQuantity(){return quantity.get();}
}
