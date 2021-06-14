package it.unibo.ingsoft.fortuna.sms;

public class SMSException extends Exception {
    public SMSException(String msg) {
        super(msg);
    }

    public SMSException(String msg, Throwable e) {
        super(msg, e);
    }
}
