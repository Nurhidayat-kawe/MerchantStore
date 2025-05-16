<?php 
require_once 'koneksi.php'; 

	$id_produk = $_POST['id_produk'];
	$stok_baru = $_POST['stok_baru'];

  $query = "UPDATE `produk_details` SET
  `stok` = '$stok_baru'
  WHERE `id_produk` = '$id_produk'";

if(mysqli_query($konek,$query)) {
  $response["kode"] = 1;
  $response["pesan"] = "Simpan Stok Berhasil";
  echo json_encode($response);
} else {
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal Simpan!";
  echo json_encode($response);
}
?>