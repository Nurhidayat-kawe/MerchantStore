<?php
require_once 'koneksi.php';

$id_user = $_POST['id_user'];
$id_produk = $_POST['id_produk'];
$jml = $_POST['jml'];
$cek="";
$cek = "SELECT id_keranjang FROM keranjang where id_user='$id_user' and id_produk='$id_produk'";
$result = mysqli_query($konek, $cek);

if ($result->num_rows == 0) {
  $query = "INSERT INTO `keranjang` (`id_user`, `id_produk`, `jml`)
VALUES
  ('$id_user', '$id_produk', '$jml')

";

  if (mysqli_query($konek, $query)) {
    $response["kode"] = 1;
    $response["pesan"] = "Proses berhasil";
    echo json_encode($response);
  } else {
    $response["kode"] = 0;
    $response["pesan"] = "oops! gagal diproses!";
    echo json_encode($response);
  }
} else {
  $query = "UPDATE
  `keranjang`
SET
  `jml` = '$jml' + jml 
WHERE id_user='$id_user' and id_produk='$id_produk'";

  if (mysqli_query($konek, $query)) {
    $response["kode"] = 1;
    $response["pesan"] = "Proses update berhasil";
    echo json_encode($response);
  } else {
    $response["kode"] = 0;
    $response["pesan"] = "oops! gagal diproses!";
    echo json_encode($response);
  }
}
