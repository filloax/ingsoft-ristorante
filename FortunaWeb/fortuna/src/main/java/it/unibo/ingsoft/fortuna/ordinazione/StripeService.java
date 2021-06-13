package it.unibo.ingsoft.fortuna.ordinazione;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unibo.ingsoft.fortuna.ConfigProps;

@Service
public class StripeService {
    @Autowired
    ConfigProps cfg;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = cfg.getKeys().getStripe();
    }

    public Charge authorize(ChargeRequest chargeRequest) 
      throws AuthenticationException, InvalidRequestException,
        APIConnectionException, CardException, APIException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());

        // Ottieni autorizzazione ma non pagare ancora
        chargeParams.put("capture", false); 

        return Charge.create(chargeParams);
    }

    public void charge(String token) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        Charge charge = Charge.retrieve(token);
        charge.capture();
    }

    public void cancelCharge(String token) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        Charge charge = Charge.retrieve(token);
        charge.refund();
    }

    public boolean isPaid(String token) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
        Charge charge = Charge.retrieve(token);
        return charge.getCaptured();
    }

    public boolean isAuthorized(String token) throws AuthenticationException, APIConnectionException, CardException, APIException {
        try {
            Charge charge = Charge.retrieve(token);
            return charge.getPaid() && !charge.getRefunded();
        } catch (InvalidRequestException e) { //token non valido
            return false;
        }
    }
}