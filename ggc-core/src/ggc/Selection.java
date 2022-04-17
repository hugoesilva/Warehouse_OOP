package ggc;

public class Selection extends Status {

    public Selection(Partner partner, int points) {
        super(partner, points);
    }

    public void updatePoints(int presentDate, int deadline, double paidValue) {
        Partner partner = getPartner();
        if (presentDate <= deadline) {
            setPoints((int) (getPoints() + Math.round(paidValue) * 10));
            updateStatus();
        }
        else if (presentDate - deadline > 2) {
            setPoints((int) Math.round(getPoints() * 0.1));
            partner.setStatus(new Normal(partner, getPoints()));
        }
    }

    public void updateStatus() {
        int points = getPoints();
        Partner partner = getPartner(); 
        if (points <= 2000) {
            partner.setStatus(new Normal(partner, points));
        }
        else if (points > 25000) {
            partner.setStatus(new Elite(partner, points));
        }
    }

    public String showStatus() {
        return "SELECTION" + "|" + getPoints();
    }

    public double calculateSale(double saleValue, int deadline, int presentDate, int dateModifier) {
        int period = super.calculatePeriod(presentDate, deadline, dateModifier);

        if (period == 1) {
            return 0.9 * saleValue;
        }

        else if (period == 2) {
            if (deadline - presentDate >= 2) {
                return 0.95 * saleValue;
            }
            else {
                return saleValue;
            }
        }
        else if (period == 3) {
            return saleValue * (((presentDate - deadline) * 0.02) + 1);
        }
        return saleValue * (((presentDate - deadline) * 0.05) + 1);
    }
}
