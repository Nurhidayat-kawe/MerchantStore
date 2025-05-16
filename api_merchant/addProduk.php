<?php 
require_once 'koneksi.php'; 

	$id_produk = $_POST['id_produk'];
	$nama_produk = $_POST['nama_produk'];
    $id_kategori = $_POST['id_kategori'];
    $harga_beli = $_POST['harga_beli'];
    $id_satuan = $_POST['id_satuan'];	
    $deskripsi = $_POST['deskripsi'];	
    $foto = $_POST['foto'];	
    $foto2 = $_POST['foto2'];
    $user = $_POST['user'];
    $cabang = $_POST['cabang'];
    $stok = $_POST['stok'];
    $jml_beli = $_POST['jml_beli'];
    $harga_disc = $_POST['harga_disc'];
    $harga_jual = $_POST['harga_jual'];
    
// Turn autocommit off
mysqli_begin_transaction($konek);

$query = "INSERT INTO `produk` (
`id_produk`,
`nama_produk`,
  `id_kategori`,
  `harga_beli`,
  `id_satuan`,
  `deskripsi`,
  `foto`,
  `foto2`,
  `user`,
  `created`
)
VALUES
  (
	'$id_produk',
	'$nama_produk',
    '$id_kategori',
    '$harga_beli',
    '$id_satuan',
    '$deskripsi',
    '$foto',
	'$foto2',
	'$user',
	curdate()
  )

";	
$query2 = "INSERT INTO `produk_details` (
`id_produk`,
`stok`,
  `jml_beli`,
  `harga_disc`,
  `harga_jual`,
  `cabang`
)
VALUES
  (
	'$id_produk',
	'$stok',
    '$jml_beli',
    '$harga_disc',
    '$harga_jual',
    '$cabang'
  )

";
try{
mysqli_query($konek,$query);
mysqli_query($konek,$query2);
mysqli_commit($konek);
$response["kode"] = 1;
  $response["pesan"] = "Tambah Produk Sukses.";
  echo json_encode($response);
} catch (mysqli_sql_exception $exception) {
  mysqli_rollback($konek);
  $response["kode"] = 0;
  $response["pesan"] = "oops! gagal!";
  echo json_encode($response);
  throw $exception;
}

?>