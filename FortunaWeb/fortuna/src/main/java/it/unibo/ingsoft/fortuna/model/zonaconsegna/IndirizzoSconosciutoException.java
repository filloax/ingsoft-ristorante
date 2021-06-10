package it.unibo.ingsoft.fortuna.model.zonaconsegna;

public class IndirizzoSconosciutoException extends ZonaConsegnaException {
    public IndirizzoSconosciutoException(String msg) {
        super(msg);
    }

    public IndirizzoSconosciutoException(String msg, Throwable e) {
        super(msg, e);
    }
}
