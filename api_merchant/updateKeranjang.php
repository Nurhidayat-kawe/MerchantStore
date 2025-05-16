<?php 
require_once 'koneksi.php'; 

	$id = $_POST['id'];
	$jml = $_POST['jml'];
	$id_user = $_POST['id_user'];
	
$query = "UPDATE
`keranjang`
SET
`jml` = '$jml' 
WHERE `id_keranjang` = '$id'";

if(mysqli_query($konek,$query)) {
  $response["kode"] = 1;
  $response["pesan"] = "Update Berhasil";
  echo json_encode($response);
} else {
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal Update!";
  echo json_encode($response);
}
?>