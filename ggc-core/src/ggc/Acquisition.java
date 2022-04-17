package ggc;

public class Acquisition extends Transaction {

    public Acquisition(int key, int payDay, Partner partner, Product product, int amount, double price) {
        super(key, payDay, partner, product, amount, price);
    }

    public String showTransaction() {
        return "COMPRA|" + super.getKey() +"|" + super.getPartner().getKey() + "|" + super.getProductKey()+ "|" + 
        super.getAmount() + "|" + Math.round(super.getPrice()) + "|" + super.getPayDay();
    }

    public boolean isPaidByPartner() {
        return false;
    }

    public int getDeadLine() {
        return getPayDay();
    }
}
