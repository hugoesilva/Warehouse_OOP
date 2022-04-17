package ggc;

public class Sale extends Transaction {

    private int _deadline;
    private double _addedTaxPrice = 0;

    public Sale(int key, int payday, Partner partner, Product product, int amount, double price, int deadline) {
        super(key, payday, partner, product, amount, price);
        _deadline = deadline;
    }

    public String showTransaction() {
        if (getPayDay() >= 0) {
            return "VENDA|" + super.getKey() +"|" + super.getPartner().getKey() + "|" + super.getProductKey()+ "|" + 
            super.getAmount() + "|" + Math.round(super.getPrice()) + "|" + Math.round(_addedTaxPrice) + "|" + _deadline + "|" + super.getPayDay();
        }
        else {
            return "VENDA|" + super.getKey() +"|" + super.getPartner().getKey() + "|" + super.getProductKey()+ "|" + 
            super.getAmount() + "|" + Math.round(super.getPrice()) + "|" + Math.round(_addedTaxPrice) + "|"  + _deadline;
        }
    }

    public void setPayDay(int payDay) {
        super.setPayDay(payDay);
    }

    public void setAddedTaxPrice(double price) {
        if (super.getPayDay() < 0) {
            _addedTaxPrice = price;
        }
    }

    public int getDeadLine() {
        return _deadline;
    }

    public boolean isPaidByPartner() {
        return true;
    }
}
