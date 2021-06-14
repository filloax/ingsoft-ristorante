package it.unibo.ingsoft.fortuna.sms;

public interface IInvioSms {
    public void inviaSMS(String telefono, String messaggio) throws SMSException;
}
