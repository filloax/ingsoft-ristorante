package it.unibo.ingsoft.fortuna.model.zonaconsegna;

public class ZonaConsegnaException extends Exception {
    public ZonaConsegnaException(String msg) {
        super(msg);
    }

    public ZonaConsegnaException(String msg, Throwable e) {
        super(msg, e);
    }
}