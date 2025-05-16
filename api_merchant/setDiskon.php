<?php 
require_once 'koneksi.php'; 

	$id_produk = $_POST['id_produk'];
	$jml_beli = $_POST['jml_beli'];
  $harga_diskon = $_POST['harga_diskon'];

$query = "UPDATE
`produk_details`
SET
`jml_beli` = '$jml_beli',
`harga_disc` = '$harga_diskon' 
WHERE `id_produk` = '$id_produk'";

try{
  mysqli_query($konek,$query);
  $response["kode"] = 1;
    $response["pesan"] = "Set Diskon Sukses.";
    echo json_encode($response);
  } catch (mysqli_sql_exception $exception) {
    mysqli_rollback($konek);
    $response["kode"] = 0;
    $response["pesan"] = "oops! gagal!";
    echo json_encode($response);
    throw $exception;
  }
?>