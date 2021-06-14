package it.unibo.ingsoft.fortuna.sms;

import org.springframework.stereotype.Component;

@Component
public class InvioSMSMock implements IInvioSms {

    @Override
    public void inviaSMS(String telefono, String messaggio) throws SMSException {
    }
    
}
