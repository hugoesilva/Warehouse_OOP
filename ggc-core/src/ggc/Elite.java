package ggc;

public class Elite extends Status {

    public Elite(Partner partner, int points) {
        super(partner, points);
    }

    public void updatePoints(int presentDate, int deadline, double paidValue) {
        Partner partner = getPartner();
        if (presentDate <= deadline) {
            setPoints((int) (getPoints() + Math.round(paidValue) * 10));
            updateStatus();
        }
        else if (presentDate - deadline > 15) {
            setPoints((int) Math.round(getPoints() * 0.25));
            partner.setStatus(new Selection(partner, getPoints()));
        }
    }

    public void updateStatus() {
        int points = getPoints();
        Partner partner = getPartner(); 
        if (points <= 2000) {
            partner.setStatus(new Normal(partner, points));
        }
        else if (points <= 25000) {
            partner.setStatus(new Selection(partner, points));
        }
    }

    public String showStatus() {
        return "ELITE" + "|" + getPoints();
    }

    public double calculateSale(double saleValue, int deadline, int presentDate, int dateModifier) {
        int period = super.calculatePeriod(presentDate, deadline, dateModifier);

        if (period == 1 || period == 2) {
            return 0.9 * saleValue;
        }
        else if (period == 3) {
            return 0.95 * saleValue;
        }
        return saleValue;
    }
}
