<?php 
require_once 'koneksi.php';
$id_user = $_POST['id_user'];
$query = "SELECT keranjang.*,produk.*,produk_details.*,satuan.nama_satuan,kategori.nama_kategori 
from keranjang 
inner join produk on produk.id_produk=keranjang.id_produk 
inner join satuan on satuan.id_satuan=produk.id_satuan
inner join kategori on kategori.id_kategori=produk.id_kategori 
inner join produk_details on produk_details.id_produk=produk.id_produk 
where keranjang.id_user = '$id_user' and deleted='0000-00-00'";

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