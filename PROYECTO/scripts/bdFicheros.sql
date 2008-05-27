-- MySQL dump 10.11
--
-- Host: localhost    Database: ficheros
-- ------------------------------------------------------
-- Server version	5.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Anotacion`
--

DROP TABLE IF EXISTS `Anotacion`;
CREATE TABLE `Anotacion` (
  `id_anotacion` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_rol` int(11) NOT NULL,
  `id_fichero` int(11) NOT NULL,
  `pagina` int(11) NOT NULL,
  `posicion_origenx` int(11) NOT NULL,
  `posicion_origeny` int(11) NOT NULL,
  `posicion_finx` int(11) NOT NULL,
  `posicion_finy` int(11) NOT NULL,
  `contenido` text NOT NULL,
  PRIMARY KEY  (`id_anotacion`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Anotacion`
--

LOCK TABLES `Anotacion` WRITE;
/*!40000 ALTER TABLE `Anotacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `Anotacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Fichero`
--

DROP TABLE IF EXISTS `Fichero`;
CREATE TABLE `Fichero` (
  `id_fichero` int(11) NOT NULL,
  `nombre` varchar(256) NOT NULL,
  `es_directorio` tinyint(1) NOT NULL,
  `permisos` varchar(6) NOT NULL,
  `usuario` int(11) default NULL,
  `rol` int(11) default NULL,
  `padre` int(11) default NULL,
  `ruta_local` varchar(512) default NULL,
  `tipo` varchar(10) default NULL,
  PRIMARY KEY  (`id_fichero`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Fichero`
--

LOCK TABLES `Fichero` WRITE;
/*!40000 ALTER TABLE `Fichero` DISABLE KEYS */;
INSERT INTO `Fichero` VALUES (1,'/',1,'rwrwrw',NULL,NULL,NULL,'/',NULL),(2,'Personal',1,'rwrwr-',1,1,1,'/Personal',NULL),(3,'memorandum.doc',0,'rwr-r-',1,1,2,'/Personal/memorandum.doc','doc'),(4,'noseque.pdf',0,'rw----',2,2,1,'/noseque.pdf','pdf'),(7,'Public',1,'rwrwrw',1,1,1,'/Public',NULL),(8,'informe.pdf',0,'rwrwrw',1,1,7,'/Public/informe.pdf','pdf'),(9,'topSecret',1,'rwr-r-',1,1,2,'/Personal/topSecret',NULL),(10,'planosEstrellaDeLaMuerte.dwg',0,'rwr-r-',1,1,9,'/Personal/topSecret/planosEstrellaDeLaMuerte.dwg','dwg'),(12,'hst_stingray_nebula.jpg',0,'r-r-r-',1,1,1,'/hst_stingray_nebula.jpg','img'),(14,'publico2.txt',0,'rwrw--',1,1,1,'/publico2.txt','txt'),(15,'curriculum.pdf',0,'rwrwr-',1,1,1,'/curriculum.pdf','pdf'),(16,'horario.pdf',0,'rwrw--',1,1,1,'/horario.pdf','pdf');
/*!40000 ALTER TABLE `Fichero` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2008-05-27 15:40:30
