package it.unibo.ingsoft.fortuna.ordinazione;

public class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(String message, Throwable err) {
        super(message, err);
    }
}
