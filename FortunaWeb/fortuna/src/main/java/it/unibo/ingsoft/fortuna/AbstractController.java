package it.unibo.ingsoft.fortuna;

import org.springframework.beans.factory.annotation.Autowired;

import it.unibo.ingsoft.fortuna.log.ILogManager;

public abstract class AbstractController {
    
	@Autowired
	private ILogManager log;

	protected void scriviMessaggio(String msg) {
		log.scriviMessaggio(msg);
	}

	protected void scriviOperazione(String ip, String operazione) {
		log.scriviOperazione(ip, operazione);
	}
}
