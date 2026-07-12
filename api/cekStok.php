<?php 
require_once 'koneksi.php';
$id_produk = $_POST['id_produk'];
$query = "SELECT stok from produk_details where id_produk = '$id_produk'";

$result = mysqli_query($konek,$query);

$array = array();

while ($row  = mysqli_fetch_assoc($result))
{
	$array[] = $row; 
}

echo ($result) ? 
json_encode(array("kode" => 1, "result"=>$array)) :
json_encode(array("kode" => 0, "pesan"=>"data tidak ditemukan"));

?>