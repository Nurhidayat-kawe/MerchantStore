<?php 
require_once 'koneksi.php'; 

$id_produk = $_POST['id_produk'];
$nama_produk = $_POST['nama_produk'];
$id_kategori = $_POST['id_kategori'];
$harga_beli = $_POST['harga_beli'];
$id_satuan = $_POST['id_satuan'];	
$deskripsi = $_POST['deskripsi'];	
$user = $_POST['user'];
$harga_jual = $_POST['harga_jual'];
$jml_beli = $_POST['jml_beli'];
$harga_diskon = $_POST['harga_diskon'];
$jml_beli2 = $_POST['jml_beli2'];
$harga_diskon2 = $_POST['harga_diskon2'];
$jml_beli3 = $_POST['jml_beli3'];
$harga_diskon3 = $_POST['harga_diskon3'];
$jml_point = $_POST['jml_point'];

mysqli_begin_transaction($konek);

$query = "UPDATE `produk`
SET
  `nama_produk` = '$nama_produk',
  `id_kategori` = '$id_kategori',
  `harga_beli` = '$harga_beli',
  `id_satuan` = '$id_satuan',
  `deskripsi` = '$deskripsi',
  `user` = '$user',
  `jml_point` = '$jml_point',
  `updated` = curdate() 
WHERE `id_produk` = '$id_produk'";

$query2 = "UPDATE `produk_details`
SET
  `harga_jual` = '$harga_jual',
  `jml_beli` = '$jml_beli',
  `harga_disc` = '$harga_diskon',
  `jml_beli2` = '$jml_beli2',
  `harga_disc2` = '$harga_diskon2',
  `jml_beli3` = '$jml_beli3',
  `harga_disc3` = '$harga_diskon3'
WHERE `id_produk` = '$id_produk'";

try {
  mysqli_query($konek, $query);
  mysqli_query($konek, $query2);
  mysqli_commit($konek);
  $response["kode"] = 1;
  $response["pesan"] = "Update Produk Sukses.";
  echo json_encode($response);
} catch (mysqli_sql_exception $exception) {
  mysqli_rollback($konek);
  $response["kode"] = 0;
  $response["pesan"] = "Oops! Gagal: " . $exception->getMessage();
  echo json_encode($response);
}
?>