<?php 
require_once 'koneksi.php'; 

	$status_toko = $_POST['status_toko'];
	
$query = "UPDATE
`settings`
SET
`status_toko` = '$status_toko' where id=0";

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