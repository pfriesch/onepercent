-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: db.f4.htw-berlin.de:3306
-- Erstellungszeit: 28. Dez 2014 um 20:29
-- Server Version: 5.5.40-0+wheezy1-log
-- PHP-Version: 5.4.34-0+deb7u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `_s0540031__Twitter`
--
/*
CREATE DATABASE IF NOT EXISTS `_s0540031__Twitter` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `_s0540031__Twitter`;
*/

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `countalltags`
--

DROP TABLE IF EXISTS `countalltags`;
CREATE TABLE IF NOT EXISTS `countalltags` (
  `count` int(11) DEFAULT NULL,
  `timestamp` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `languagedistribution`
--

DROP TABLE IF EXISTS `languagedistribution`;
CREATE TABLE IF NOT EXISTS `languagedistribution` (
  `language` varchar(20) NOT NULL,
  `count` int(11) NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`language`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `origintweets`
--

DROP TABLE IF EXISTS `origintweets`;
CREATE TABLE IF NOT EXISTS `origintweets` (
  `name` varchar(20) NOT NULL,
  `count` int(11) NOT NULL,
  `timestamp` datetime NOT NULL,
  PRIMARY KEY (`name`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `toptentags`
--

DROP TABLE IF EXISTS `toptentags`;
CREATE TABLE IF NOT EXISTS `toptentags` (
  `name` varchar(20) NOT NULL,
  `timestamp` datetime NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`name`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tweetids`
--

DROP TABLE IF EXISTS `tweetids`;
CREATE TABLE IF NOT EXISTS `tweetids` (
  `name` varchar(30) NOT NULL,
  `tweetid` varchar(20) NOT NULL,
  `written` datetime NOT NULL,
  PRIMARY KEY (`name`,`tweetid`,`written`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tweetsatdaytime`
--

DROP TABLE IF EXISTS `tweetsatdaytime`;
CREATE TABLE IF NOT EXISTS `tweetsatdaytime` (
  `timestamp` datetime NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `wordsearch`
--

DROP TABLE IF EXISTS `wordsearch`;
CREATE TABLE IF NOT EXISTS `wordsearch` (
  `name` varchar(30) NOT NULL,
  `timestamp` datetime NOT NULL,
  `count` int(11) NOT NULL,
  `written` datetime NOT NULL,
  PRIMARY KEY (`name`,`timestamp`,`written`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
