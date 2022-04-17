package ggc;
import java.text.Collator;

public class Batch extends Product implements Comparable<Batch> {

    private String _partnerKey;
    private int _units;
    private double _pricePerUnit;

    public Batch(String key, String partnerKey, int units, double pricePerUnit) {
        super(key);
        _partnerKey = partnerKey;
        _units = units;
        _pricePerUnit = pricePerUnit;
    }

    public String getBatchPartnerKey() {
        return _partnerKey;
    }

    public String showBatch() {
        return getProductKey() + "|" + _partnerKey + "|" + (int) Math.round(_pricePerUnit) + "|" + _units;
    }

    public void updateUnits(int amount) {
        _units -= amount;
    }

    public int getBatchUnits() {
        return _units;
    }

    public double getPricePerUnit() {
        return _pricePerUnit;
    }

    @Override
    public int compareTo(Batch other) {
        if (!getProductKey().equals(other.getProductKey())) {
            return Collator.getInstance().compare(getProductKey(), other.getProductKey());
        }
        else if (!_partnerKey.equals(other._partnerKey)) {
            return Collator.getInstance().compare(_partnerKey, other._partnerKey);
        }
        else if (_pricePerUnit != other._pricePerUnit) {
            double x = _pricePerUnit - other._pricePerUnit;
            if (x > 0) {
                return 1;
            }
            else {
                return -1;
            }
        }
        else {
            return _units - other._units;
        }
    }
}
