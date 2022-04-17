package ggc.exceptions;

public class ProductNotRegisteredException extends Exception {

    public String _productKey;

    public ProductNotRegisteredException(String productKey) {
        _productKey = productKey;
    }

    public String getKey() {
        return _productKey;
    }
}
