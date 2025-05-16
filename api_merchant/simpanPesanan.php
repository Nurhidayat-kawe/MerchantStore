<?php 
require_once 'koneksi.php'; 

	  $id_transaksi = $_POST['id_transaksi'];
	  $id_user = $_POST['id_user'];
    $produk = $_POST['produk'];
    $jumlah = $_POST['jumlah'];
    $harga = $_POST['harga'];	
    $diskon = $_POST['diskon'];	
    
// Turn autocommit off
mysqli_begin_transaction($konek);

$query = "INSERT INTO `transaksi` (
  `id_transaksi`,
  `id_user`,
  `tanggal`,
  `status`,
  `ongkir`,
  `status_bayar`,
  `jam`
)
VALUES
  (
    '$id_transaksi',
    '$id_user',
    CURDATE(),
    'baru',
    '0',
    'belum',
    CURTIME()
  )
";	
$query2 = "INSERT INTO `transaksi_details` (
  `id_transaksi`,
  `produk`,
  `jumlah`,
  `harga`,
  `diskon`
)
VALUES
  (
    '$id_transaksi',
    '$produk',
    '$jumlah',
    '$harga',
    '$diskon'
  )
";
try{
mysqli_query($konek,$query);
mysqli_query($konek,$query2);
mysqli_commit($konek);
$response["kode"] = 1;
  $response["pesan"] = "Tambah Transaksi Sukses.";
  echo json_encode($response);
} catch (mysqli_sql_exception $exception) {
  mysqli_rollback($konek);
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal!";
  echo json_encode($response);
  throw $exception;
}

?>