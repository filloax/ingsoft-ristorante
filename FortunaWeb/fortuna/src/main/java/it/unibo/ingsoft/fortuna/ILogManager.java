package it.unibo.ingsoft.fortuna;

import java.time.LocalDate;
import java.util.List;

public interface ILogManager {
    public void scriviMessaggio(String msg);
    public void scriviOperazione(String operazione, String ip);
    public List<String> listaLogFile(LocalDate from, LocalDate to);
}
