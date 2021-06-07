package it.unibo.ingsoft.fortuna.ordinazione;

import it.unibo.ingsoft.fortuna.model.richiesta.*;

public interface IPagamentoOnline {
    public boolean verificaAutorizzazione(OrdineDomicilio ordine) throws PaymentException;
    public boolean verificaPagamento(OrdineDomicilio ordine) throws PaymentException;
    public void effettuaPagamento(OrdineDomicilio ordine) throws PaymentException;
}
