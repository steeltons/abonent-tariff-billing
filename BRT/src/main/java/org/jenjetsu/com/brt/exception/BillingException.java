package org.jenjetsu.com.brt.exception;

public class BillingException extends Exception{

    public BillingException() {
        super();
    }

    public BillingException(String message) {
        super(message);
    }

    public BillingException(Throwable cause) {
        super(cause);
    }

    public BillingException(String message, Throwable cause) {
        super(message, cause);
    }
}
