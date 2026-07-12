<?php 
require_once 'koneksi.php';
$id_trans = $_POST['id_trans'];
$query = "SELECT cabang.*,transaksi_details.*, produk.nama_produk from transaksi_details  
inner join produk on produk.id_produk = transaksi_details.produk 
inner join produk_details on produk_details.id_produk = produk.id_produk 
inner join cabang on cabang.id_cabang = produk_details.cabang 
where transaksi_details.id_transaksi='$id_trans' ";

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