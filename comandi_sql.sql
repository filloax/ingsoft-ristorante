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


##
SHOW GRANTS FOR 'springuser'@localhost;
GRANT UPDATE ON fortuna.* TO 'springuser'@localhost;
GRANT INSERT ON fortuna.* TO 'springuser'@localhost;
GRANT DELETE ON fortuna.* TO 'springuser'@localhost;
##
DROP TABLE prenotazioni;
##