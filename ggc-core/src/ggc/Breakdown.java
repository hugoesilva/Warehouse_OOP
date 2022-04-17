package ggc;

public class Breakdown extends Transaction {

    double _delta = 0;
    String _recipe;
    
    public Breakdown(int key, int payDay, Partner partner, Product product, int amount, double price, double delta, String recipe) {
        super(key, payDay, partner, product, amount, price);
        _delta = delta;
        _recipe = recipe;
    }

    public String showTransaction() {
        return "DESAGREGAÇÃO|" + super.getKey() +"|" + super.getPartner().getKey() + "|" + super.getProductKey()+ "|" + 
        super.getAmount() + "|" + Math.round(_delta) + "|" + Math.round(super.getPrice()) + "|" + super.getPayDay() + "|" + _recipe;
    }

    public boolean isPaidByPartner() {
        return true;
    }

    public int getDeadLine() {
        return getPayDay();
    }
}
