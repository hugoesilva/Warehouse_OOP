package ggc.exceptions;

public class UnavailableProductExceptionC extends Exception {

    private String _productKey;
    private int _requested;
    private int _available;

    public UnavailableProductExceptionC(String productKey, int requested, int available) {
        _productKey = productKey;
        _requested = requested;
        _available = available;
    }

    public String getKey() {
        return _productKey;
    }

    public int getRequested() {
        return _requested;
    }

    public int getAvailable() {
        return _available;
    }
}