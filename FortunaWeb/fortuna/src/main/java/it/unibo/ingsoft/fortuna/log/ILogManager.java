package it.unibo.ingsoft.fortuna.log;

import java.time.LocalDate;
import java.util.List;

public interface ILogManager {
    public void scriviMessaggio(String msg);
    public void scriviOperazione(String ip, String operazione);
    public List<String> listaLogFile(LocalDate from, LocalDate to);
}
