<?php
require_once 'koneksi.php';

$id = $_POST['id'];
$id_user = $_POST['id_user'];

$query = "DELETE
FROM
  `keranjang`
WHERE `id_keranjang` = '$id'";

if(mysqli_query($konek,$query)) {
  $response["kode"] = 1;
  $response["pesan"] = "Hapus Berhasil";
  echo json_encode($response);
} else {
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal Hapus!";
  echo json_encode($response);
}
