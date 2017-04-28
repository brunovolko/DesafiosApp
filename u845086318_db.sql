-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: 10.1.2.125
-- Generation Time: Apr 27, 2017 at 01:37 PM
-- Server version: 10.0.28-MariaDB-wsrep
-- PHP Version: 7.0.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `u845086318_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `comentario`
--

CREATE TABLE `comentario` (
  `ID` int(255) UNSIGNED NOT NULL,
  `COMENTARIO` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `DESAFIO` int(255) NOT NULL,
  `USUARIO` int(255) NOT NULL,
  `ESTADO` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'activo'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `desafio`
--

CREATE TABLE `desafio` (
  `ID` int(255) UNSIGNED NOT NULL,
  `DESAFIO` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `FECHAHORA` date NOT NULL,
  `USUARIO` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `negativo`
--

CREATE TABLE `negativo` (
  `ID` int(255) UNSIGNED NOT NULL,
  `DESAFIO` int(255) NOT NULL,
  `USUARIO` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notificacion`
--

CREATE TABLE `notificacion` (
  `ID` int(255) UNSIGNED NOT NULL,
  `TIPO` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `USUARIO` int(255) NOT NULL,
  `REFERENCIA` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `positivo`
--

CREATE TABLE `positivo` (
  `ID` int(255) UNSIGNED NOT NULL,
  `USUARIO` int(255) NOT NULL,
  `PUBLICACION` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `publicacion`
--

CREATE TABLE `publicacion` (
  `ID` int(255) UNSIGNED NOT NULL,
  `DESAFIO` int(255) NOT NULL,
  `USUARIO` int(255) NOT NULL,
  `FECHAHORA` date NOT NULL,
  `POSITIVO` int(255) NOT NULL DEFAULT '0',
  `NEGATIVO` int(255) NOT NULL DEFAULT '0',
  `ESTADO` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'activa'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

CREATE TABLE `usuario` (
  `ID` int(255) UNSIGNED NOT NULL,
  `NOMBRE` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `APELLIDO` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `USUARIO` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `EMAIL` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `CONTRASEÑA` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ESTADO` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT 'pendiente'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `comentario`
--
ALTER TABLE `comentario`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `desafio`
--
ALTER TABLE `desafio`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `negativo`
--
ALTER TABLE `negativo`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `notificacion`
--
ALTER TABLE `notificacion`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `positivo`
--
ALTER TABLE `positivo`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `publicacion`
--
ALTER TABLE `publicacion`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `comentario`
--
ALTER TABLE `comentario`
  MODIFY `ID` int(255) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `desafio`
--
ALTER TABLE `desafio`
  MODIFY `ID` int(255) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `negativo`
--
ALTER TABLE `negativo`
  MODIFY `ID` int(255) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `notificacion`
--
ALTER TABLE `notificacion`
  MODIFY `ID` int(255) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `positivo`
--
ALTER TABLE `positivo`
  MODIFY `ID` int(255) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `publicacion`
--
ALTER TABLE `publicacion`
  MODIFY `ID` int(255) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `ID` int(255) UNSIGNED NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
