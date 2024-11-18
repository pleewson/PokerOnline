-- MySQL dump 10.13  Distrib 9.0.1, for macos14.4 (arm64)
--
-- Host: localhost    Database: pokerDB
-- ------------------------------------------------------
-- Server version	8.2.0

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
-- Table structure for table `cards_history`
--

DROP TABLE IF EXISTS `cards_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cards_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `community_cards` varchar(255) DEFAULT NULL,
  `player1cards` varchar(255) DEFAULT NULL,
  `player2cards` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cards_history`
--

LOCK TABLES `cards_history` WRITE;
/*!40000 ALTER TABLE `cards_history` DISABLE KEYS */;
INSERT INTO `cards_history` VALUES (1,'[5-H] [3-D] [J-S] [3-S] [2-C] ','[10-H] [10-C] ','[8-S] [5-S] '),(2,'[7-C] [8-H] [9-H] [A-H] [4-D] ','[2-H] [9-C] ','[A-S] [10-S] '),(3,'[4-H] [A-D] [Q-S] [6-H] [K-S] ','[9-S] [9-D] ','[8-C] [K-C] '),(4,'[K-D] [6-D] [6-S] [Q-D] [A-C] ','[5-C] [7-H] ','[10-D] [4-C] '),(5,'[J-H] [4-S] [5-D] [7-S] [J-D] ','[2-S] [7-D] ','[K-H] [8-D] '),(6,'[Q-S] [2-S] [A-S] [3-D] [4-H] ','[8-D] [A-H] ','[Q-H] [9-D] '),(7,'[5-H] [2-H] [10-D] [6-C] [4-S] ','[9-H] [9-C] ','[K-H] [7-H] '),(8,'[7-D] [2-C] [Q-D] [7-C] [4-D] ','[10-C] [Q-C] ','[J-H] [K-D] '),(9,'[K-D] [2-D] [6-C] [8-D] [7-H] ','[8-C] [3-S] ','[7-S] [4-D] '),(10,'[10-D] [K-C] [6-H] [10-S] [3-H] ','[7-C] [8-S] ','[2-H] [10-H] '),(11,'[5-D] [Q-D] [J-H] [4-C] [6-S] ','[6-D] [J-S] ','[4-H] [J-C] '),(12,'[A-H] [3-C] [4-S] [8-H] [9-H] ','[5-C] [A-S] ','[3-D] [5-S] '),(13,'[K-S] [Q-S] [Q-H] [2-S] [A-C] ','[Q-C] [10-C] ','[7-D] [J-D] '),(14,'[A-C] [5-C] [5-D] [5-H] [Q-C] ','[3-H] [3-C] ','[9-C] [K-C] '),(15,'[A-S] [8-C] [7-H] [A-D] [4-S] ','[J-D] [3-D] ','[8-H] [10-C] '),(16,'[3-C] [7-S] [6-H] [3-D] [7-H] ','[5-S] [10-D] ','[9-C] [10-H] '),(17,'[4-S] [2-C] [J-H] [K-D] [6-C] ','[3-S] [2-D] ','[5-C] [4-C] '),(18,'[8-H] [8-C] [8-D] [5-C] [3-H] ','[7-S] [J-D] ','[2-C] [2-H] '),(19,'[8-C] [9-S] [K-C] [9-D] [6-C] ','[7-C] [10-S] ','[K-H] [K-D] '),(20,'[2-H] [J-D] [9-D] [6-S] [Q-S] ','[K-H] [10-S] ','[9-C] [A-S] '),(21,'[A-S] [3-D] [K-C] [J-D] [4-H] ','[8-D] [9-C] ','[J-S] [6-H] '),(22,'[9-H] [Q-H] [2-C] [9-S] [8-C] ','[4-S] [10-D] ','[5-S] [K-C] '),(23,'[7-C] [Q-D] [8-H] [J-D] [10-S] ','[A-D] [J-S] ','[9-S] [A-C] '),(24,'[3-D] [4-S] [3-H] [4-H] [10-D] ','[8-D] [6-D] ','[2-H] [3-C] '),(25,'[5-S] [6-C] [J-S] [7-D] [A-C] ','[9-C] [K-S] ','[10-H] [7-S] '),(26,'[5-S] [8-H] [6-D] [7-S] [9-S] ','[2-H] [10-D] ','[Q-D] [10-H] '),(27,'[K-H] [8-H] [9-C] [10-C] [3-S] ','[A-C] [J-H] ','[8-D] [J-D] '),(28,'[A-D] [J-D] [8-H] [7-S] [4-D] ','[K-H] [A-C] ','[2-D] [8-C] '),(29,'[3-H] [Q-C] [Q-S] [5-S] [J-S] ','[K-C] [7-C] ','[3-S] [8-D] '),(30,'[A-H] [K-H] [Q-H] [6-H] [J-D] ','[K-D] [4-C] ','[2-D] [10-S] '),(31,'[8-C] [6-C] [10-C] [5-H] [2-H] ','[6-S] [3-D] ','[7-S] [6-D] '),(32,'[9-C] [K-S] [A-S] [9-D] [4-S] ','[10-D] [7-D] ','[3-C] [9-H] '),(33,'[6-C] [3-C] [3-H] [8-C] [5-D] ','[9-D] [4-C] ','[K-C] [9-C] '),(34,'[7-D] [K-H] [2-H] [9-C] [10-S] ','[5-S] [2-S] ','[Q-D] [4-D] '),(35,'[5-C] [9-C] [6-C] [9-D] [8-S] ','[Q-C] [4-H] ','[2-D] [5-H] '),(36,'[Q-S] [7-S] [2-D] [A-D] [10-H] ','[8-D] [4-D] ','[9-C] [K-H] '),(37,'[3-H] [2-S] [8-H] [Q-H] [5-H] ','[3-S] [J-D] ','[A-H] [4-S] '),(38,'[2-S] [4-H] [10-D] [7-D] [A-S] ','[A-D] [9-H] ','[K-C] [3-S] '),(39,'[3-H] [8-C] [8-H] [7-C] [7-D] ','[K-C] [Q-H] ','[10-C] [8-D] '),(40,'[4-D] [8-H] [Q-S] [2-H] [K-S] ','[9-H] [J-D] ','[6-C] [8-C] '),(41,'[6-D] [10-C] [2-S] [2-D] [K-C] ','[3-S] [7-D] ','[4-C] [3-H] '),(42,'[6-S] [Q-D] [A-C] [2-H] [Q-H] ','[9-S] [2-C] ','[4-H] [Q-C] ');
/*!40000 ALTER TABLE `cards_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player`
--

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (1,'admin@wp.pl','admin','$2a$10$u5A/hD.L9sw.6Zr.PrmvhuWLPlTyLkVnSfNL8hpDxfh19k4scxiXK'),(2,'jola552@gmail.com','jola552','$2a$10$4QnC9EM.abLajW05yEfwS.QAkbZfAuhHV6FdGWfO9dcpHxwqsmD76'),(3,'spiderman@gmail.com','spiderman','$2a$10$cYe.Zc/3sgVHwERxXh8yQOuc/d350gE2609VeIiPlsswKXPfwgG9q'),(4,'mario@wp.pl','Mario','$2a$10$UlRMICldkkYA1UIBbkrMSeqLYSjqz/AtRBAM.h16WY4oBlFWHPwW.'),(5,'harrypotter@hogwart.com','HarryPotter','$2a$10$MONDF8lh.bbnO0NyBrBPv.hAapdG/cC8NSSIppFs8/SwKR3xj.zFe'),(6,'elpablo@gmail.com','elPablo','$2a$10$bDvPuic5o6jIpNh4kWal5ue4KZRHbHAZWxverePaVMxjPoj8EODpq'),(7,'test@wp.pl','test','$2a$10$jwAo15ZgGhYvbZD6aKdUTeEjCd48HolAYvja/EvFMhiaOr5s06jeG'),(8,'bananaBoyy@gmail.com','bananaBoy','$2a$10$G2InIt/RA0rC8a.O//suz.K8cCy/pRvsO8lmT8FK/nb7r5d9DpB2i'),(9,'reks@gmail.com','reks','$2a$10$9EiaqjnL9xS8OXoIApsM7uv4X7H1LLMX08qSeujoimaf0yZ5eMXYO');
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_details`
--

DROP TABLE IF EXISTS `player_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `country` varchar(255) DEFAULT NULL,
  `created` datetime(6) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_adult` bit(1) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `updated` datetime(6) DEFAULT NULL,
  `player_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKhm71mk5ja9pbj26ffte3gcx8h` (`player_id`),
  CONSTRAINT `FK3fuwl81pyajj7mtlw118aqvg4` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_details`
--

LOCK TABLES `player_details` WRITE;
/*!40000 ALTER TABLE `player_details` DISABLE KEYS */;
INSERT INTO `player_details` VALUES (1,'admin','2024-06-29 08:21:00.102952','admin',_binary '','admin',NULL,1),(2,'Polska','2024-06-29 08:29:45.711804','Jola',_binary '','Lojalna',NULL,2),(3,'United States','2024-06-29 08:30:05.901137','Peter',_binary '','Parker',NULL,3),(4,'Nintendo','2024-06-29 08:30:42.711505','Its me',_binary '','Mario',NULL,4),(5,'Hogwart','2024-06-29 08:31:24.787428','Harry',_binary '','Potter',NULL,5),(6,'Mexico','2024-06-29 08:32:06.203960','Pablo',_binary '','Locasto',NULL,6),(7,'test','2024-06-29 08:32:26.384911','test',_binary '','test',NULL,7),(8,'noOneKnows','2024-06-29 08:37:29.480751','Bananaa',_binary '','Girl',NULL,8),(9,'dino','2024-11-18 14:39:04.389456','reks',_binary '\0','reks',NULL,9);
/*!40000 ALTER TABLE `player_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trophies`
--

DROP TABLE IF EXISTS `trophies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trophies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` int DEFAULT NULL,
  `player_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKrfu78o1lew1eq8o02ddysew64` (`player_id`),
  CONSTRAINT `FKmc4upl9v8rmcy44kanqa8ib16` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trophies`
--

LOCK TABLES `trophies` WRITE;
/*!40000 ALTER TABLE `trophies` DISABLE KEYS */;
INSERT INTO `trophies` VALUES (1,2,1),(2,12,2),(3,4,3),(4,2,4),(5,0,5),(6,7,6),(7,1,7),(8,1,8),(9,0,9);
/*!40000 ALTER TABLE `trophies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_egzamin_entity`
--

DROP TABLE IF EXISTS `user_egzamin_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_egzamin_entity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_egzamin_entity`
--

LOCK TABLES `user_egzamin_entity` WRITE;
/*!40000 ALTER TABLE `user_egzamin_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_egzamin_entity` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-18 15:21:28
