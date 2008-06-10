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
INSERT INTO `Fichero` VALUES (1,'/',1,'rwrwrw',NULL,NULL,NULL,'/',NULL),(2,'Public',1,'rwrw--',1,1,1,'/Public','null'),(3,'Incoming',1,'r-r-r-',1,1,1,'/Incoming','INCOMING'),(4,'SolicitudIdiomasIN.pdf',0,'rw----',1,1,1,'/SolicitudIdiomasIN.pdf','pdf'),(6,'bdFicheros.sql',0,'rwrwrw',1,1,1,'/bdFicheros.sql','sql'),(7,'automensaje.msg',0,'rw----',1,1,3,'/Incoming/automensaje.msg','msg'),(8,'Re automensaje.msg',0,'rw----',1,1,3,'/Incoming/Re automensaje.msg','msg'),(9,'Privado',1,'rw----',1,1,1,'/Privado','NULL'),(10,'Privado',1,'rwr---',2,2,1,'/Privado','NULL'),(12,'leeme.txt',0,'rwrwrw',2,2,1,'/leeme.txt','txt'),(15,'HOLA hola.msg',0,'rw----',1,1,3,'/Incoming/HOLA hola.msg','msg'),(17,'global_03-06-08_09:35:25.h',0,'rwrw--',1,1,2,'/Public/global_03-06-08_09:35:25.h','VER'),(18,'global_03-06-08_09:36:32.h',0,'rwrw--',1,1,1,'/global_03-06-08_09:36:32.h','VER'),(19,'global_04-06-08_03:11:20.h',0,'rwrw--',1,1,1,'/global_04-06-08_03:11:20.h','VER'),(20,'global.h',0,'rwrw--',1,1,1,'/global.h','h'),(21,'global_2.h',0,'rwrwrw',1,1,22,'/Antiguo/global_2.h','h'),(22,'Antiguo',1,'rwrwr-',1,1,1,'/Antiguo','NULL'),(23,'Hola.msg',0,'rw----',3,5,3,'/Incoming/Hola.msg','msg');
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

-- Dump completed on 2008-06-10 14:42:32
