package ggc;

import java.io.Serializable;

public abstract class Transaction implements Serializable {

    private int _key;
    private int _payDay;
    private Partner _partner;
    private Product _product;
    private int _amount;
    private double _price;

    public Transaction(int key, int payDay, Partner partner, Product product, int amount, double price) {
        _key = key;
        _payDay = payDay;
        _partner = partner;
        _product = product;
        _amount = amount;
        _price = price;
    }

    public int getKey() {
        return _key;
    }

    public int getPayDay() {
        return _payDay;
    }

    public Partner getPartner() {
        return _partner;
    }

    public Product getProduct() {
        return _product;
    }

    public String getProductKey() {
        return _product.getProductKey();
    }

    public int getAmount() {
        return _amount;
    }

    public double getPrice() {
        return _price;
    }

    public void setPayDay(int payDay) {
        _payDay = payDay;
    }

    public abstract String showTransaction();

    public abstract boolean isPaidByPartner();

    public abstract int getDeadLine();
}
