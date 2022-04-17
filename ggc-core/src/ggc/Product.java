package ggc;

import java.io.Serializable;

public class Product implements Serializable {

    private String _key;
    private double _maxPrice = 0;
    private int _totalStock = 0;

    public Product(String key) {
        _key = key;
    }

    public String getProductKey() {
        return _key;
    }

    public double getMaxPrice() {
        return _maxPrice;
    }

    public int getStock() {
        return _totalStock;
    }

    public void setMaxPrice(double price) {
        _maxPrice = price;
    }

    public void updateStock(int amount) {
        _totalStock += amount;
    }

    public String showProduct() {
        return _key + "|" + (int) Math.round(_maxPrice) + "|" + _totalStock;
    }

    public Recipe getRecipe() {
        return null;
    }

    public int getDateModifier() {
        return 5;
    }

}