<?php 
require_once 'koneksi.php'; 

	$id_user = $_POST['id_user'];
  $nama = $_POST['nama'];
  $alamat = $_POST['alamat'];
  $telp = $_POST['telp'];
  $toko = $_POST['toko'];
	
$query = "UPDATE
`users`
SET
`nama` = '$nama',
alamat = '$alamat',
telp = '$telp',
toko = '$toko' 
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