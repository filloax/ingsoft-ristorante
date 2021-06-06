-- MySQL dump 10.13  Distrib 8.0.25, for Win64 (x86_64)
--
-- Host: localhost    Database: fortuna
-- ------------------------------------------------------
-- Server version	8.0.25

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
  `accettato` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordini`
--

LOCK TABLES `ordini` WRITE;
/*!40000 ALTER TABLE `ordini` DISABLE KEYS */;
INSERT INTO `ordini` VALUES (1,'Filip','aaaaaaaaaaaaa','2021-06-02 02:43:00','sas',NULL,NULL,NULL),(2,'a','b','2021-06-02 12:24:00','sas',NULL,NULL,NULL),(3,'b','c','2021-06-01 22:22:00','a','sass',NULL,NULL),(4,'dasfsda','sadfdsaf','2021-06-01 23:23:00','34234',NULL,NULL,NULL),(5,'243243','23423','2021-06-01 03:04:00','2342','',NULL,NULL),(6,'asdfasdf','asdafsdaf','2021-06-01 23:05:00','safsdf','23525',NULL,NULL),(7,'Fillo','saas','2021-06-01 12:34:00','366366366',NULL,NULL,NULL);
/*!40000 ALTER TABLE `ordini` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenotazioni`
--

LOCK TABLES `prenotazioni` WRITE;
/*!40000 ALTER TABLE `prenotazioni` DISABLE KEYS */;
INSERT INTO `prenotazioni` VALUES (1,'Ciao rega','366366366',12,'2021-05-20 15:25:00',NULL),(2,'asgfdgdsg','45236',4,'2021-06-16 23:12:00',NULL),(3,'adsfsd','2355',12,'2021-06-16 23:15:00',NULL),(4,'sdfasdfa','235235',12,'2021-06-09 15:15:00',NULL),(5,'asdada','235325',12,'2021-06-09 15:15:00',NULL),(6,'asfdas','325435',2,'2021-06-09 12:12:00',NULL),(7,'asdfsdf','235235',5,'2021-06-02 15:15:00',NULL),(8,'safsd','354353',9,'2021-06-23 23:25:00',NULL);
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
  `prezzo` decimal(10,0) NOT NULL,
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
INSERT INTO `prodotti` VALUES (102,'Involtini','',3,NULL),(104,'Ravioli','',4,NULL),(150,'Riso','',4,NULL);
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
/*!40000 ALTER TABLE `prodotti_ordinati` ENABLE KEYS */;
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
  `numeroProdotto` int unsigned DEFAULT NULL,
  `quantita` decimal(10,0) DEFAULT NULL,
  `quantitaPct` decimal(10,0) DEFAULT NULL,
  `costoMinimo` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `numeroProdotto` (`numeroProdotto`),
  CONSTRAINT `sconti_ibfk_1` FOREIGN KEY (`numeroProdotto`) REFERENCES `prodotti` (`numero`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sconti`
--

LOCK TABLES `sconti` WRITE;
/*!40000 ALTER TABLE `sconti` DISABLE KEYS */;
INSERT INTO `sconti` VALUES (1,'2021-06-05 00:00:00','2021-06-30 23:59:00',NULL,NULL,0,NULL),(2,'2021-05-05 00:00:00','2021-06-10 23:59:00',NULL,1,NULL,5),(3,'2021-06-11 00:00:00','2021-06-21 23:59:00',102,NULL,1,6);
/*!40000 ALTER TABLE `sconti` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `tgr_sconti_insert` BEFORE INSERT ON `sconti` FOR EACH ROW BEGIN
    IF (NEW.quantitaPct IS NULL AND NEW.quantita IS NULL) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Quantita non specificata';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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
/*!40000 ALTER TABLE `sconti_applicati` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-06 20:48:49
