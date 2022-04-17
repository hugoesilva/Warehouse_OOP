package ggc.exceptions;

public class UnknownTransactionException extends Exception {
    private int _transactionKey;

    public UnknownTransactionException(int transactionKey) {
        _transactionKey = transactionKey;
    }

    public int getKey() {
        return _transactionKey;
    }
}