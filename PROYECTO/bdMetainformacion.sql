-- MySQL dump 10.11
--
-- Host: localhost    Database: metainformacion
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
-- Table structure for table `Aplicacion`
--

DROP TABLE IF EXISTS `Aplicacion`;
CREATE TABLE `Aplicacion` (
  `id_aplicacion` int(11) NOT NULL auto_increment,
  `nombre_aplicacion` tinytext NOT NULL,
  `nivel` int(2) NOT NULL,
  PRIMARY KEY  (`id_aplicacion`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Aplicacion`
--

LOCK TABLES `Aplicacion` WRITE;
/*!40000 ALTER TABLE `Aplicacion` DISABLE KEYS */;
INSERT INTO `Aplicacion` VALUES (1,'AplicacionDePrueba',20);
/*!40000 ALTER TABLE `Aplicacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Componente`
--

DROP TABLE IF EXISTS `Componente`;
CREATE TABLE `Componente` (
  `id_componente` int(11) NOT NULL auto_increment,
  `nombre_componente` tinytext NOT NULL,
  `id_aplicacion` int(11) NOT NULL,
  PRIMARY KEY  (`id_componente`),
  KEY `Componente_ibfk_1` (`id_aplicacion`),
  CONSTRAINT `Componente_ibfk_1` FOREIGN KEY (`id_aplicacion`) REFERENCES `aplicacion` (`id_aplicacion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Componente`
--

LOCK TABLES `Componente` WRITE;
/*!40000 ALTER TABLE `Componente` DISABLE KEYS */;
INSERT INTO `Componente` VALUES (0,'visor',1),(1,'lista',1),(2,'checkBox',1),(3,'toggleButton',1),(4,'button',1),(5,'comboBox',1),(6,'barramenu',1),(7,'menu1',1),(8,'menu2',1),(9,'menu3',1),(10,'item1_1',1),(11,'item1_2',1),(12,'item1_3',1),(13,'submenu1',1),(14,'subitem_1',1),(15,'subitem_2',1),(16,'item2_1',1),(17,'item2_2',1),(18,'item2_3',1),(19,'item3_1',1),(20,'item3_3',1),(21,'chat',1),(22,'arbol',1),(23,'campoTexto',1),(24,'MousesRemotos',1),(25,'ListaUsuariosConectados',1),(26,'ListaUsuariosConectadosRol',1),(27,'ListaUsuariosConectadosInfoRol',1),(28,'CambioRol',1),(29,'EtiquetaRolActual',1),(30,'Componente1',1),(31,'Componente2',1),(32,'Componente3',1),(33,'Componente4',1),(34,'Componente5',1),(35,'Lista',1),(36,'Lista2',1),(37,'Boton',1),(38,'Arbol',1),(39,'panelDibujo',1),(40,'lienzo',1),(41,'mousesRemotos2',1),(42,'menu1',1),(43,'chat',1),(44,'panelChat',1),(45,'mousesRemotos3',1);
/*!40000 ALTER TABLE `Componente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Initlog`
--

DROP TABLE IF EXISTS `Initlog`;
CREATE TABLE `Initlog` (
  `fecha` datetime NOT NULL,
  PRIMARY KEY  (`fecha`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Initlog`
--

LOCK TABLES `Initlog` WRITE;
/*!40000 ALTER TABLE `Initlog` DISABLE KEYS */;
INSERT INTO `Initlog` VALUES ('2008-04-21 16:47:08'),('2008-04-21 16:47:38'),('2008-04-21 16:50:40'),('2008-04-21 16:59:19'),('2008-04-21 17:08:55'),('2008-04-21 17:48:30'),('2008-04-21 18:31:07'),('2008-04-21 18:39:38'),('2008-04-23 19:17:00'),('2008-04-23 19:23:56'),('2008-04-27 11:44:21'),('2008-04-27 13:02:49'),('2008-04-27 13:28:32'),('2008-04-27 13:32:30'),('2008-04-27 13:40:56'),('2008-04-27 14:15:08'),('2008-04-27 14:32:18'),('2008-04-27 17:33:51'),('2008-04-27 18:17:04'),('2008-04-27 18:39:37'),('2008-04-27 18:44:21'),('2008-04-27 18:48:23'),('2008-04-27 18:50:16'),('2008-04-27 19:03:35'),('2008-04-27 19:04:04'),('2008-04-27 19:04:30'),('2008-04-27 19:05:18'),('2008-04-27 19:39:52'),('2008-04-27 19:44:48'),('2008-04-27 19:47:05'),('2008-04-27 19:53:19'),('2008-04-27 19:57:59'),('2008-04-27 20:28:10'),('2008-04-27 20:30:02'),('2008-04-27 20:34:34'),('2008-04-27 23:26:58'),('2008-04-28 00:07:59'),('2008-04-28 00:37:07'),('2008-04-28 12:11:21'),('2008-04-28 13:15:12');
/*!40000 ALTER TABLE `Initlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Permitidos`
--

DROP TABLE IF EXISTS `Permitidos`;
CREATE TABLE `Permitidos` (
  `id_rol` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  PRIMARY KEY  (`id_rol`,`id_usuario`),
  KEY `permitidos_ibfk_2` (`id_usuario`),
  CONSTRAINT `permitidos_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `permitidos_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Permitidos`
--

LOCK TABLES `Permitidos` WRITE;
/*!40000 ALTER TABLE `Permitidos` DISABLE KEYS */;
INSERT INTO `Permitidos` VALUES (1,1),(2,1),(3,1),(5,1),(2,2),(5,3),(3,4),(2,6);
/*!40000 ALTER TABLE `Permitidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Rol`
--

DROP TABLE IF EXISTS `Rol`;
CREATE TABLE `Rol` (
  `id_rol` int(11) NOT NULL auto_increment,
  `nombre_rol` tinytext NOT NULL,
  PRIMARY KEY  (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Rol`
--

LOCK TABLES `Rol` WRITE;
/*!40000 ALTER TABLE `Rol` DISABLE KEYS */;
INSERT INTO `Rol` VALUES (1,'MaloMalisimo'),(2,'Jedi'),(3,'Princesa'),(4,'Wookie'),(5,'Contrabandista');
/*!40000 ALTER TABLE `Rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Usuario`
--

DROP TABLE IF EXISTS `Usuario`;
CREATE TABLE `Usuario` (
  `id_usuario` int(11) NOT NULL auto_increment,
  `nombre_usuario` tinytext NOT NULL,
  `clave` tinytext NOT NULL,
  `id_rol_predeterminado` int(11) NOT NULL,
  `administrador` tinyint(1) NOT NULL,
  PRIMARY KEY  (`id_usuario`),
  KEY `Usuario_ibfk_1` (`id_rol_predeterminado`),
  CONSTRAINT `Usuario_ibfk_1` FOREIGN KEY (`id_rol_predeterminado`) REFERENCES `rol` (`id_rol`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `Usuario`
--

LOCK TABLES `Usuario` WRITE;
/*!40000 ALTER TABLE `Usuario` DISABLE KEYS */;
INSERT INTO `Usuario` VALUES (1,'DarthVader','clave',1,1),(2,'Luke','clave',2,1),(3,'HanSolo','clave',5,0),(4,'Leia','clave',3,0),(5,'Chewbacca','clave',4,0),(6,'Yoda','clave',2,1);
/*!40000 ALTER TABLE `Usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permisosRol`
--

DROP TABLE IF EXISTS `permisosRol`;
CREATE TABLE `permisosRol` (
  `id_rol` int(11) NOT NULL,
  `id_componente` int(11) NOT NULL,
  `nivel` int(2) NOT NULL,
  PRIMARY KEY  (`id_rol`,`id_componente`),
  KEY `permisosRol_ibfk_2` (`id_componente`),
  CONSTRAINT `permisosRol_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `permisosRol_ibfk_2` FOREIGN KEY (`id_componente`) REFERENCES `componente` (`id_componente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `permisosRol`
--

LOCK TABLES `permisosRol` WRITE;
/*!40000 ALTER TABLE `permisosRol` DISABLE KEYS */;
INSERT INTO `permisosRol` VALUES (1,0,0),(1,1,20),(1,4,20),(1,39,20),(1,43,20),(2,2,20),(2,43,20);
/*!40000 ALTER TABLE `permisosRol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permisosUsuario`
--

DROP TABLE IF EXISTS `permisosUsuario`;
CREATE TABLE `permisosUsuario` (
  `id_usuario` int(11) NOT NULL,
  `id_componente` int(11) NOT NULL,
  `nivel` int(2) NOT NULL,
  PRIMARY KEY  (`id_usuario`,`id_componente`),
  KEY `permisosUsuario_ibfk_2` (`id_componente`),
  CONSTRAINT `permisosUsuario_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `permisosUsuario_ibfk_2` FOREIGN KEY (`id_componente`) REFERENCES `componente` (`id_componente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `permisosUsuario`
--

LOCK TABLES `permisosUsuario` WRITE;
/*!40000 ALTER TABLE `permisosUsuario` DISABLE KEYS */;
INSERT INTO `permisosUsuario` VALUES (1,0,20),(1,1,20),(1,4,20),(1,39,20),(1,43,20),(2,0,20),(2,1,20),(2,4,20),(2,39,20),(2,43,20),(6,0,20),(6,1,20),(6,4,20),(6,39,20),(6,43,20);
/*!40000 ALTER TABLE `permisosUsuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posibleRol`
--

DROP TABLE IF EXISTS `posibleRol`;
CREATE TABLE `posibleRol` (
  `id_rol` int(11) NOT NULL,
  `id_aplicacion` int(11) NOT NULL,
  PRIMARY KEY  (`id_rol`,`id_aplicacion`),
  KEY `posibleRol_ibfk_2` (`id_aplicacion`),
  CONSTRAINT `posibleRol_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `posibleRol_ibfk_2` FOREIGN KEY (`id_aplicacion`) REFERENCES `aplicacion` (`id_aplicacion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `posibleRol`
--

LOCK TABLES `posibleRol` WRITE;
/*!40000 ALTER TABLE `posibleRol` DISABLE KEYS */;
INSERT INTO `posibleRol` VALUES (1,1),(2,1),(3,1),(4,1),(5,1);
/*!40000 ALTER TABLE `posibleRol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posibleUsuario`
--

DROP TABLE IF EXISTS `posibleUsuario`;
CREATE TABLE `posibleUsuario` (
  `id_usuario` int(11) NOT NULL,
  `id_aplicacion` int(11) NOT NULL,
  PRIMARY KEY  (`id_usuario`,`id_aplicacion`),
  KEY `posibleUsuario_ibfk_2` (`id_aplicacion`),
  CONSTRAINT `posibleUsuario_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `posibleUsuario_ibfk_2` FOREIGN KEY (`id_aplicacion`) REFERENCES `aplicacion` (`id_aplicacion`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `posibleUsuario`
--

LOCK TABLES `posibleUsuario` WRITE;
/*!40000 ALTER TABLE `posibleUsuario` DISABLE KEYS */;
INSERT INTO `posibleUsuario` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1);
/*!40000 ALTER TABLE `posibleUsuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2008-04-28 11:46:45
