-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 04, 2025 at 03:23 AM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `laboratorium`
--
CREATE DATABASE IF NOT EXISTS `laboratorium` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `laboratorium`;

-- --------------------------------------------------------

--
-- Table structure for table `dokter`
--

DROP TABLE IF EXISTS `dokter`;
CREATE TABLE `dokter` (
  `Id_Dokter` int(11) NOT NULL,
  `Nama` varchar(25) NOT NULL,
  `Kontak` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dokter`
--

INSERT INTO `dokter` (`Id_Dokter`, `Nama`, `Kontak`) VALUES
(2392, 'Rafli', '12345678');

-- --------------------------------------------------------

--
-- Table structure for table `hasil_periksa`
--

DROP TABLE IF EXISTS `hasil_periksa`;
CREATE TABLE `hasil_periksa` (
  `id_pemeriksaan` varchar(255) NOT NULL,
  `no_rm` int(11) NOT NULL,
  `Tanggal` date NOT NULL,
  `nama_pasien` int(25) NOT NULL,
  `nama_dokter` int(25) NOT NULL,
  `jenis_layanan` varchar(555) NOT NULL,
  `Hasil` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `layanan`
--

DROP TABLE IF EXISTS `layanan`;
CREATE TABLE `layanan` (
  `id_layanan` int(11) NOT NULL,
  `nama_layanan` varchar(100) NOT NULL,
  `harga` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `layanan`
--

INSERT INTO `layanan` (`id_layanan`, `nama_layanan`, `harga`) VALUES
(1, 'Urine', '50000.00'),
(2, 'Hematologi', '50000.00'),
(3, 'Serologi', '50000.00'),
(4, 'Kimia 1', '50000.00'),
(5, 'Kimia 2', '50000.00'),
(6, 'Tinja', '50000.00');

-- --------------------------------------------------------

--
-- Table structure for table `pasien`
--

DROP TABLE IF EXISTS `pasien`;
CREATE TABLE `pasien` (
  `No_rm` int(11) NOT NULL,
  `Nama` varchar(25) NOT NULL,
  `Alamat` varchar(255) NOT NULL,
  `jk` enum('L','P') NOT NULL,
  `no_telp` varchar(15) NOT NULL,
  `Tgl_Lahir` date NOT NULL,
  `Tinggi` int(3) NOT NULL,
  `Berat` int(3) NOT NULL,
  `goldar` enum('A','B','AB','O') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pasien`
--

INSERT INTO `pasien` (`No_rm`, `Nama`, `Alamat`, `jk`, `no_telp`, `Tgl_Lahir`, `Tinggi`, `Berat`, `goldar`) VALUES
(1, 'Rafli', 'Jl.Nangka', 'L', '0897', '2003-09-30', 175, 90, 'A'),
(2, 'dina', 'tes\n', 'P', '0123312', '2004-01-26', 160, 50, 'B'),
(3, 'rafli', 'tes	', 'L', '111', '2003-09-30', 175, 180, 'AB'),
(1001, 'Aria', 'jl.gedong', 'L', '08123066', '2024-11-06', 170, 60, 'B'),
(1002, 'TAMA', 'JL.JAlan', 'L', '08123456', '2003-09-03', 175, 80, 'A'),
(1003, 'tes', 'tes	', 'L', 'tees', '2003-09-30', 170, 90, 'A');

-- --------------------------------------------------------

--
-- Table structure for table `pembayaran`
--

DROP TABLE IF EXISTS `pembayaran`;
CREATE TABLE `pembayaran` (
  `id_pembayaran` varchar(50) NOT NULL,
  `id_pendaftaran` int(11) DEFAULT NULL,
  `tanggal_bayar` date DEFAULT NULL,
  `total_bayar` double DEFAULT NULL,
  `metode_pembayaran` varchar(50) DEFAULT NULL,
  `dibayar` double DEFAULT NULL,
  `kembali` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pembayaran`
--

INSERT INTO `pembayaran` (`id_pembayaran`, `id_pendaftaran`, `tanggal_bayar`, `total_bayar`, `metode_pembayaran`, `dibayar`, `kembali`) VALUES
('PB20250601001', 2, '2025-06-01', 100000, NULL, NULL, NULL),
('PB20250601002', 6, '2025-06-01', 100000, NULL, NULL, NULL),
('PB20250601003', 2, '2025-06-01', 100000, NULL, NULL, NULL),
('PB20250601004', 3, '2025-06-01', 100000, NULL, NULL, NULL),
('PB20250601005', 8, '2025-06-01', 100000, NULL, NULL, NULL),
('PB20250601006', 4, '2025-06-01', 100000, NULL, NULL, NULL),
('PB20250604001', 4, '2025-06-04', 100000, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `pendaftaran`
--

DROP TABLE IF EXISTS `pendaftaran`;
CREATE TABLE `pendaftaran` (
  `id_pendaftaran` int(11) NOT NULL,
  `No_rm` int(11) NOT NULL,
  `Id_dokter` int(11) NOT NULL,
  `tanggal_daftar` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pendaftaran`
--

INSERT INTO `pendaftaran` (`id_pendaftaran`, `No_rm`, `Id_dokter`, `tanggal_daftar`) VALUES
(2, 1, 2392, '2025-05-06'),
(3, 2, 2392, '2025-05-06'),
(4, 1001, 2392, '2025-05-06'),
(5, 1002, 2392, '2025-05-06'),
(6, 1, 2392, '2025-05-06'),
(7, 1002, 2392, '2025-05-06'),
(8, 1002, 2392, '2025-05-07');

-- --------------------------------------------------------

--
-- Table structure for table `pendaftaran_detail`
--

DROP TABLE IF EXISTS `pendaftaran_detail`;
CREATE TABLE `pendaftaran_detail` (
  `id_detail` int(11) NOT NULL,
  `id_pendaftaran` int(11) NOT NULL,
  `id_layanan` int(11) NOT NULL,
  `harga` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pendaftaran_detail`
--

INSERT INTO `pendaftaran_detail` (`id_detail`, `id_pendaftaran`, `id_layanan`, `harga`) VALUES
(1, 2, 2, '50000.00'),
(2, 2, 1, '50000.00'),
(3, 3, 2, '50000.00'),
(4, 3, 1, '50000.00'),
(5, 4, 2, '50000.00'),
(6, 4, 1, '50000.00'),
(7, 5, 1, '50000.00'),
(8, 5, 2, '50000.00'),
(9, 6, 1, '50000.00'),
(10, 6, 3, '50000.00'),
(11, 7, 2, '50000.00'),
(12, 7, 4, '50000.00'),
(13, 8, 2, '50000.00'),
(14, 8, 1, '50000.00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dokter`
--
ALTER TABLE `dokter`
  ADD PRIMARY KEY (`Id_Dokter`);

--
-- Indexes for table `hasil_periksa`
--
ALTER TABLE `hasil_periksa`
  ADD PRIMARY KEY (`id_pemeriksaan`);

--
-- Indexes for table `layanan`
--
ALTER TABLE `layanan`
  ADD PRIMARY KEY (`id_layanan`);

--
-- Indexes for table `pasien`
--
ALTER TABLE `pasien`
  ADD PRIMARY KEY (`No_rm`);

--
-- Indexes for table `pembayaran`
--
ALTER TABLE `pembayaran`
  ADD PRIMARY KEY (`id_pembayaran`);

--
-- Indexes for table `pendaftaran`
--
ALTER TABLE `pendaftaran`
  ADD PRIMARY KEY (`id_pendaftaran`),
  ADD KEY `No_rm` (`No_rm`),
  ADD KEY `Id_dokter` (`Id_dokter`);

--
-- Indexes for table `pendaftaran_detail`
--
ALTER TABLE `pendaftaran_detail`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_pendaftaran` (`id_pendaftaran`),
  ADD KEY `id_layanan` (`id_layanan`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `layanan`
--
ALTER TABLE `layanan`
  MODIFY `id_layanan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `pasien`
--
ALTER TABLE `pasien`
  MODIFY `No_rm` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1004;

--
-- AUTO_INCREMENT for table `pendaftaran`
--
ALTER TABLE `pendaftaran`
  MODIFY `id_pendaftaran` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `pendaftaran_detail`
--
ALTER TABLE `pendaftaran_detail`
  MODIFY `id_detail` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `pendaftaran`
--
ALTER TABLE `pendaftaran`
  ADD CONSTRAINT `pendaftaran_ibfk_1` FOREIGN KEY (`No_rm`) REFERENCES `pasien` (`No_rm`),
  ADD CONSTRAINT `pendaftaran_ibfk_2` FOREIGN KEY (`Id_dokter`) REFERENCES `dokter` (`Id_Dokter`);

--
-- Constraints for table `pendaftaran_detail`
--
ALTER TABLE `pendaftaran_detail`
  ADD CONSTRAINT `pendaftaran_detail_ibfk_1` FOREIGN KEY (`id_pendaftaran`) REFERENCES `pendaftaran` (`id_pendaftaran`),
  ADD CONSTRAINT `pendaftaran_detail_ibfk_2` FOREIGN KEY (`id_layanan`) REFERENCES `layanan` (`id_layanan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
