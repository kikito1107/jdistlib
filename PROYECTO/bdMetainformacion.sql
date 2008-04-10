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
INSERT INTO `Componente` VALUES (0,'visor',1),(1,'lista',1),(2,'checkBox',1),(3,'toggleButton',1),(4,'button',1),(5,'comboBox',1),(6,'barramenu',1),(7,'menu1',1),(8,'menu2',1),(9,'menu3',1),(10,'item1_1',1),(11,'item1_2',1),(12,'item1_3',1),(13,'submenu1',1),(14,'subitem_1',1),(15,'subitem_2',1),(16,'item2_1',1),(17,'item2_2',1),(18,'item2_3',1),(19,'item3_1',1),(20,'item3_3',1),(21,'chat',1),(22,'arbol',1),(23,'campoTexto',1),(24,'MousesRemotos',1),(25,'ListaUsuariosConectados',1),(26,'ListaUsuariosConectadosRol',1),(27,'ListaUsuariosConectadosInfoRol',1),(28,'CambioRol',1),(29,'EtiquetaRolActual',1),(30,'Componente1',1),(31,'Componente2',1),(32,'Componente3',1),(33,'Componente4',1),(34,'Componente5',1),(35,'Lista',1),(36,'Lista2',1),(37,'Boton',1),(38,'Arbol',1),(39,'panelDibujo',1),(40,'lienzo',1),(41,'',1);
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
INSERT INTO `Initlog` VALUES ('2008-01-21 20:37:38'),('2008-01-21 20:38:47'),('2008-01-21 20:40:07'),('2008-01-21 20:44:40'),('2008-01-21 20:44:44'),('2008-01-21 20:45:34'),('2008-01-21 20:46:36'),('2008-01-21 20:48:25'),('2008-01-21 20:52:29'),('2008-01-21 20:57:46'),('2008-01-23 19:01:14'),('2008-01-23 19:01:48'),('2008-01-23 19:24:49'),('2008-01-31 23:09:33'),('2008-01-31 23:16:33'),('2008-01-31 23:18:05'),('2008-01-31 23:28:22'),('2008-01-31 23:35:57'),('2008-01-31 23:37:28'),('2008-01-31 23:56:23'),('2008-01-31 23:59:25'),('2008-02-01 00:01:18'),('2008-02-01 00:04:32'),('2008-02-01 00:08:08'),('2008-02-01 00:18:48'),('2008-02-01 00:37:32'),('2008-02-09 13:52:53'),('2008-02-09 13:59:54'),('2008-02-09 14:08:45'),('2008-02-09 14:09:41'),('2008-02-09 14:21:40'),('2008-02-09 14:22:38'),('2008-02-09 15:08:13'),('2008-02-11 14:36:40'),('2008-02-11 14:37:58'),('2008-02-11 14:38:42'),('2008-02-11 14:49:18'),('2008-02-11 20:20:14'),('2008-02-11 20:22:30'),('2008-02-14 15:19:42'),('2008-02-14 15:19:52'),('2008-02-14 15:20:36'),('2008-02-14 16:06:39'),('2008-02-14 16:19:35'),('2008-02-14 16:30:37'),('2008-02-14 17:02:01'),('2008-02-14 17:07:22'),('2008-02-14 22:31:49'),('2008-02-23 12:16:17'),('2008-02-23 12:48:34'),('2008-02-23 12:50:14'),('2008-02-23 19:27:52'),('2008-02-23 20:39:16'),('2008-02-26 17:30:51'),('2008-02-26 17:32:04'),('2008-02-26 17:53:16'),('2008-02-26 18:23:51'),('2008-02-26 18:34:09'),('2008-02-26 19:46:19'),('2008-02-26 19:47:45'),('2008-02-26 20:02:56'),('2008-02-27 17:39:43'),('2008-02-27 17:43:40'),('2008-02-27 17:49:09'),('2008-02-27 18:57:46'),('2008-02-27 19:29:38'),('2008-02-27 19:30:36'),('2008-03-03 14:02:04'),('2008-03-03 15:02:00'),('2008-03-03 15:04:01'),('2008-03-03 15:20:34'),('2008-03-03 17:52:31'),('2008-03-03 18:53:16'),('2008-03-03 18:53:44'),('2008-03-03 18:54:21'),('2008-03-03 18:54:41'),('2008-03-03 18:55:54'),('2008-03-03 19:01:05'),('2008-03-03 19:09:08'),('2008-03-03 19:16:22'),('2008-03-03 19:34:30'),('2008-03-03 19:47:04'),('2008-03-03 19:47:37'),('2008-03-05 19:12:34'),('2008-03-05 20:32:27'),('2008-03-05 21:32:15'),('2008-03-05 21:32:59'),('2008-03-06 11:34:56'),('2008-03-06 21:59:06'),('2008-03-06 22:19:12'),('2008-03-06 22:19:38'),('2008-03-06 23:21:48'),('2008-03-06 23:30:28'),('2008-03-07 00:23:33'),('2008-03-07 00:24:09'),('2008-03-11 10:58:20'),('2008-03-11 13:51:55'),('2008-03-11 14:01:41'),('2008-03-11 14:12:45'),('2008-03-11 14:31:30'),('2008-03-11 15:08:39'),('2008-03-11 15:11:34'),('2008-03-11 15:13:59'),('2008-03-11 15:15:35'),('2008-03-11 15:25:22'),('2008-03-11 15:29:19'),('2008-03-11 15:30:44'),('2008-03-11 15:31:58'),('2008-03-11 15:33:25'),('2008-03-11 16:44:44'),('2008-03-11 16:45:32'),('2008-03-11 16:46:49'),('2008-03-11 16:52:16'),('2008-03-11 16:54:47'),('2008-03-11 16:55:21'),('2008-03-11 16:56:51'),('2008-03-11 17:08:18'),('2008-03-11 17:10:14'),('2008-03-11 17:10:58'),('2008-03-11 17:15:31'),('2008-03-11 17:30:14'),('2008-03-11 17:31:28'),('2008-03-11 17:32:18'),('2008-03-11 17:33:25');
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
INSERT INTO `permisosRol` VALUES (1,0,0),(1,1,20),(1,4,20),(1,39,20),(2,2,20);
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
INSERT INTO `permisosUsuario` VALUES (1,0,20),(1,1,20),(1,4,20),(1,39,20),(2,0,20),(2,1,20),(2,4,20),(2,39,20),(6,0,20),(6,1,20),(6,4,20),(6,39,20);
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

-- Dump completed on 2008-03-11 17:00:30
