package ggc.exceptions;

public class UnknownPartnerException extends Exception {

    private String _key;

    public UnknownPartnerException(String key) {
        _key = key;
    }

    public String getUnknownKey() {
        return _key;
    }
    
}

