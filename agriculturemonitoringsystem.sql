-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 19, 2025 at 08:41 PM
-- Server version: 8.3.0
-- PHP Version: 8.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `agriculturemonitoringsystem`
--

DELIMITER $$
--
-- Procedures
--
DROP PROCEDURE IF EXISTS `GetFarmerStatistics`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `GetFarmerStatistics` (IN `farmer_id` INT)   BEGIN
    SELECT 
        f.FarmerID,
        f.FullName,
        f.Location,
        f.Experience,
        COUNT(DISTINCT fl.FieldID) as TotalFields,
        SUM(fl.Area) as TotalArea,
        COUNT(DISTINCT c.CropID) as TotalCrops,
        COUNT(DISTINCT h.HarvestID) as TotalHarvests,
        COALESCE(SUM(m.QuantitySold * m.Price), 0) as TotalRevenue
    FROM Farmers f
    LEFT JOIN Fields fl ON f.FarmerID = fl.FarmerID
    LEFT JOIN Crops c ON fl.FieldID = c.FieldID
    LEFT JOIN Harvests h ON c.CropID = h.CropID
    LEFT JOIN Markets m ON h.HarvestID = m.HarvestID
    WHERE f.FarmerID = farmer_id
    GROUP BY f.FarmerID, f.FullName, f.Location, f.Experience;
END$$

DROP PROCEDURE IF EXISTS `UpdateCropStatus`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateCropStatus` (IN `crop_id` INT, IN `new_status` VARCHAR(20))   BEGIN
    UPDATE Crops 
    SET Status = new_status 
    WHERE CropID = crop_id;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `croprecommendations`
--

DROP TABLE IF EXISTS `croprecommendations`;
CREATE TABLE IF NOT EXISTS `croprecommendations` (
  `RecommendationID` int NOT NULL AUTO_INCREMENT,
  `Season` varchar(50) DEFAULT NULL,
  `SoilType` varchar(50) DEFAULT NULL,
  `Region` varchar(100) DEFAULT NULL,
  `RecommendedCrops` text,
  `Advice` text,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`RecommendationID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `croprecommendations`
--

INSERT INTO `croprecommendations` (`RecommendationID`, `Season`, `SoilType`, `Region`, `RecommendedCrops`, `Advice`, `CreatedAt`) VALUES
(1, 'Rainy Season', 'Loamy Soil', 'Kigali', 'Maize, Beans, Potatoes', 'Plant at onset of rains. Use organic manure for better yield.', '2025-10-24 14:37:11'),
(2, 'Dry Season', 'Sandy Soil', 'Northern Province', 'Cassava, Sweet Potatoes, Millet', 'Ensure proper irrigation. Use drought-resistant varieties.', '2025-10-24 14:37:11'),
(3, 'Rainy Season', 'Clay Soil', 'Eastern Province', 'Rice, Sugarcane, Vegetables', 'Good for paddy crops. Monitor water levels regularly.', '2025-10-24 14:37:11'),
(4, 'Both Seasons', 'Volcanic Soil', 'Western Province', 'Coffee, Tea, Bananas', 'Rich soil suitable for perennial crops. Regular pruning needed.', '2025-10-24 14:37:11'),
(5, 'Dry Season', 'Loamy Soil', 'Southern Province', 'Beans, Peas, Cabbage', 'Ideal for short-season crops. Use mulch to retain moisture.', '2025-10-24 14:37:11');

-- --------------------------------------------------------

--
-- Table structure for table `crops`
--

