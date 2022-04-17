package ggc.exceptions;

public class InvalidDateExceptionC extends Exception {
    
    private int _days;

    public InvalidDateExceptionC(int days) {
        _days = days;
    }

    public int getDays() {
        return _days;
    }
}


