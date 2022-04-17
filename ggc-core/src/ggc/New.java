package ggc;

public class New extends Notification {
    
    public New(Product p, double price) {
        super(p, price);
    }

    public String showNotification() {
        return "NEW" + "|" + getProduct().getProductKey() + "|" + Math.round(getPrice());
    }
}
