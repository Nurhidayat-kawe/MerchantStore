<?php 
require_once 'koneksi.php'; 

	$id_user = $_POST['id_user'];
    $stat_user = $_POST['stat_user'];
	
$query = "UPDATE
`users`
SET
`stat_user` = '$stat_user' 
WHERE `id_user` = '$id_user'";

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