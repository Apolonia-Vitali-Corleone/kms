-- MySQL dump 10.13  Distrib 8.0.43, for Linux (x86_64)
--
-- Host: localhost    Database: kmsdb
-- ------------------------------------------------------
-- Server version	8.0.43-0ubuntu0.24.04.1

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
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '类目名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父类目ID（仅存值，应用层维护层级关系）',
  `recommend` int NOT NULL DEFAULT '0' COMMENT '0=不推荐,1=推荐',
  `status` int NOT NULL DEFAULT '1' COMMENT '0=停用，1=正常',
  `remark` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1957666607081930753,'手机数码',0,0,0,'顶级类目','system','2025-08-19 04:51:05','2025-08-19 08:05:04'),(1957666607316811778,'家用电器',0,1,1,'顶级类目','system','2025-08-19 04:51:05','2025-08-19 08:05:04'),(1957666607321006081,'图书音像',0,0,0,'暂停中','system','2025-08-19 04:51:05','2025-08-19 08:04:54'),(1957667626675290113,'手机配件',1957666607081930753,1,1,'子类目','system','2025-08-19 04:55:08','2025-08-19 08:05:04'),(1957667626687873025,'智能穿戴',1957666607081930753,0,1,'子类目','system','2025-08-19 04:55:08','2025-08-19 04:55:08');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `knowledge`
--

DROP TABLE IF EXISTS `knowledge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `knowledge` (
  `id` bigint NOT NULL,
  `category_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tag_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `visibility_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `keywords` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '1',
  `summary` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `question_no` int NOT NULL DEFAULT '1',
  `content` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `attachments` mediumtext COLLATE utf8mb4_unicode_ci,
  `created_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `knowledge`
--

LOCK TABLES `knowledge` WRITE;
/*!40000 ALTER TABLE `knowledge` DISABLE KEYS */;
INSERT INTO `knowledge` VALUES (1001,'CategoryA','Knowledge 1','Tag1','Public','k1,k2',1,'Summary 1',1,'Content 1','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1002,'CategoryA','Knowledge 2','Tag2','Public','k1,k2',1,'Summary 2',2,'Content 2','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1003,'CategoryA','Knowledge 3','Tag3','Public','k1,k2',1,'Summary 3',3,'Content 3','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1004,'CategoryA','Knowledge 4','Tag4','Public','k1,k2',1,'Summary 4',4,'Content 4','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1005,'CategoryA','Knowledge 5','Tag5','Public','k1,k2',1,'Summary 5',5,'Content 5','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1006,'CategoryB','Knowledge 6','Tag6','Private','k1,k2',1,'Summary 6',6,'Content 6','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1007,'CategoryB','Knowledge 7','Tag7','Private','k1,k2',1,'Summary 7',7,'Content 7','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1008,'CategoryB','Knowledge 8','Tag8','Private','k1,k2',1,'Summary 8',8,'Content 8','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1009,'CategoryB','Knowledge 9','Tag9','Private','k1,k2',1,'Summary 9',9,'Content 9','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1010,'CategoryB','Knowledge 10','Tag10','Private','k1,k2',1,'Summary 10',10,'Content 10','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1011,'CategoryC','Knowledge 11','Tag11','Team','k1,k2',1,'Summary 11',11,'Content 11','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1012,'CategoryC','Knowledge 12','Tag12','Team','k1,k2',1,'Summary 12',12,'Content 12','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1013,'CategoryC','Knowledge 13','Tag13','Team','k1,k2',1,'Summary 13',13,'Content 13','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1014,'CategoryC','Knowledge 14','Tag14','Team','k1,k2',1,'Summary 14',14,'Content 14','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1015,'CategoryC','Knowledge 15','Tag15','Team','k1,k2',1,'Summary 15',15,'Content 15','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1016,'CategoryD','Knowledge 16','Tag16','Dept','k1,k2',1,'Summary 16',16,'Content 16','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1017,'CategoryD','Knowledge 17','Tag17','Dept','k1,k2',1,'Summary 17',17,'Content 17','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1018,'CategoryD','Knowledge 18','Tag18','Dept','k1,k2',1,'Summary 18',18,'Content 18','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1019,'CategoryD','Knowledge 19','Tag19','Dept','k1,k2',1,'Summary 19',19,'Content 19','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55'),(1020,'CategoryD','Knowledge 20','Tag20','Dept','k1,k2',1,'Summary 20',20,'Content 20','[]','admin','2025-08-19 10:06:55','2025-08-19 10:06:55');
/*!40000 ALTER TABLE `knowledge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` bigint NOT NULL,
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'Tag1',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(2,'Tag2',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(3,'Tag3',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(4,'Tag4',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(5,'Tag5',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(6,'Tag6',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(7,'Tag7',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(8,'Tag8',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(9,'Tag9',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(10,'Tag10',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(11,'Tag11',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(12,'Tag12',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(13,'Tag13',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(14,'Tag14',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(15,'Tag15',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(16,'Tag16',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(17,'Tag17',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(18,'Tag18',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(19,'Tag19',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(20,'Tag20',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visibility`
--

DROP TABLE IF EXISTS `visibility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visibility` (
  `id` bigint NOT NULL,
  `name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `remark` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visibility`
--

LOCK TABLES `visibility` WRITE;
/*!40000 ALTER TABLE `visibility` DISABLE KEYS */;
INSERT INTO `visibility` VALUES (1,'Public',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(2,'Private',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(3,'Team',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(4,'Dept',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(5,'Internal',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(6,'External',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(7,'Partner',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(8,'VIP',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(9,'Guest',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(10,'Restricted',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(11,'Level1',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(12,'Level2',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(13,'Level3',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(14,'Level4',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(15,'Level5',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(16,'Level6',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(17,'Level7',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(18,'Level8',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(19,'Level9',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55'),(20,'Level10',NULL,'2025-08-19 10:06:55','2025-08-19 10:06:55');
/*!40000 ALTER TABLE `visibility` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-20  3:01:26
