package org.jenjetsu.com.hrs.biller;

public class BillingException extends Exception{

    public BillingException() {
        super();
    }

    public BillingException(String message) {
        super(message);
    }

    public BillingException(Exception e) {
        super(e);
    }

    public BillingException(String message, Exception cause) {
        super(message, cause);
    }
}
