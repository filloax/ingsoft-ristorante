# ingsoft-ristorante
Progetto per Ingegneria del Software T (Unibo)

## Come usare
- Per lanciare il server: lanciate FortunaWeb/lancia.bat, o il comando ivi scritto da dentro la cartella
- Va avviato insieme anche FortunaLog/log.txt o il jar (equivalente), senza non andrò il log e farà molti errori il server (anche se funzionerà comunque)

Occorre installare alcune estensioni di VSCode, per la precisione *"Spring Boot Dashboard", "Spring Boot Tools", "Spring Initalizr Java Support"*, e se non sono già installati *"Java Extension Pack"* e *"Java Test Runner", "Debugger for Java"*, se volete fare test.

Nota: viene usato come sistema di Build Maven e Spring Boot. Se non sapete di preciso come funziona no problem, neanch'io.
Il server è impostato per venire re-compilato e buildato automaticamente a ogni modifica fatta alle classi, html, o altro, quindi ogni modifica dovrebbe bastare aggiornare la pagina per vederla all'istante nel caso di cose web, o comunque si applica subito in gen.

Il log non usa spring e non fa sta cosa di aggiornarsi subito, ma tanto dovrebbe già andare bene così com'è.

Se serve aggiungere librerie ditemi che ci guardiamo, non sono sicuro neanch'io di come funzioni ma -credo- che basti aggiungere la dipendenza sul file principale pom.xml, che ha un tasto da VSCode che fa tutto lui.
