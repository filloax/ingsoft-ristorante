package it.unibo.ingsoft.fortuna.autenticazione;

public class AutenticazioneException extends Exception {
    public AutenticazioneException(String msg) {
        super(msg);
    }

    public AutenticazioneException(String msg, Throwable e) {
        super(msg, e);
    }

    public AutenticazioneException(Throwable e) {
        super(e);
    }

}
