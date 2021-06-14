-- MySQL dump 10.13  Distrib 8.0.25, for Linux (x86_64)
--
-- Host: localhost    Database: fortuna
-- ------------------------------------------------------
-- Server version	8.0.25-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `fortuna`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `fortuna` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `fortuna`;

--
-- Table structure for table `ordini`
--

DROP TABLE IF EXISTS `ordini`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordini` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `note` varchar(255) NOT NULL,
  `data_ora` datetime NOT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `indirizzo` varchar(255) DEFAULT NULL,
  `tavolo` varchar(50) DEFAULT NULL,
  `pagamento` varchar(32) DEFAULT NULL,
  `accettato` char(1) DEFAULT NULL,
  `ordini_tipo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordini`
--

LOCK TABLES `ordini` WRITE;
/*!40000 ALTER TABLE `ordini` DISABLE KEYS */;
INSERT INTO `ordini` VALUES (25,'Mario Smough','Allergico a plastica','2021-06-08 15:59:00','32323232','Via Sleppa',NULL,'ch_1Izj1jC9JTYu94Xdu51iYKcm',NULL,NULL),(26,'Saas','','2021-06-09 12:29:00','323232',NULL,NULL,NULL,NULL,NULL),(27,'SUUUUUUS','Sus','2021-06-09 12:31:00','3232323232','VERIFICA: Saas',NULL,NULL,NULL,NULL),(28,'sas','sus','2021-06-10 15:48:00','3232323232',NULL,NULL,NULL,NULL,NULL),(29,'Mario','gianni','2021-06-10 15:58:00','36643434343',NULL,NULL,NULL,NULL,NULL),(30,'Porva','sos','2021-06-19 10:29:00','32323232','Saaas',NULL,'ch_1J15kmC9JTYu94XdTPrpUJ13',NULL,NULL),(31,'Saaas','suuus','2021-06-12 10:38:00','32323232','Saaas',NULL,'ch_1J15tdC9JTYu94XdOLMoL36U',NULL,NULL),(32,'Filippo Lenzi','Allergico a Cani','2021-06-13 17:04:00','454545454545','Via Andrea Costa 15',NULL,'ch_1J1YOUC9JTYu94Xd04h8opPE',NULL,NULL),(33,'Fillo Lenzi','No','2021-06-13 17:05:00','42424242','Via Andrea Costa 15',NULL,'ch_1J1YOUC9JTYu94Xd04h8opPE',NULL,NULL),(34,'Fillo','','2021-06-13 17:28:00','424242424242',NULL,NULL,NULL,NULL,NULL),(35,'Fillo','','2021-06-13 17:30:00','525252525252',NULL,NULL,NULL,NULL,NULL),(36,'Fillo','','2021-06-13 17:31:00','424242424242',NULL,NULL,NULL,NULL,NULL),(37,'Marco','','2021-06-13 18:21:00','3334446667','Via Marconi 25',NULL,NULL,NULL,NULL),(38,'genio','sdanger','2021-06-16 04:55:00','223144',NULL,NULL,NULL,NULL,NULL),(39,'gigione','22223','2021-06-14 08:08:00','2355312145','VERIFICA: Via Ugo Bassi 34, Bologna',NULL,NULL,NULL,NULL),(40,'sdaaaaaaaaaaaa','take','2021-06-14 09:09:00','78965245455',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `ordini` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `periodi_disattivazione`
--

DROP TABLE IF EXISTS `periodi_disattivazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `periodi_disattivazione` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `inizio` datetime NOT NULL,
  `fine` datetime NOT NULL,
  `tipo` varchar(32) NOT NULL,
  `numero_prodotto` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `numeroProdotto` (`numero_prodotto`),
  CONSTRAINT `periodi_disattivazione_ibfk_1` FOREIGN KEY (`numero_prodotto`) REFERENCES `prodotti` (`numero`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `periodi_disattivazione`
--

LOCK TABLES `periodi_disattivazione` WRITE;
/*!40000 ALTER TABLE `periodi_disattivazione` DISABLE KEYS */;
INSERT INTO `periodi_disattivazione` VALUES (2,'2021-06-14 10:34:56','2021-07-05 13:33:00','0',NULL),(3,'2021-06-14 10:35:04','2021-07-05 13:33:00','0',NULL),(4,'2021-06-14 10:34:41','2021-07-05 13:33:00','0',NULL),(5,'2022-06-14 10:34:41','2023-08-05 13:33:00','0',NULL),(6,'2021-06-14 10:39:58','2021-07-05 13:33:00','2',NULL),(7,'2021-06-14 10:40:23','2021-07-05 13:33:00','0',NULL);
/*!40000 ALTER TABLE `periodi_disattivazione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prenotazioni`
--

DROP TABLE IF EXISTS `prenotazioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prenotazioni` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `telefono` varchar(50) NOT NULL,
  `numero_persone` int unsigned NOT NULL,
  `data_ora` datetime NOT NULL,
  `accettato` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenotazioni`
--

LOCK TABLES `prenotazioni` WRITE;
/*!40000 ALTER TABLE `prenotazioni` DISABLE KEYS */;
INSERT INTO `prenotazioni` VALUES (1,'Ciao rega','366366366',12,'2021-05-20 15:25:00','Y'),(2,'asgfdgdsg','45236',4,'2021-06-16 23:12:00','Y'),(3,'adsfsd','2355',12,'2021-06-16 23:15:00','Y'),(4,'sdfasdfa','235235',12,'2021-06-09 15:15:00','Y'),(5,'asdada','235325',12,'2021-06-09 15:15:00','Y'),(6,'asfdas','325435',2,'2021-06-09 12:12:00','Y'),(7,'asdfsdf','235235',5,'2021-06-02 15:15:00','Y'),(8,'safsd','354353',9,'2021-06-23 23:25:00','Y'),(9,'Fillo','321312321',15,'2021-06-16 12:32:00','Y'),(10,'Fillo','321312321',15,'2021-06-16 12:32:00','Y'),(11,'gigio','85632455',4,'2021-06-18 17:05:00',NULL);
/*!40000 ALTER TABLE `prenotazioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotti`
--

DROP TABLE IF EXISTS `prodotti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodotti` (
  `numero` int unsigned NOT NULL,
  `nome` varchar(50) NOT NULL,
  `descrizione` varchar(1023) NOT NULL,
  `prezzo` decimal(10,2) NOT NULL,
  `immagine` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`numero`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotti`
--

LOCK TABLES `prodotti` WRITE;
/*!40000 ALTER TABLE `prodotti` DISABLE KEYS */;
INSERT INTO `prodotti` VALUES (102,'Involtini','',3.00,NULL),(104,'Ravioli','',4.00,NULL),(150,'Riso','',4.00,NULL);
/*!40000 ALTER TABLE `prodotti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotti_ordinati`
--

DROP TABLE IF EXISTS `prodotti_ordinati`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodotti_ordinati` (
  `id_ordine` int unsigned NOT NULL,
  `numero_prod` int unsigned NOT NULL,
  `quantita` int unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_ordine`,`numero_prod`),
  KEY `numero_prod` (`numero_prod`),
  CONSTRAINT `prodotti_ordinati_ibfk_1` FOREIGN KEY (`id_ordine`) REFERENCES `ordini` (`id`),
  CONSTRAINT `prodotti_ordinati_ibfk_2` FOREIGN KEY (`numero_prod`) REFERENCES `prodotti` (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotti_ordinati`
--

LOCK TABLES `prodotti_ordinati` WRITE;
/*!40000 ALTER TABLE `prodotti_ordinati` DISABLE KEYS */;
INSERT INTO `prodotti_ordinati` VALUES (25,102,4),(25,104,2),(25,150,1),(26,150,1),(27,150,1),(28,102,2),(28,104,2),(28,150,1),(29,102,1),(29,104,1),(29,150,6),(30,104,1),(31,102,1),(31,104,1),(32,102,1),(32,104,1),(33,102,1),(33,104,1),(34,102,1),(34,104,1),(35,102,1),(35,104,1),(36,102,1),(36,104,1),(37,104,4),(37,150,1),(38,102,2),(38,104,1),(38,150,2),(39,104,4),(40,104,4);
/*!40000 ALTER TABLE `prodotti_ordinati` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotti_sconti`
--

DROP TABLE IF EXISTS `prodotti_sconti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodotti_sconti` (
  `id_sconto` int unsigned NOT NULL,
  `numero_prod` int unsigned NOT NULL,
  PRIMARY KEY (`id_sconto`,`numero_prod`),
  KEY `numero_prod` (`numero_prod`),
  CONSTRAINT `prodotti_sconti_ibfk_1` FOREIGN KEY (`numero_prod`) REFERENCES `prodotti` (`numero`),
  CONSTRAINT `prodotti_sconti_ibfk_2` FOREIGN KEY (`id_sconto`) REFERENCES `sconti` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotti_sconti`
--

LOCK TABLES `prodotti_sconti` WRITE;
/*!40000 ALTER TABLE `prodotti_sconti` DISABLE KEYS */;
INSERT INTO `prodotti_sconti` VALUES (3,102);
/*!40000 ALTER TABLE `prodotti_sconti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sconti`
--

DROP TABLE IF EXISTS `sconti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sconti` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `inizio` datetime NOT NULL,
  `fine` datetime NOT NULL,
  `quantita` decimal(10,2) DEFAULT NULL,
  `quantitaPct` decimal(10,2) DEFAULT NULL,
  `costoMinimo` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sconti`
--

LOCK TABLES `sconti` WRITE;
/*!40000 ALTER TABLE `sconti` DISABLE KEYS */;
INSERT INTO `sconti` VALUES (1,'2021-06-05 00:00:00','2021-06-30 23:59:00',NULL,0.20,NULL),(2,'2021-05-05 00:00:00','2021-06-10 23:59:00',1.00,NULL,5.00),(3,'2021-06-11 00:00:00','2021-06-21 23:59:00',NULL,1.00,6.00);
/*!40000 ALTER TABLE `sconti` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sconti_applicati`
--

DROP TABLE IF EXISTS `sconti_applicati`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sconti_applicati` (
  `id_ordine` int unsigned NOT NULL,
  `id_sconto` int unsigned NOT NULL,
  PRIMARY KEY (`id_ordine`,`id_sconto`),
  KEY `id_sconto` (`id_sconto`),
  CONSTRAINT `sconti_applicati_ibfk_1` FOREIGN KEY (`id_ordine`) REFERENCES `ordini` (`id`),
  CONSTRAINT `sconti_applicati_ibfk_2` FOREIGN KEY (`id_sconto`) REFERENCES `sconti` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sconti_applicati`
--

LOCK TABLES `sconti_applicati` WRITE;
/*!40000 ALTER TABLE `sconti_applicati` DISABLE KEYS */;
INSERT INTO `sconti_applicati` VALUES (25,1),(26,1),(27,1),(28,1),(29,1),(36,1),(37,1),(39,1),(40,1),(36,3);
/*!40000 ALTER TABLE `sconti_applicati` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zona_consegna`
--

DROP TABLE IF EXISTS `zona_consegna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zona_consegna` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `prezzo_minimo` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zona_consegna`
--

LOCK TABLES `zona_consegna` WRITE;
/*!40000 ALTER TABLE `zona_consegna` DISABLE KEYS */;
INSERT INTO `zona_consegna` VALUES (1,0.00);
/*!40000 ALTER TABLE `zona_consegna` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zona_consegna_punti`
--

DROP TABLE IF EXISTS `zona_consegna_punti`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zona_consegna_punti` (
  `id` int unsigned NOT NULL,
  `latitudine` decimal(10,8) NOT NULL,
  `longitudine` decimal(11,8) NOT NULL,
  `id_lista` int unsigned DEFAULT NULL,
  KEY `id` (`id`),
  CONSTRAINT `zona_consegna_punti_ibfk_1` FOREIGN KEY (`id`) REFERENCES `zona_consegna` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zona_consegna_punti`
--

LOCK TABLES `zona_consegna_punti` WRITE;
/*!40000 ALTER TABLE `zona_consegna_punti` DISABLE KEYS */;
INSERT INTO `zona_consegna_punti` VALUES (1,44.46155500,11.26215100,0),(1,44.56273300,11.26215100,1),(1,44.56273300,11.43036300,2),(1,44.46155500,11.43036300,3);
/*!40000 ALTER TABLE `zona_consegna_punti` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-14 11:58:53
