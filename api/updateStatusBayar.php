<?php 
require_once 'koneksi.php'; 

	$id = $_POST['id'];
    $status = $_POST['status'];
	
$query = "UPDATE
`transaksi`
SET
`status_bayar` = '$status' 
WHERE `id` = '$id'";

if(mysqli_query($konek,$query)) {
  $response["kode"] = 1;
  $response["pesan"] = "Proses berhasil";
  echo json_encode($response);
} else {
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal diproses!";
  echo json_encode($response);
}
?>