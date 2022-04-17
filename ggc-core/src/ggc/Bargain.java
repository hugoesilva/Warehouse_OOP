package ggc;

public class Bargain extends Notification {

    public Bargain(Product p, double price) {
        super(p, price);
    }

    public String showNotification() {
        return "BARGAIN" + "|" + getProduct().getProductKey() + "|" + Math.round(getPrice());
    }
    
}
