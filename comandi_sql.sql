SELECT * FROM prenotazioni;
SELECT * FROM ordini;

CREATE TABLE prenotazioni
(
 id INT unsigned NOT NULL AUTO_INCREMENT,
 nome VARCHAR(50) NOT NULL,
 telefono VARCHAR(50) NOT NULL,
 numero_persone INT unsigned NOT NULL,
 data_ora DATETIME NOT NULL,
 accettato CHAR(1),
 PRIMARY KEY (id)
);

CREATE TABLE ordini
(
 id INT unsigned NOT NULL AUTO_INCREMENT,
 nome VARCHAR(50) NOT NULL,
 note VARCHAR(255) NOT NULL,
 data_ora DATETIME NOT NULL,
 telefono VARCHAR(50),
 indirizzo VARCHAR(255),
 tavolo VARCHAR(50),
 accettato CHAR(1),
 PRIMARY KEY (id)
);

CREATE TABLE prodotti
(
	numero INT unsigned NOT NULL,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descrizione VARCHAR(1023) NOT NULL,
    prezzo DECIMAL NOT NULL,
    immagine VARCHAR(255),
    PRIMARY KEY (numero)
);

CREATE TABLE sconti
(
 id INT unsigned NOT NULL AUTO_INCREMENT,
 inizio DATETIME NOT NULL,
 fine DATETIME NOT NULL,
 numeroProdotto INT unsigned,
 quantita DECIMAL,
 quantitaPct DECIMAL,
 costoMinimo DECIMAL,
 PRIMARY KEY (id),
 FOREIGN KEY (numeroProdotto) REFERENCES prodotti(numero)
);

SHOW TRIGGERS;
DROP TRIGGER IF EXISTS tgr_sconti_insert;
DELIMITER //
CREATE TRIGGER tgr_sconti_insert
    BEFORE INSERT
    ON sconti
    FOR EACH ROW
BEGIN
    IF (NEW.quantitaPct IS NULL AND NEW.quantita IS NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Quantita non specificata';
    END IF;
END;//
DELIMITER ;

CREATE TABLE prodotti_ordinati
(
	id_ordine INT unsigned NOT NULL,
    numero_prod INT unsigned NOT NULL,
    quantita INT unsigned NOT NULL DEFAULT 1,
    PRIMARY KEY (id_ordine, numero_prod),
    FOREIGN KEY (id_ordine) REFERENCES ordini(id),
    FOREIGN KEY (numero_prod) REFERENCES prodotti(numero)
);

CREATE TABLE sconti_applicati
(
	id_ordine INT unsigned NOT NULL,
    id_sconto INT unsigned NOT NULL,
    PRIMARY KEY (id_ordine, id_sconto),
    FOREIGN KEY (id_ordine) REFERENCES ordini(id),
    FOREIGN KEY (id_sconto) REFERENCES sconti(id)
);
##
SHOW GRANTS FOR 'springuser'@localhost;
GRANT UPDATE ON fortuna.* TO 'springuser'@localhost;
GRANT INSERT ON fortuna.* TO 'springuser'@localhost;
GRANT DELETE ON fortuna.* TO 'springuser'@localhost;
##
DROP TABLE prenotazioni;
##