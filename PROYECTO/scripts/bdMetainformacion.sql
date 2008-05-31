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
INSERT INTO `Componente` VALUES (1,'visor',1),(2,'lista',1),(3,'checkBox',1),(4,'toggleButton',1),(5,'button',1),(6,'comboBox',1),(7,'barramenu',1),(8,'menu1',1),(9,'menu2',1),(10,'menu3',1),(11,'item1_1',1),(12,'item1_2',1),(13,'item1_3',1),(14,'submenu1',1),(15,'subitem_1',1),(16,'subitem_2',1),(17,'item2_1',1),(18,'item2_2',1),(19,'item2_3',1),(20,'item3_1',1),(21,'item3_3',1),(22,'chat',1),(23,'arbol',1),(24,'campoTexto',1),(25,'MousesRemotos',1),(26,'ListaUsuariosConectados',1),(27,'ListaUsuariosConectadosRol',1),(28,'ListaUsuariosConectadosInfoRol',1),(29,'CambioRol',1),(30,'EtiquetaRolActual',1),(31,'Componente1',1),(32,'Componente2',1),(33,'Componente3',1),(34,'Componente4',1),(35,'Componente5',1),(36,'Lista',1),(37,'Lista2',1),(38,'Boton',1),(39,'Arbol',1),(40,'panelDibujo',1),(41,'lienzo',1),(42,'mousesRemotos2',1),(43,'menu1',1),(44,'chat',1),(45,'panelChat',1),(46,'mousesRemotos3',1),(47,'chatplugin',1),(48,'Pizarra',1),(49,'usuariosMail',1),(50,'listaUsuarios',1);
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
INSERT INTO `Initlog` VALUES ('2008-04-21 16:47:08'),('2008-04-21 16:47:38'),('2008-04-21 16:50:40'),('2008-04-21 16:59:19'),('2008-04-21 17:08:55'),('2008-04-21 17:48:30'),('2008-04-21 18:31:07'),('2008-04-21 18:39:38'),('2008-04-23 19:17:00'),('2008-04-23 19:23:56'),('2008-04-27 11:44:21'),('2008-04-27 13:02:49'),('2008-04-27 13:28:32'),('2008-04-27 13:32:30'),('2008-04-27 13:40:56'),('2008-04-27 14:15:08'),('2008-04-27 14:32:18'),('2008-04-27 17:33:51'),('2008-04-27 18:17:04'),('2008-04-27 18:39:37'),('2008-04-27 18:44:21'),('2008-04-27 18:48:23'),('2008-04-27 18:50:16'),('2008-04-27 19:03:35'),('2008-04-27 19:04:04'),('2008-04-27 19:04:30'),('2008-04-27 19:05:18'),('2008-04-27 19:39:52'),('2008-04-27 19:44:48'),('2008-04-27 19:47:05'),('2008-04-27 19:53:19'),('2008-04-27 19:57:59'),('2008-04-27 20:28:10'),('2008-04-27 20:30:02'),('2008-04-27 20:34:34'),('2008-04-27 23:26:58'),('2008-04-28 00:07:59'),('2008-04-28 00:37:07'),('2008-04-28 12:11:21'),('2008-04-28 13:15:12'),('2008-04-28 14:19:53'),('2008-04-28 14:31:53'),('2008-04-28 14:37:54'),('2008-04-28 17:40:32'),('2008-04-28 17:48:26'),('2008-04-28 19:04:44'),('2008-04-28 19:09:46'),('2008-04-28 19:11:48'),('2008-04-28 19:14:30'),('2008-04-28 19:28:42'),('2008-04-28 19:41:51'),('2008-04-28 21:03:58'),('2008-04-28 22:22:18'),('2008-04-28 22:30:44'),('2008-04-29 18:22:32'),('2008-04-29 18:26:55'),('2008-04-29 18:59:56'),('2008-04-29 20:34:45'),('2008-04-29 20:52:06'),('2008-04-29 20:57:00'),('2008-04-29 21:13:57'),('2008-04-29 21:41:04'),('2008-04-29 21:42:00'),('2008-04-30 00:08:49'),('2008-04-30 12:13:53'),('2008-04-30 13:58:11'),('2008-05-01 18:20:33'),('2008-05-01 18:21:57'),('2008-05-01 18:23:02'),('2008-05-01 18:25:18'),('2008-05-01 18:26:44'),('2008-05-01 18:43:13'),('2008-05-01 19:39:57'),('2008-05-01 20:32:29'),('2008-05-01 20:32:51'),('2008-05-01 22:04:45'),('2008-05-01 22:20:28'),('2008-05-01 22:27:49'),('2008-05-01 22:47:50'),('2008-05-01 22:55:55'),('2008-05-03 19:01:17'),('2008-05-03 20:58:01'),('2008-05-04 13:41:31'),('2008-05-05 12:10:29'),('2008-05-05 13:28:25'),('2008-05-05 17:12:49'),('2008-05-05 17:15:28'),('2008-05-05 17:19:21'),('2008-05-05 17:37:19'),('2008-05-05 17:40:35'),('2008-05-06 16:48:54'),('2008-05-06 18:02:50'),('2008-05-07 14:06:09'),('2008-05-07 14:42:21'),('2008-05-09 17:50:30'),('2008-05-09 18:34:23'),('2008-05-09 19:52:46'),('2008-05-09 19:55:25'),('2008-05-09 20:17:05'),('2008-05-09 20:19:36'),('2008-05-10 18:44:39'),('2008-05-10 21:12:20'),('2008-05-11 17:07:11'),('2008-05-11 17:10:25'),('2008-05-12 12:50:27'),('2008-05-12 12:52:00'),('2008-05-23 12:06:48'),('2008-05-23 12:30:18'),('2008-05-23 12:46:27'),('2008-05-23 15:36:27'),('2008-05-23 15:51:08'),('2008-05-23 16:01:58'),('2008-05-23 16:35:54'),('2008-05-23 17:53:26'),('2008-05-23 18:02:53'),('2008-05-23 18:06:23'),('2008-05-23 18:07:12'),('2008-05-24 01:50:12'),('2008-05-24 13:05:26'),('2008-05-24 13:57:58'),('2008-05-24 14:20:48'),('2008-05-24 14:34:10'),('2008-05-24 15:07:19'),('2008-05-24 15:41:24'),('2008-05-24 16:17:13'),('2008-05-24 16:17:22'),('2008-05-24 16:18:14'),('2008-05-24 17:08:04'),('2008-05-24 17:08:43'),('2008-05-24 17:12:22'),('2008-05-24 17:52:36'),('2008-05-24 18:34:10'),('2008-05-24 18:50:03'),('2008-05-24 18:57:30'),('2008-05-24 19:01:47'),('2008-05-25 19:39:20'),('2008-05-25 19:46:36'),('2008-05-25 19:50:27'),('2008-05-25 19:54:46'),('2008-05-25 20:14:51'),('2008-05-25 20:32:04'),('2008-05-25 20:53:23'),('2008-05-25 21:37:30'),('2008-05-25 21:38:52'),('2008-05-25 21:40:02'),('2008-05-25 21:57:10'),('2008-05-26 11:32:25'),('2008-05-26 11:37:43'),('2008-05-26 12:25:06'),('2008-05-26 15:12:27'),('2008-05-26 15:33:26'),('2008-05-26 15:43:09'),('2008-05-26 16:08:33'),('2008-05-26 23:08:49'),('2008-05-26 23:12:08'),('2008-05-27 12:53:55'),('2008-05-27 14:00:48'),('2008-05-27 18:52:26'),('2008-05-28 22:13:04'),('2008-05-28 22:17:46'),('2008-05-29 12:21:07'),('2008-05-29 12:52:22'),('2008-05-29 13:48:40'),('2008-05-29 13:51:41'),('2008-05-29 13:57:55'),('2008-05-29 14:02:16'),('2008-05-29 14:12:38'),('2008-05-29 14:16:42'),('2008-05-29 14:24:05'),('2008-05-29 14:47:45'),('2008-05-29 14:52:44'),('2008-05-29 15:29:14'),('2008-05-29 18:20:51'),('2008-05-29 18:22:50'),('2008-05-29 18:26:49'),('2008-05-29 18:33:58'),('2008-05-29 18:39:50'),('2008-05-29 20:57:41'),('2008-05-29 21:55:17'),('2008-05-29 23:05:56'),('2008-05-29 23:28:41'),('2008-05-29 23:52:33'),('2008-05-29 23:54:47'),('2008-05-29 23:56:38'),('2008-05-30 00:01:16'),('2008-05-30 00:02:39'),('2008-05-30 13:01:36'),('2008-05-30 13:40:07'),('2008-05-30 13:50:30'),('2008-05-30 14:00:28'),('2008-05-30 14:05:55'),('2008-05-30 14:06:59'),('2008-05-30 14:08:23'),('2008-05-30 14:12:11'),('2008-05-30 14:34:29'),('2008-05-30 14:39:06'),('2008-05-30 14:45:55'),('2008-05-30 14:47:48'),('2008-05-30 14:50:19'),('2008-05-30 17:36:24'),('2008-05-30 17:37:25'),('2008-05-30 17:48:18'),('2008-05-30 17:54:27'),('2008-05-30 17:56:38'),('2008-05-30 17:58:10'),('2008-05-30 17:59:03'),('2008-05-30 18:38:34'),('2008-05-30 18:58:21'),('2008-05-30 19:00:01'),('2008-05-30 19:01:05'),('2008-05-30 19:15:15'),('2008-05-30 19:18:23'),('2008-05-30 19:19:28'),('2008-05-30 20:58:43'),('2008-05-30 20:59:33'),('2008-05-30 21:00:00'),('2008-05-30 21:03:59'),('2008-05-30 21:07:30'),('2008-05-30 22:51:12'),('2008-05-30 23:47:05'),('2008-05-31 00:26:04'),('2008-05-31 00:52:46'),('2008-05-31 01:18:14'),('2008-05-31 01:22:26'),('2008-05-31 01:24:36'),('2008-05-31 01:46:06');
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
INSERT INTO `Permitidos` VALUES (1,1),(6,1),(1,2),(2,2),(1,3),(4,3),(5,3),(3,4),(1,5),(3,5),(4,5),(1,6),(2,6),(4,6);
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
INSERT INTO `Rol` VALUES (1,'Boss'),(2,'Manager'),(3,'Ejecutive'),(4,'Janitor'),(5,'Unknown'),(6,'Externall Asistant');
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
INSERT INTO `Usuario` VALUES (1,'Ana','clave',1,1),(2,'Carlos','clave',2,1),(3,'Jose','clave',5,1),(4,'Peter','clave',3,1),(5,'Elena','clave',4,1),(6,'Susan','clave',2,1);
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
INSERT INTO `permisosRol` VALUES (1,1,0),(1,2,20),(1,5,20),(1,22,20),(1,40,20),(1,47,20),(1,48,20),(1,49,20),(2,3,20),(2,22,20),(2,47,20),(2,48,20),(2,49,20);
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
INSERT INTO `permisosUsuario` VALUES (1,1,20),(1,2,20),(1,5,20),(1,22,20),(1,40,20),(1,47,20),(1,48,20),(1,49,20),(2,1,20),(2,2,20),(2,5,20),(2,22,20),(2,40,20),(2,47,20),(2,48,20),(2,49,20),(3,49,20),(4,49,20),(5,49,20),(6,49,20);
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
INSERT INTO `posibleRol` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1);
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

-- Dump completed on 2008-05-31  0:04:44
