package it.unibo.ingsoft.fortuna.ordinazione;

//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.stripe.exception.*;
import com.stripe.model.Charge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.unibo.ingsoft.fortuna.model.Prodotto;

@Controller
public class PagamentoServlet {
    @Value("${STRIPE_PUBLIC_KEY: null}")
    private String stripePublicKey;
    
    @Autowired
    private StripeService pagamentoStripe;

    @Autowired
    IOrdinazioneController ordinazione;

    @RequestMapping("/checkout")
    public String checkout(Model model, HttpSession session, HttpServletRequest request) {
        if (session.getAttribute("datiOrdine") == null) {
            return "redirect:/ordine-dati";
        }

        @SuppressWarnings("unchecked")
        List<Prodotto> prodotti = (List<Prodotto>) session.getAttribute("prodotti");
        DatiOrdine datiOrdine = (DatiOrdine) session.getAttribute("datiOrdine");
        double totale = ordinazione.calcolaTotale(prodotti);
        double totaleScontato = ordinazione.calcolaTotaleScontato(prodotti, LocalDateTime.of(datiOrdine.getData(), datiOrdine.getOra()));

        model.addAttribute("amountOld", (int) Math.floor(totale * 100)); // in centesimi
        model.addAttribute("amount", (int) Math.floor(totaleScontato * 100)); // in centesimi
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.EUR);

        return "ordinazione/checkout";
    }

    @PostMapping("/pagamento")
    public String charge(ChargeRequest chargeRequest, Model model, HttpSession session)
      throws StripeException {
        chargeRequest.setDescription("Pagamento ordine");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        
        Charge charge = pagamentoStripe.authorize(chargeRequest);
        session.setAttribute("tokenPagamento", charge.getId());
        
        return "redirect:/ordine-finale";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException e, RedirectAttributes redirectAttrs) {
        redirectAttrs.addFlashAttribute("error", e.getMessage());

        return "redirect:/checkout";
    }

    @ModelAttribute("ordinazioneService")
    public IOrdinazioneController getOrdinazione() {
        return this.ordinazione;
    }
}
