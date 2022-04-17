package ggc.exceptions;

public class DuplicatePartnerException extends Exception {

    private String _key;

    public DuplicatePartnerException(String key) {
        _key = key;
    }
    
    public String getKey() {
        return _key;
    }
}
