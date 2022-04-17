package ggc;

public class First extends Notification {
    
    public First(Product p, double price) {
        super(p, price);
    }

    public String showNotification() {
        return "FIRST" + "|" + getProduct().getProductKey() + "|" + Math.round(getPrice());
    }
}