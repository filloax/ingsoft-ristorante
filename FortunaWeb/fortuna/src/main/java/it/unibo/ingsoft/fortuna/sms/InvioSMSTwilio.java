package it.unibo.ingsoft.fortuna.sms;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.twilio.Twilio;
import com.twilio.base.Resource;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.ConfigProps;
import it.unibo.ingsoft.fortuna.ResourceUtilsLib;
import it.unibo.ingsoft.fortuna.log.ILogManager;

@Component
@Primary
public class InvioSMSTwilio implements IInvioSms {
    public static boolean DEBUG_NUMBERS = true;

    @Autowired
    private ConfigProps cfg;
    @Autowired
    private ILogManager log;

    private Set<PhoneNumber> debugPhonenumbers;

    @PostConstruct
    public void init() throws IOException {
        Twilio.init(cfg.getKeys().getTwilioSid(), cfg.getKeys().getTwilioAuth());

        if (DEBUG_NUMBERS) {
            debugPhonenumbers = Arrays.stream(ResourceUtilsLib.loadResourceToString("sms/debugNumbers.txt").split(","))
                .map(s -> new PhoneNumber(s))
                .collect(Collectors.toSet());
        }
    }

    @Override
    public void inviaSMS(String telefono, String messaggio) throws SMSException {
        Message message = null;
        if (!DEBUG_NUMBERS || debugPhonenumbers.contains(new PhoneNumber(telefono))) {
            try {
                message = Message.creator(
                    new PhoneNumber(telefono),
                    new PhoneNumber(cfg.getSms().getNumber()),
                    messaggio)
                .create();
            } catch (Exception e) {
                throw new SMSException(e);
            }
        } else {
            log.scriviMessaggio("SMS: DEBUG ATTIVO, numero " + telefono + " non incluso nei numeri di debug quindi SMS non inviato");
        }

        System.out.println();
        
        if (cfg.getSms().isLogNumbers())
            log.scriviMessaggio("INVIATO SMS id:" + (message != null ? message.getSid() : "null") + " @" + telefono + ": " + messaggio);
        else
            log.scriviMessaggio("INVIATO SMS id:" + (message != null ? message.getSid() : "null") + " msg: " + messaggio);
    }
    
}
