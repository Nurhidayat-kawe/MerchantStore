<?php 
require_once 'koneksi.php'; 

	$id_produk = $_POST['id_produk'];
	
$query = "UPDATE
`produk`
SET
`deleted` = CURDATE() 
WHERE `id_produk` = '$id_produk'";

if(mysqli_query($konek,$query)) {
  $response["kode"] = 1;
  $response["pesan"] = "Hapus berhasil";
  echo json_encode($response);
} else {
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal diproses!";
  echo json_encode($response);
}
?>