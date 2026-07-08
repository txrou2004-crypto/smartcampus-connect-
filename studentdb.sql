-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: studentdb
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `academic_records`
--

DROP TABLE IF EXISTS `academic_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `academic_records` (
  `record_id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(10) NOT NULL,
  `academic_year` varchar(9) NOT NULL,
  `semester` int NOT NULL,
  `gpa` decimal(3,2) NOT NULL,
  `cgpa` decimal(3,2) NOT NULL,
  `credits_earned` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`record_id`),
  KEY `fk_student` (`student_id`),
  CONSTRAINT `fk_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`),
  CONSTRAINT `chk_cgpa` CHECK ((`cgpa` between 0.00 and 4.00)),
  CONSTRAINT `chk_credits` CHECK ((`credits_earned` >= 0)),
  CONSTRAINT `chk_gpa` CHECK ((`gpa` between 0.00 and 4.00)),
  CONSTRAINT `chk_semester` CHECK ((`semester` between 1 and 3))
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `academic_records`
--

LOCK TABLES `academic_records` WRITE;
/*!40000 ALTER TABLE `academic_records` DISABLE KEYS */;
INSERT INTO `academic_records` VALUES (1,'S1001','2024/2025',1,3.45,3.45,18),(2,'S1001','2024/2025',2,3.60,3.53,36),(3,'S1002','2024/2025',1,3.20,3.20,18),(4,'S1002','2024/2025',2,3.40,3.30,36),(5,'S1003','2024/2025',1,3.75,3.75,18),(6,'S1003','2024/2025',2,3.82,3.79,36),(7,'S1004','2024/2025',1,3.55,3.55,18),(8,'S1004','2024/2025',2,3.61,3.58,36),(9,'S1005','2024/2025',1,3.10,3.10,18),(10,'S1005','2024/2025',2,3.25,3.18,36),(11,'S1006','2024/2025',1,3.90,3.90,18),(12,'S1006','2024/2025',2,3.88,3.89,36),(13,'S1007','2024/2025',1,3.33,3.33,18),(14,'S1007','2024/2025',2,3.47,3.40,36),(15,'S1008','2024/2025',1,3.68,3.68,18),(16,'S1008','2024/2025',2,3.72,3.70,36),(17,'S1009','2024/2025',1,3.25,3.25,18),(18,'S1009','2024/2025',2,3.35,3.30,36),(19,'S1010','2024/2025',1,3.80,3.80,18),(20,'S1010','2024/2025',2,3.85,3.83,36);
/*!40000 ALTER TABLE `academic_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `student_id` varchar(10) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `tel_number` varchar(20) NOT NULL,
  `programme_code` varchar(20) NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`student_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('S1001','John','Tan','john.tan@student.edu','0123456781','CS110','ACTIVE'),('S1002','Sarah','Lee','sarah.lee@student.edu','0123456782','CS110','ACTIVE'),('S1003','Kevin','Lim','kevin.lim@student.edu','0123456783','SE220','ACTIVE'),('S1004','Amanda','Wong','amanda.wong@student.edu','0123456784','SE220','ACTIVE'),('S1005','Jason','Ng','jason.ng@student.edu','0123456785','IT330','ACTIVE'),('S1006','Melissa','Chong','melissa.chong@student.edu','0123456786','IT330','ACTIVE'),('S1007','Daniel','Ong','daniel.ong@student.edu','0123456787','CS110','ACTIVE'),('S1008','Rachel','Yap','rachel.yap@student.edu','0123456788','SE220','ACTIVE'),('S1009','Bryan','Khoo','bryan.khoo@student.edu','0123456789','IT330','ACTIVE'),('S1010','Grace','Teh','grace.teh@student.edu','0123456790','CS110','ACTIVE');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-26 19:49:05
