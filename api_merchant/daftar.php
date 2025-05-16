<?php 
require_once 'koneksi.php'; 

	  $nama = $_POST['nama'];
    $alamat = $_POST['alamat'];
    $telp = $_POST['telp'];
    $toko = $_POST['toko'];	
    $username = $_POST['username'];	
    $password = $_POST['password'];	
    
$query = "INSERT INTO `users` (
  `nama`,
  `alamat`,
  `telp`,
  `tgl_daftar`,
  `role`,
  `toko`,
  `username`,
  `password`,
  `foto_user`,
  `cabang`,
  `stat_user`
)
VALUES
  (
    '$nama',
    '$alamat',
    '$telp',
    CURDATE(),
    'user',
    '$toko',
    '$username',
    '$password',
    '',
    '0',
    'menunggu'
  )
";	
try{
mysqli_query($konek,$query);
$response["kode"] = 1;
  $response["pesan"] = "Tambah User Sukses.";
  echo json_encode($response);
} catch (mysqli_sql_exception $exception) {
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal!";
  echo json_encode($response);
  throw $exception;
}
