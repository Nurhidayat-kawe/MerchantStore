<?php 
require_once 'koneksi.php'; 

	$id_user = $_POST['id_user'];
	$jml_bonus_point = $_POST['jml_bonus_point'];

  $query = "UPDATE `users` SET
  `point` = point+'$jml_bonus_point'
  WHERE `id_user` = '$id_user'";

if(mysqli_query($konek,$query)) {
  $response["kode"] = 1;
  $response["pesan"] = "Update Point Berhasil";
  echo json_encode($response);
} else {
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal Simpan!";
  echo json_encode($response);
}
?>