-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 21, 2024 at 03:43 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `vehicle_rental`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `calculate_rental_days` (IN `book_id` INT(5), OUT `b_date` DATE, OUT `r_date` DATE)   select booking_date,return_date into b_date,r_date from booking where b_id=book_id$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `bills`
--

CREATE TABLE `bills` (
  `bill_id` bigint(20) UNSIGNED NOT NULL,
  `v_id` int(11) DEFAULT NULL,
  `c_id` int(11) DEFAULT NULL,
  `amount` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bills`
--

INSERT INTO `bills` (`bill_id`, `v_id`, `c_id`, `amount`) VALUES
(3, 1, 1, 3500),
(4, 6, 2, 1350),
(5, 2, 3, 900),
(6, 2, 4, 2700),
(7, 2, 3, -10500),
(8, 2, 3, -10500),
(9, 3, 3, 6000),
(10, 20, 4, 3100);

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `b_id` int(5) NOT NULL,
  `v_id` int(5) NOT NULL,
  `c_id` int(5) NOT NULL,
  `booking_date` date NOT NULL,
  `return_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`b_id`, `v_id`, `c_id`, `booking_date`, `return_date`) VALUES
(1, 1, 1, '2024-08-10', '2024-08-17'),
(2, 6, 2, '2024-08-18', '2024-08-21'),
(3, 2, 3, '2024-09-25', '2024-08-21'),
(4, 2, 4, '2024-08-01', '2024-08-10'),
(5, 3, 3, '2024-08-15', '2024-08-21'),
(6, 20, 4, '2024-10-01', '2024-11-01');

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `c_id` int(5) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(30) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `city` varchar(20) NOT NULL,
  `password` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`c_id`, `name`, `email`, `phone`, `city`, `password`) VALUES
(1, 'carry_minatti', 'carry07@gmail.com', '9537100239', 'mumbai', 'carry'),
(2, 'prince malaviya', 'prince123@gmail.com', '6356230835', 'prempara', 'pm_12'),
(3, 'arya', 'arya@gmail.com', '1234567891', 'ahmedabad', '5306'),
(4, 'vynzo', 'xyz@gmail.com', '1234567894', 'abc', 'xyz123');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle`
--

CREATE TABLE `vehicle` (
  `v_id` int(5) NOT NULL,
  `type` varchar(20) NOT NULL,
  `model` varchar(50) NOT NULL,
  `year` int(4) NOT NULL,
  `price` int(5) NOT NULL,
  `isBooked` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicle`
--

INSERT INTO `vehicle` (`v_id`, `type`, `model`, `year`, `price`, `isBooked`) VALUES
(1, 'car', 'bugati chiron', 2020, 500, 0),
(2, 'car', 'mercedes G-wegon', 2019, 1200, 0),
(3, 'car', 'Rolls-Royce Phantom', 2024, 1000, 0),
(4, 'activa', '2G', 2009, 100, 0),
(6, 'motorcycle', 'Royal Enfield Shotgun 650', 2018, 450, 0),
(7, 'activa', 'Honda Avtiva 125', 2019, 350, 0),
(8, 'car', 'Toyota Corolla', 2022, 60, 0),
(9, 'car', 'Honda Accord', 2021, 65, 0),
(10, 'activa', 'Honda Activa 6G', 2023, 15, 0),
(11, 'motorcycle', 'Yamaha YZF-R15', 2020, 30, 0),
(12, 'car', 'BMW 3 Series', 2022, 85, 0),
(13, 'motorcycle', 'Kawasaki Ninja 300', 2021, 40, 0),
(14, 'activa', 'TVS Jupiter', 2022, 18, 0),
(15, 'car', 'Hyundai Elantra', 2021, 55, 0),
(16, 'motorcycle', 'Royal Enfield Classic 350', 2020, 35, 0),
(17, 'activa', 'Suzuki Access 125', 2022, 20, 0),
(18, 'car', 'Audi A4', 2022, 90, 0),
(19, 'motorcycle', 'Bajaj Pulsar 150', 2021, 25, 0),
(20, 'car', 'Mercedes-Benz C-Class', 2021, 100, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bills`
--
ALTER TABLE `bills`
  ADD UNIQUE KEY `bill_id` (`bill_id`);

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`b_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bills`
--
ALTER TABLE `bills`
  MODIFY `bill_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `b_id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
