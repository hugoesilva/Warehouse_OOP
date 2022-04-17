package ggc;

import java.io.Serializable;

public abstract class Status implements Serializable {

    private Partner _partner;
    private int _points = 0;
    
    public Status(Partner partner, int points) {
        _partner = partner;
        _points = points;
    }

    public int getPoints() {
        return _points;
    }

    public void setPoints(int points) {
        _points = points;
    }

    public Partner getPartner() {
        return _partner;
    }

    public abstract void updatePoints(int presentDate, int deadline, double paidValue);

    public void updateStatus() { }

    public abstract String showStatus();

    public abstract double calculateSale(double saleValue, int deadline, int presentDate, int dateModifier);

    public int calculatePeriod(int presentDate, int deadline, int dateModifier) {
        if (deadline - presentDate >= dateModifier) {
            return 1;
        }
        else if ((deadline - presentDate) >= 0 && (deadline - presentDate) <= dateModifier) {
            return 2;
        }
        else if ((presentDate - deadline > 0) && (presentDate - deadline) <= dateModifier) {
            return 3;
        }
        else {
            return 4;
        }
    }
}
