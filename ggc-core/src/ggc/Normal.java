package ggc;

public class Normal extends Status {
    
    public Normal(Partner partner, int points) {
        super(partner, points);
    }

    public void updatePoints(int presentDate, int deadline, double paidValue) {
        Partner partner = getPartner();
        if (presentDate <= deadline) {
            setPoints((int) (getPoints() + Math.round(paidValue) * 10));
            updateStatus();
        }
        else {
            setPoints(0);
        }
    }

    public void updateStatus() {
        int points = getPoints();
        Partner partner = getPartner(); 
        if (points > 25000) {
            partner.setStatus(new Elite(partner, points));
        }
        else if (points > 2000) {
            partner.setStatus(new Selection(partner, points));
        }
    }

    public String showStatus() {
        return "NORMAL" + "|" + getPoints();
    }

    public double calculateSale(double saleValue, int deadline, int presentDate, int dateModifier) {
        int period = super.calculatePeriod(presentDate, deadline, dateModifier);
        if (period == 1) {
            return 0.9 * saleValue;
        }
        else if (period == 2) {
            return saleValue;
        }
        else if (period == 3) {
            return saleValue * (((presentDate - deadline) * 0.05) + 1);
        }
        return saleValue * (((presentDate - deadline) * 0.10) + 1);
    }
}
