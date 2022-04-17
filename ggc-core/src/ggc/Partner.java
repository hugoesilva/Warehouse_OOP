package ggc;

import java.io.Serializable;
import java.util.*;

import ggc.Transaction;

public class Partner implements Serializable {

    private String _key;
    private String _name;
    private String _address;
    private int _acquisitionsByWarehouseValue = 0;
    private int _salesByWarehouseValue = 0;
    private int _paidSalesByWarehouse = 0;
    private Status _status = new Normal(this, 0);
    private TreeMap<Integer, Acquisition> _acquisitions = new TreeMap<Integer, Acquisition>();
    private TreeMap<Integer, Transaction> _salesAndBreakdowns = new TreeMap<Integer, Transaction>();
    private ArrayList<Notification> _notifications = new ArrayList<Notification>();
    private TreeSet<String> _activatedProductNotifications = new TreeSet<String>();

    public Partner(String key, String name, String address) {
        _key = key;
        _name = name;
        _address = address;
    }

    public String getKey() {
        return _key;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public int getPoints() {
        return _status.getPoints();
    }

    public void addAcquisition(Acquisition a) {
        _acquisitions.put(a.getKey(), a);
    }

    public void addSale(Sale s) {
        _salesAndBreakdowns.put(s.getKey(), s);
    }

    public void addBreakdown(Breakdown b) {
        _salesAndBreakdowns.put(b.getKey(), b);
    }

    public String showAcquisitionsWithPartner() {
        String out = "";
        for (Map.Entry<Integer, Acquisition> entry : _acquisitions.entrySet()) {
            out += entry.getValue().showTransaction() + "\n";
        }
        int index = out.length() - 1;
        if (index >= 0) {
          return out.substring(0, index);
        }
        else {
          return out;
        }
    }

    public String showSalesAndBreakdownsWithPartner(int date) {
        String out = "";
        for (Map.Entry<Integer, Transaction> entry : _salesAndBreakdowns.entrySet()) {
            Transaction t = entry.getValue();
            if (t.getPayDay() < 0) {
                Product p = t.getProduct();
                Sale s = (Sale) t;
                s.setAddedTaxPrice(calculateSale(s.getPrice(), s.getDeadLine(), date, p.getDateModifier()));
            }
            out += entry.getValue().showTransaction() + "\n";
        }
        int index = out.length() - 1;
        if (index >= 0) {
            return out.substring(0, index);
        }
        else {
            return out;
        }
    }

    public String showPartner() {
        return _key + "|" + _name + "|" + _address + "|" +  _status.showStatus() +
        "|" + _acquisitionsByWarehouseValue + "|" + _salesByWarehouseValue + "|" + _paidSalesByWarehouse;
    }

    public void setStatus(Status status) {
        _status = status;
    }

    public double calculateSale(double saleValue, int deadline, int presentDate, int dateModifier) {
        return _status.calculateSale(saleValue, deadline, presentDate, dateModifier);
    }

    public void updatePoints(int presentDate, int deadline, double paidValue) {
        _status.updatePoints(presentDate, deadline, paidValue);
    }

    public void setAcquisitionsByWarehouseValue(double value) {
        _acquisitionsByWarehouseValue += value;
    }

    public void setSalesByWarehouseValue(double value) {
        _salesByWarehouseValue += value;
    }
    
    public void setPaidSalesByWarehouseValue(double value) {
        _paidSalesByWarehouse += value;
    }

    public void addNotification(Notification n) {
        if (!_activatedProductNotifications.contains(n.getProduct().getProductKey())) {
            return;
        }
        else {
            _notifications.add(n);
        }
    }

    public void removeAllNotifications() {
        _notifications.clear();
    }

    public String showPartnerNotifications(DeliveryMethod delivery) {
        String out = delivery.deliver(_notifications);
        removeAllNotifications();
        return out;
    }

    public void toggleProductNotifications(String productKey) {
        boolean contains = _activatedProductNotifications.contains(productKey);
        if (contains) {
            _activatedProductNotifications.remove(productKey);
        }
        else {
            _activatedProductNotifications.add(productKey);
        }
    }

    public String showProductsBoughtByPartner() {
        String out = "";
        for (Map.Entry<Integer, Transaction> entry : _salesAndBreakdowns.entrySet()) {
            out += entry.getValue().getProductKey() + "\n";
        }
        int index = out.length() - 1;
        if (index >= 0) {
          return out.substring(0, index);
        }
        else {
          return out;
        }
    }
}