DROP TABLE IF EXISTS `crops`;
CREATE TABLE IF NOT EXISTS `crops` (
  `CropID` int NOT NULL AUTO_INCREMENT,
  `FieldID` int DEFAULT NULL,
  `Name` varchar(100) NOT NULL,
  `Description` text,
  `Category` varchar(50) DEFAULT NULL,
  `PriceOrValue` decimal(10,2) DEFAULT NULL,
  `Status` enum('PLANTED','GROWING','HARVESTED','SOLD') DEFAULT 'PLANTED',
  `PlantingDate` date DEFAULT NULL,
  `ExpectedHarvestDate` date DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CropID`),
  KEY `FieldID` (`FieldID`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `crops`
--

INSERT INTO `crops` (`CropID`, `FieldID`, `Name`, `Description`, `Category`, `PriceOrValue`, `Status`, `PlantingDate`, `ExpectedHarvestDate`, `CreatedAt`) VALUES
(1, 1, 'Maize', 'High-yield maize variety', 'Cereals', 150.00, 'GROWING', '2025-08-15', '2025-12-10', '2025-10-24 14:37:11'),
(2, 1, 'Beans', 'Climbing beans for intercropping', 'Legumes', 200.00, 'PLANTED', '2025-09-01', '2025-11-20', '2025-10-24 14:37:11'),
(3, 2, 'Tomatoes', 'Greenhouse tomatoes', 'Vegetables', 300.00, 'GROWING', '2025-07-20', '2025-10-30', '2025-10-24 14:37:11'),
(4, 2, 'Sweet Potatoes', 'Orange flesh variety', 'Roots', 180.00, 'PLANTED', '2025-08-10', '2025-12-15', '2025-10-24 14:37:11'),
(5, 3, 'Coffee', 'Arabica coffee plants', 'Beverages', 500.00, 'GROWING', '2024-03-15', '2025-06-20', '2025-10-24 14:37:11'),
(6, 3, 'Bananas', 'Cavendish bananas', 'Fruits', 250.00, 'HARVESTED', '2024-01-10', '2025-09-05', '2025-10-24 14:37:11'),
(7, 4, 'Rice', 'Paddy rice cultivation', 'Cereals', 220.00, 'GROWING', '2025-07-01', '2025-11-15', '2025-10-24 14:37:11'),
(8, 5, 'Avocado', 'Hass avocado trees', 'Fruits', 400.00, 'GROWING', '2024-05-20', '2025-10-25', '2025-10-24 14:37:11'),
(9, 3, 'insina', 'all season', 'banana', 0.00, 'PLANTED', '2025-10-27', '2026-01-27', '2025-10-27 10:01:07'),
(10, 3, 'avocado', 'in child', 'fruits', 10.00, 'PLANTED', '2025-11-21', '2026-02-21', '2025-11-21 07:55:05'),
(11, 3, 'Maize', 'Hybride', 'Ibinyampeke', 0.00, 'GROWING', '2025-11-24', '2026-02-24', '2025-11-24 13:02:11'),
(12, 3, 'IMYUMBATI', 'Ubuhinzi bw\'ibinyabijumba', 'IBINYABIJUMBA', 0.00, 'PLANTED', '2025-12-15', '2026-03-15', '2025-12-15 16:15:57');

-- --------------------------------------------------------

--
-- Stand-in structure for view `cropstatusoverview`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `cropstatusoverview`;
CREATE TABLE IF NOT EXISTS `cropstatusoverview` (
`CropID` int
,`CropName` varchar(100)
,`Category` varchar(50)
,`Status` enum('PLANTED','GROWING','HARVESTED','SOLD')
,`PlantingDate` date
,`ExpectedHarvestDate` date
,`FieldName` varchar(100)
,`FarmerName` varchar(100)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `farmerdashboardsummary`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `farmerdashboardsummary`;
CREATE TABLE IF NOT EXISTS `farmerdashboardsummary` (
`FarmerID` int
,`FullName` varchar(100)
,`Location` varchar(100)
,`TotalFields` bigint
,`TotalArea` decimal(32,2)
,`TotalCrops` bigint
,`GrowingCrops` bigint
,`TotalHarvests` bigint
,`TotalRevenue` decimal(42,4)
);

-- --------------------------------------------------------

--
-- Table structure for table `farmers`
--

DROP TABLE IF EXISTS `farmers`;
CREATE TABLE IF NOT EXISTS `farmers` (
  `FarmerID` int NOT NULL AUTO_INCREMENT,
  `UserID` int DEFAULT NULL,
  `FullName` varchar(100) NOT NULL,
  `Location` varchar(100) DEFAULT NULL,
  `Contact` varchar(20) DEFAULT NULL,
  `Experience` int DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`FarmerID`),
  KEY `UserID` (`UserID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `farmers`
--

INSERT INTO `farmers` (`FarmerID`, `UserID`, `FullName`, `Location`, `Contact`, `Experience`, `CreatedAt`) VALUES
(1, 1, 'Esther ITURIZE', 'Kigali', '+250788123456', 8, '2025-10-24 14:37:11'),
(2, 2, 'Elie TUYIZERE', 'east', '+250790784625', 5, '2025-10-24 14:37:11'),
(3, 4, 'ISHIMWE Robert', 'Eastern Province', '+250788112233', 12, '2025-10-24 14:37:11');

-- --------------------------------------------------------

--
-- Table structure for table `fields`
--

DROP TABLE IF EXISTS `fields`;
CREATE TABLE IF NOT EXISTS `fields` (
  `FieldID` int NOT NULL AUTO_INCREMENT,
  `FarmerID` int DEFAULT NULL,
  `Name` varchar(100) NOT NULL,
  `Address` text,
  `Capacity` decimal(10,2) DEFAULT NULL,
  `Manager` varchar(100) DEFAULT NULL,
  `Contact` varchar(20) DEFAULT NULL,
  `Area` decimal(10,2) DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`FieldID`),
  KEY `FarmerID` (`FarmerID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `fields`
--

INSERT INTO `fields` (`FieldID`, `FarmerID`, `Name`, `Address`, `Capacity`, `Manager`, `Contact`, `Area`, `CreatedAt`) VALUES
(1, 1, 'GreenField', 'Kigali Heights, Kigali City', 1000.00, 'Jane Farmer', '+250788123456', 2.50, '2025-10-24 14:37:11'),
(2, 1, 'Green Acres', 'Gasabo District, Kigali', 1500.00, 'Jane Farmer', '+250788123456', 3.50, '2025-10-24 14:37:11'),
(3, 2, 'Sunny Slope', 'Musanze, Northern Province', 2000.00, 'John Doe', '+250788654321', 5.20, '2025-10-24 14:37:11'),
(4, 3, 'River Side', 'Kayonza, Eastern Province', 1800.00, 'Robert Brown', '+250788112233', 4.80, '2025-10-24 14:37:11'),
(5, 1, 'Mountain View', 'Rubavu, Western Province', 1200.00, 'Jane Farmer', '+250788123456', 3.00, '2025-10-24 14:37:11');

--
-- Triggers `fields`
--
DROP TRIGGER IF EXISTS `BeforeFieldUpdate`;
DELIMITER $$
CREATE TRIGGER `BeforeFieldUpdate` BEFORE UPDATE ON `fields` FOR EACH ROW BEGIN
    IF OLD.Area <> NEW.Area THEN
        INSERT INTO FieldUpdateLog (FieldID, OldArea, NewArea, UpdatedAt)
        VALUES (OLD.FieldID, OLD.Area, NEW.Area, NOW());
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fieldupdatelog`
--

DROP TABLE IF EXISTS `fieldupdatelog`;
CREATE TABLE IF NOT EXISTS `fieldupdatelog` (
  `LogID` int NOT NULL AUTO_INCREMENT,
  `FieldID` int DEFAULT NULL,
  `OldArea` decimal(10,2) DEFAULT NULL,
  `NewArea` decimal(10,2) DEFAULT NULL,
  `UpdatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`LogID`),
  KEY `FieldID` (`FieldID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `harvests`
--

DROP TABLE IF EXISTS `harvests`;
CREATE TABLE IF NOT EXISTS `harvests` (
  `HarvestID` int NOT NULL AUTO_INCREMENT,
  `CropID` int DEFAULT NULL,
  `HarvestDate` date NOT NULL,
  `Quantity` decimal(10,2) DEFAULT NULL,
  `Quality` enum('POOR','FAIR','GOOD','EXCELLENT') DEFAULT NULL,
  `Price` decimal(10,2) DEFAULT NULL,
  `Notes` text,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`HarvestID`),
  KEY `CropID` (`CropID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `harvests`
--

INSERT INTO `harvests` (`HarvestID`, `CropID`, `HarvestDate`, `Quantity`, `Quality`, `Price`, `Notes`, `CreatedAt`) VALUES
(1, 6, '2025-09-10', 1200.50, 'EXCELLENT', 280.00, 'High quality banana harvest', '2025-10-24 14:37:11'),
(2, 1, '2025-08-20', 800.25, 'GOOD', 160.00, 'First maize harvest of season', '2025-10-24 14:37:11'),
(3, 3, '2025-09-15', 450.75, 'EXCELLENT', 320.00, 'Premium tomato quality', '2025-10-24 14:37:11'),
(4, 5, '2025-06-25', 300.00, 'GOOD', 520.00, 'Coffee bean first harvest', '2025-10-24 14:37:11'),
(5, 7, '2025-08-30', 950.00, 'FAIR', 240.00, 'Rice harvest - good yield', '2025-10-24 14:37:11');

--
-- Triggers `harvests`
--
DROP TRIGGER IF EXISTS `AfterHarvestInsert`;
DELIMITER $$
CREATE TRIGGER `AfterHarvestInsert` AFTER INSERT ON `harvests` FOR EACH ROW BEGIN
    UPDATE Crops 
    SET Status = 'HARVESTED' 
    WHERE CropID = NEW.CropID;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `irrigationharvest`
--

DROP TABLE IF EXISTS `irrigationharvest`;
CREATE TABLE IF NOT EXISTS `irrigationharvest` (
  `IrrigationScheduleID` int NOT NULL,
  `HarvestID` int NOT NULL,
  PRIMARY KEY (`IrrigationScheduleID`,`HarvestID`),
  KEY `HarvestID` (`HarvestID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `irrigationharvest`
--

INSERT INTO `irrigationharvest` (`IrrigationScheduleID`, `HarvestID`) VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 4),
(4, 5);

-- --------------------------------------------------------

--
-- Table structure for table `irrigationschedules`
--

DROP TABLE IF EXISTS `irrigationschedules`;
CREATE TABLE IF NOT EXISTS `irrigationschedules` (
  `IrrigationScheduleID` int NOT NULL AUTO_INCREMENT,
  `FieldID` int DEFAULT NULL,
  `ScheduleDate` date NOT NULL,
  `WaterAmount` decimal(10,2) DEFAULT NULL,
  `Duration` int DEFAULT NULL,
  `Status` enum('PENDING','COMPLETED','CANCELLED') DEFAULT 'PENDING',
  `Notes` text,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`IrrigationScheduleID`),
  KEY `FieldID` (`FieldID`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `irrigationschedules`
--

INSERT INTO `irrigationschedules` (`IrrigationScheduleID`, `FieldID`, `ScheduleDate`, `WaterAmount`, `Duration`, `Status`, `Notes`, `CreatedAt`) VALUES
(1, 1, '2025-10-25', 500.00, 120, 'COMPLETED', 'Morning irrigation - optimal conditions', '2025-10-24 14:37:11'),
(2, 1, '2025-10-28', 450.00, 110, 'PENDING', 'Scheduled morning irrigation', '2025-10-24 14:37:11'),
(3, 2, '2025-10-26', 600.00, 150, 'COMPLETED', 'Evening irrigation for tomatoes', '2025-10-24 14:37:11'),
(4, 3, '2025-10-27', 700.00, 180, 'PENDING', 'Coffee field irrigation', '2025-10-24 14:37:11'),
(5, 4, '2025-10-25', 550.00, 130, 'COMPLETED', 'Rice paddy watering', '2025-10-24 14:37:11'),
(6, 5, '2025-10-29', 400.00, 100, 'PENDING', 'Avocado orchard irrigation', '2025-10-24 14:37:11');

-- --------------------------------------------------------

--
-- Stand-in structure for view `irrigationscheduleview`
-- (See below for the actual view)
--
DROP VIEW IF EXISTS `irrigationscheduleview`;
CREATE TABLE IF NOT EXISTS `irrigationscheduleview` (
`IrrigationScheduleID` int
,`ScheduleDate` date
,`WaterAmount` decimal(10,2)
,`Duration` int
,`Status` enum('PENDING','COMPLETED','CANCELLED')
,`Notes` text
,`FieldName` varchar(100)
,`Area` decimal(10,2)
,`FarmerName` varchar(100)
);

-- --------------------------------------------------------

--
-- Table structure for table `markets`
--

DROP TABLE IF EXISTS `markets`;
CREATE TABLE IF NOT EXISTS `markets` (
  `MarketID` int NOT NULL AUTO_INCREMENT,
  `HarvestID` int DEFAULT NULL,
  `MarketName` varchar(100) DEFAULT NULL,
  `Price` decimal(10,2) DEFAULT NULL,
  `QuantitySold` decimal(10,2) DEFAULT NULL,
  `SaleDate` date DEFAULT NULL,
  `BuyerName` varchar(100) DEFAULT NULL,
  `Contact` varchar(20) DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`MarketID`),
  KEY `HarvestID` (`HarvestID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `markets`
--

INSERT INTO `markets` (`MarketID`, `HarvestID`, `MarketName`, `Price`, `QuantitySold`, `SaleDate`, `BuyerName`, `Contact`, `CreatedAt`) VALUES
(1, 1, 'Kigali Central Market', 300.00, 1000.00, '2025-09-12', 'Fresh Foods Ltd', '+250788445566', '2025-10-24 14:37:11'),
(2, 2, 'Local Farmers Market', 170.00, 750.00, '2025-08-25', 'Grain Distributors Co', '+250788778899', '2025-10-24 14:37:11'),
(3, 3, 'Supermarket Chain', 350.00, 400.00, '2025-09-18', 'SuperMart Rwanda', '+250788334455', '2025-10-24 14:37:11'),
(4, 4, 'Export Market', 550.00, 250.00, '2025-07-01', 'Global Coffee Exporters', '+250788667788', '2025-10-24 14:37:11'),
(5, 5, 'Regional Market', 260.00, 800.00, '2025-09-05', 'Rice Traders Association', '+250788990011', '2025-10-24 14:37:11');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(50) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `UserType` enum('FARMER','USER') NOT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `CreatedAt` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `Username` (`Username`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`UserID`, `Username`, `Password`, `UserType`, `Email`, `CreatedAt`) VALUES
(1, 'esther', 'tuyizere', 'FARMER', 'jane.farmer@email.com', '2025-10-24 14:37:11'),
(2, 'elie', 'tuyizere', 'FARMER', 'john.doe@email.com', '2025-10-24 14:37:11'),
(3, 'mary_smith', 'password123', 'USER', 'mary.smith@email.com', '2025-10-24 14:37:11'),
(4, 'robert', 'ely', 'FARMER', 'robert.brown@email.com', '2025-10-24 14:37:11'),
(5, 'admin_user', 'admin123', 'USER', 'admin@agriportal.com', '2025-10-24 14:37:11'),
(6, 'eric', 'tuyizere', 'USER', 'eric@gmail.com', '2025-11-23 14:27:46');

-- --------------------------------------------------------

--
-- Structure for view `cropstatusoverview`
--
DROP TABLE IF EXISTS `cropstatusoverview`;

DROP VIEW IF EXISTS `cropstatusoverview`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `cropstatusoverview`  AS SELECT `c`.`CropID` AS `CropID`, `c`.`Name` AS `CropName`, `c`.`Category` AS `Category`, `c`.`Status` AS `Status`, `c`.`PlantingDate` AS `PlantingDate`, `c`.`ExpectedHarvestDate` AS `ExpectedHarvestDate`, `f`.`Name` AS `FieldName`, `fm`.`FullName` AS `FarmerName` FROM ((`crops` `c` join `fields` `f` on((`c`.`FieldID` = `f`.`FieldID`))) join `farmers` `fm` on((`f`.`FarmerID` = `fm`.`FarmerID`))) ;

-- --------------------------------------------------------

--
-- Structure for view `farmerdashboardsummary`
--
DROP TABLE IF EXISTS `farmerdashboardsummary`;

DROP VIEW IF EXISTS `farmerdashboardsummary`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `farmerdashboardsummary`  AS SELECT `f`.`FarmerID` AS `FarmerID`, `f`.`FullName` AS `FullName`, `f`.`Location` AS `Location`, count(distinct `fl`.`FieldID`) AS `TotalFields`, sum(`fl`.`Area`) AS `TotalArea`, count(distinct `c`.`CropID`) AS `TotalCrops`, count(distinct (case when (`c`.`Status` = 'GROWING') then `c`.`CropID` end)) AS `GrowingCrops`, count(distinct `h`.`HarvestID`) AS `TotalHarvests`, coalesce(sum((`m`.`QuantitySold` * `m`.`Price`)),0) AS `TotalRevenue` FROM ((((`farmers` `f` left join `fields` `fl` on((`f`.`FarmerID` = `fl`.`FarmerID`))) left join `crops` `c` on((`fl`.`FieldID` = `c`.`FieldID`))) left join `harvests` `h` on((`c`.`CropID` = `h`.`CropID`))) left join `markets` `m` on((`h`.`HarvestID` = `m`.`HarvestID`))) GROUP BY `f`.`FarmerID`, `f`.`FullName`, `f`.`Location` ;

-- --------------------------------------------------------

--
-- Structure for view `irrigationscheduleview`
--
DROP TABLE IF EXISTS `irrigationscheduleview`;

DROP VIEW IF EXISTS `irrigationscheduleview`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `irrigationscheduleview`  AS SELECT `i`.`IrrigationScheduleID` AS `IrrigationScheduleID`, `i`.`ScheduleDate` AS `ScheduleDate`, `i`.`WaterAmount` AS `WaterAmount`, `i`.`Duration` AS `Duration`, `i`.`Status` AS `Status`, `i`.`Notes` AS `Notes`, `f`.`Name` AS `FieldName`, `f`.`Area` AS `Area`, `fm`.`FullName` AS `FarmerName` FROM ((`irrigationschedules` `i` join `fields` `f` on((`i`.`FieldID` = `f`.`FieldID`))) join `farmers` `fm` on((`f`.`FarmerID` = `fm`.`FarmerID`))) ;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
