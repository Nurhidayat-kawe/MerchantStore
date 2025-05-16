/*
SQLyog Professional v12.4.3 (64 bit)
MySQL - 10.4.25-MariaDB : Database - merchant
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `cabang` */

DROP TABLE IF EXISTS `cabang`;

CREATE TABLE `cabang` (
  `id_cabang` varchar(10) NOT NULL,
  `nama_cabang` varchar(50) NOT NULL,
  `alamat_cabang` text NOT NULL,
  `telp_cabang` varchar(20) NOT NULL,
  PRIMARY KEY (`id_cabang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `cabang` */

insert  into `cabang`(`id_cabang`,`nama_cabang`,`alamat_cabang`,`telp_cabang`) values 
('0','Ahmad Cell','Kalisoka, Adiwerna Kab. Tegal','0856666');

/*Table structure for table `kategori` */

DROP TABLE IF EXISTS `kategori`;

CREATE TABLE `kategori` (
  `id_kategori` int(11) NOT NULL AUTO_INCREMENT,
  `nama_kategori` varchar(30) NOT NULL,
  PRIMARY KEY (`id_kategori`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `kategori` */

insert  into `kategori`(`id_kategori`,`nama_kategori`) values 
(1,'Voucher');

/*Table structure for table `keranjang` */

DROP TABLE IF EXISTS `keranjang`;

CREATE TABLE `keranjang` (
  `id_keranjang` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `id_produk` varchar(30) NOT NULL,
  `jml` int(11) NOT NULL,
  PRIMARY KEY (`id_keranjang`),
  KEY `id_user` (`id_user`),
  CONSTRAINT `keranjang_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4;

/*Data for the table `keranjang` */

insert  into `keranjang`(`id_keranjang`,`id_user`,`id_produk`,`jml`) values 
(34,3,'20230602120949',16),
(35,3,'20230602120948',14);

/*Table structure for table `produk` */

DROP TABLE IF EXISTS `produk`;

CREATE TABLE `produk` (
  `id_produk` varchar(30) NOT NULL,
  `nama_produk` varchar(50) NOT NULL,
  `id_kategori` varchar(11) NOT NULL,
  `harga_beli` decimal(12,0) NOT NULL,
  `id_satuan` varchar(11) NOT NULL,
  `deskripsi` text NOT NULL,
  `foto` text NOT NULL,
  `foto2` text NOT NULL,
  `user` int(11) NOT NULL,
  `created` date NOT NULL,
  `updated` date NOT NULL,
  `deleted` date NOT NULL,
  `barcode` varchar(100) NOT NULL,
  PRIMARY KEY (`id_produk`),
  KEY `kategori` (`id_kategori`),
  KEY `satuan` (`id_satuan`),
  KEY `user` (`user`),
  CONSTRAINT `produk_ibfk_3` FOREIGN KEY (`user`) REFERENCES `users` (`id_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `produk` */

insert  into `produk`(`id_produk`,`nama_produk`,`id_kategori`,`harga_beli`,`id_satuan`,`deskripsi`,`foto`,`foto2`,`user`,`created`,`updated`,`deleted`,`barcode`) values 
('20230602120948','Voucher Telkomsel 10Gb Unli','1',2828,'1',' ycycuvuvvuv vyvyv vyvyvy uvuvuv yv','IMG20230413064758.jpg','0',1,'2023-06-02','0000-00-00','0000-00-00',''),
('20230602120949','Voucher 3 20Gb Unlimited ','1 ',15000,'2 ','Voucher Unlimited Telkomsel\r\ndengan kapasitas 20Gb dan kecepatan sampai 128Mbps dapat membuat hidup anda lebih berwarna.\r\nbersama Telkomsel jaringan luas senusantara.','','0',1,'2023-06-08','2023-06-29','0000-00-00',''),
('20230628060958','Voucher 3 10gb','1',50000,'2','voucher 3 unlimited','0','0',1,'2023-06-28','0000-00-00','0000-00-00','');

/*Table structure for table `produk_details` */

DROP TABLE IF EXISTS `produk_details`;

CREATE TABLE `produk_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_produk` varchar(30) NOT NULL,
  `cabang` int(11) NOT NULL,
  `stok` int(11) NOT NULL DEFAULT 0,
  `jml_beli` int(11) NOT NULL DEFAULT 0,
  `harga_disc` decimal(12,0) NOT NULL DEFAULT 0,
  `harga_jual` decimal(12,0) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `id_produk` (`id_produk`),
  CONSTRAINT `produk_details_ibfk_1` FOREIGN KEY (`id_produk`) REFERENCES `produk` (`id_produk`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Data for the table `produk_details` */

insert  into `produk_details`(`id`,`id_produk`,`cabang`,`stok`,`jml_beli`,`harga_disc`,`harga_jual`) values 
(8,'20230602120948',0,1,88,82,28282),
(9,'20230602120949',0,338,10,12000,20000),
(10,'20230628060958',0,0,5,65000,68000);

/*Table structure for table `satuan` */

DROP TABLE IF EXISTS `satuan`;

CREATE TABLE `satuan` (
  `id_satuan` int(11) NOT NULL AUTO_INCREMENT,
  `nama_satuan` varchar(30) NOT NULL,
  PRIMARY KEY (`id_satuan`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `satuan` */

insert  into `satuan`(`id_satuan`,`nama_satuan`) values 
(1,'Pcs'),
(2,'Box');

/*Table structure for table `transaksi` */

DROP TABLE IF EXISTS `transaksi`;

CREATE TABLE `transaksi` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_transaksi` varchar(30) NOT NULL,
  `id_user` varchar(50) NOT NULL,
  `tanggal` date NOT NULL,
  `status` enum('baru','proses','siap','selesai','batal') NOT NULL COMMENT 'baru,proses,siap,selesai,batal',
  `ongkir` decimal(12,2) NOT NULL,
  `status_bayar` enum('belum','sudah') NOT NULL,
  `jam` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_transaksi` (`id_transaksi`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;

/*Data for the table `transaksi` */

insert  into `transaksi`(`id`,`id_transaksi`,`id_user`,`tanggal`,`status`,`ongkir`,`status_bayar`,`jam`) values 
(2,'2','2','2023-06-24','proses',0.00,'sudah','00:00:00'),
(3,'3','2','2023-06-23','selesai',0.00,'sudah','00:00:00'),
(4,'4','2','2023-06-24','selesai',0.00,'sudah','00:00:00'),
(5,'5','3','2023-06-08','selesai',0.00,'sudah','00:00:00'),
(7,'TP2023071501101123','3','2023-07-15','selesai',0.00,'sudah','01:10:14'),
(8,'TP2023071501115024','3','2023-07-15','selesai',0.00,'sudah','01:11:52'),
(9,'TP2023071501234214','3','2023-07-15','selesai',0.00,'belum','01:23:45'),
(10,'TP2023071501250733','3','2023-07-15','batal',0.00,'belum','01:25:12'),
(11,'TP2023071501260095','3','2023-07-15','selesai',0.00,'belum','01:26:03'),
(12,'TP2023071501264428','3','2023-07-15','baru',0.00,'belum','01:26:47'),
(13,'TP2023071823253970','3','2023-07-18','baru',0.00,'belum','23:25:43'),
(14,'TP2023071905321689','4','2023-07-19','batal',0.00,'belum','05:32:19'),
(15,'TP2023071912042161','4','2023-07-19','batal',0.00,'belum','12:04:30'),
(16,'TP2023071912134415','4','2023-07-19','batal',0.00,'belum','12:13:47'),
(17,'TP2023071912134415','4','2023-07-19','batal',0.00,'belum','12:13:47'),
(18,'TP2023071912253062','4','2023-07-19','baru',0.00,'belum','12:25:32'),
(19,'TP2023071912253062','4','2023-07-19','baru',0.00,'belum','12:25:32');

/*Table structure for table `transaksi_details` */

DROP TABLE IF EXISTS `transaksi_details`;

CREATE TABLE `transaksi_details` (
  `id_details` int(11) NOT NULL AUTO_INCREMENT,
  `id_transaksi` varchar(30) NOT NULL,
  `produk` varchar(30) NOT NULL,
  `jumlah` decimal(12,0) NOT NULL,
  `harga` decimal(12,2) NOT NULL,
  `diskon` decimal(12,2) DEFAULT NULL,
  PRIMARY KEY (`id_details`),
  KEY `transaksi_details_ibfk_1` (`id_transaksi`),
  KEY `produk` (`produk`),
  CONSTRAINT `transaksi_details_ibfk_1` FOREIGN KEY (`id_transaksi`) REFERENCES `transaksi` (`id_transaksi`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `transaksi_details_ibfk_2` FOREIGN KEY (`produk`) REFERENCES `produk` (`id_produk`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;

/*Data for the table `transaksi_details` */

insert  into `transaksi_details`(`id_details`,`id_transaksi`,`produk`,`jumlah`,`harga`,`diskon`) values 
(2,'2','20230602120948',2,28282.00,0.00),
(3,'2','20230602120949',5,15000.00,5000.00),
(4,'4','20230602120948',10,28282.00,10000.00),
(5,'5','20230602120948',5,28282.00,0.00),
(7,'TP2023071501101123','20230628060958',2,68000.00,0.00),
(8,'TP2023071501115024','20230602120948',1,28282.00,0.00),
(9,'TP2023071501234214','20230602120948',2,28282.00,0.00),
(10,'TP2023071501250733','20230602120949',10,12000.00,8000.00),
(11,'TP2023071501260095','20230602120949',5,20000.00,0.00),
(12,'TP2023071501264428','20230628060958',10,65000.00,3000.00),
(13,'TP2023071823253970','20230602120948',2,28282.00,0.00),
(14,'TP2023071905321689','20230628060958',1,68000.00,0.00),
(15,'TP2023071912042161','20230602120949',1,20000.00,0.00),
(16,'TP2023071912134415','20230602120948',2,28282.00,0.00),
(17,'TP2023071912134415','20230602120949',1,20000.00,0.00),
(18,'TP2023071912253062','20230602120949',5,20000.00,0.00),
(19,'TP2023071912253062','20230602120948',5,28282.00,0.00);

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(80) NOT NULL,
  `alamat` text NOT NULL,
  `telp` varchar(20) NOT NULL,
  `tgl_daftar` date NOT NULL,
  `role` varchar(15) NOT NULL DEFAULT 'user',
  `toko` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `foto_user` varchar(100) NOT NULL,
  `cabang` int(11) NOT NULL DEFAULT 0,
  `stat_user` varchar(15) DEFAULT 'menunggu' COMMENT 'aktif, menunggu, blokir',
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

/*Data for the table `users` */

insert  into `users`(`id_user`,`nama`,`alamat`,`telp`,`tgl_daftar`,`role`,`toko`,`username`,`password`,`foto_user`,`cabang`,`stat_user`) values 
(1,'Admin','Tegal','','2023-05-17','admin','Sumber Angin','admin','admin','IMG20220508102149.jpg',0,'aktif'),
(2,'User','Adiwerna','0','2023-05-28','user','Toko Ujicoba','user','user123','',0,'aktif'),
(3,'Nuril Hidayat','Slawi','0866666','2023-06-08','user','Cahaya Cell','user','user2','',0,'aktif'),
(4,'Ardan Nuril Hidayat','Adiwerna','085742545855','2023-07-19','user','Berkah Celluler','berkah','berkah','4.jpg',0,'aktif');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
