<?php 
require_once 'koneksi.php'; 

$id_produk = $_POST['id_produk'];
$nama_produk = $_POST['nama_produk'];
$id_kategori = $_POST['id_kategori'];
$harga_beli = $_POST['harga_beli'];
$id_satuan = $_POST['id_satuan'];	
$deskripsi = $_POST['deskripsi'];	
$foto = $_POST['foto'];	
$foto2 = $_POST['foto2'];
$user = $_POST['user'];
$cabang = $_POST['cabang'];
$stok = $_POST['stok'];
$jml_beli = $_POST['jml_beli'];
$jml_beli2 = $_POST['jml_beli2'];
$jml_beli3 = $_POST['jml_beli3'];
$harga_disc = $_POST['harga_disc'];
$harga_disc2 = $_POST['harga_disc2'];
$harga_disc3 = $_POST['harga_disc3'];
$harga_jual = $_POST['harga_jual'];
$jml_point = $_POST['jml_point'];

mysqli_begin_transaction($konek);

$query = "INSERT INTO `produk` (
  `id_produk`,
  `nama_produk`,
  `id_kategori`,
  `harga_beli`,
  `id_satuan`,
  `deskripsi`,
  `foto`,
  `foto2`,
  `user`,
  `created`,
  `updated`,
  `deleted`,
  `barcode`,
  `jml_point`
)
VALUES (
  '$id_produk',
  '$nama_produk',
  '$id_kategori',
  '$harga_beli',
  '$id_satuan',
  '$deskripsi',
  '$foto',
  '$foto2',
  '$user',
  curdate(),
  '0000-00-00',
  '0000-00-00',
  '',
  '$jml_point'
)";

$query2 = "INSERT INTO `produk_details` (
  `id_produk`,
  `stok`,
  `jml_beli`,
  `harga_disc`,
  `jml_beli2`,
  `harga_disc2`,
  `jml_beli3`,
  `harga_disc3`,
  `harga_jual`,
  `cabang`
)
VALUES (
  '$id_produk',
  '$stok',
  '$jml_beli',
  '$harga_disc',
  '$jml_beli2',
  '$harga_disc2',
  '$jml_beli3',
  '$harga_disc3',
  '$harga_jual',
  '$cabang'
)";

try {
  mysqli_query($konek, $query);
  mysqli_query($konek, $query2);
  mysqli_commit($konek);
  $response["kode"] = 1;
  $response["pesan"] = "Tambah Produk Sukses.";
  echo json_encode($response);
} catch (mysqli_sql_exception $exception) {
  mysqli_rollback($konek);
  $response["kode"] = 0;
  $response["pesan"] = "Oops! Gagal: " . $exception->getMessage();
  echo json_encode($response);
}
?>