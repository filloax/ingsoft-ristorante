package it.unibo.ingsoft.fortuna.ordinazione;

import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unibo.ingsoft.fortuna.model.richiesta.OrdineDomicilio;

@Component
public class PagamentoOnline implements IPagamentoOnline {

    @Autowired
    StripeService pagamentoStripe;

    @Override
    public boolean verificaPagamento(OrdineDomicilio ordine) throws PaymentException {
        try {
            return pagamentoStripe.isPaid(ordine.getTokenPagamento());
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                | APIException e) {
            throw new PaymentException("Errore nel controllo pagamento!", e);
        }
    }

    @Override
    public void effettuaPagamento(OrdineDomicilio ordine) throws PaymentException {
        try {
            pagamentoStripe.charge(ordine.getTokenPagamento());
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                | APIException e) {
            throw new PaymentException("Errore nel pagamento!", e);
        }
    }

    @Override
    public void annullaPagamento(OrdineDomicilio ordine) throws PaymentException {
        try {
            pagamentoStripe.cancelCharge(ordine.getTokenPagamento());
        } catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
                | APIException e) {
            throw new PaymentException("Errore nel risarcimento!", e);
        }
    }

    @Override
    public boolean verificaAutorizzazione(OrdineDomicilio ordine) throws PaymentException {
        try {
            return pagamentoStripe.isAuthorized(ordine.getTokenPagamento());
        } catch (AuthenticationException | APIConnectionException | CardException
                | APIException e) {
            throw new PaymentException("Errore nel controllo autorizzazione!", e);
        }
    }

}