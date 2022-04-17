package ggc;

import java.io.Serializable;

public abstract class Notification implements Serializable {
    
    private Product _product;
    private double _price;

    public Notification(Product p, double price) {
        _product = p;
        _price = price;
    }

    public Product getProduct() {
        return _product;
    }

    public double getPrice() {
        return _price;
    }
    
    public abstract String showNotification();
    
}
