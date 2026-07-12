<?php 
require_once 'koneksi.php'; 

    $id_transaksi = $_POST['id_transaksi'];
    $id_keranjang = $_POST['id_keranjang'];
	  $id_user = $_POST['id_user'];
    $produk = $_POST['produk'];
    $jumlah = $_POST['jumlah'];
    $harga = $_POST['harga'];	
    $diskon = $_POST['diskon'];	
    
// Turn autocommit off
mysqli_begin_transaction($konek);

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
$query_update_stok = "UPDATE `produk_details` SET
  `stok` = stok-'$jumlah'
  WHERE `id_produk` = '$produk'";

  $query_del_keranjang = "DELETE
  FROM
    `keranjang`
  WHERE `id_keranjang` = '$id_keranjang'";
try{
mysqli_query($konek,$query2);
mysqli_query($konek,$query_update_stok);
mysqli_query($konek,$query_del_keranjang);
mysqli_commit($konek);
$response["kode"] = 1;
  $response["pesan"] = "Tambah Details Transaksi Sukses.";
  echo json_encode($response);
} catch (mysqli_sql_exception $exception) {
  mysqli_rollback($konek);
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal!";
  echo json_encode($response);
  throw $exception;
}

?>