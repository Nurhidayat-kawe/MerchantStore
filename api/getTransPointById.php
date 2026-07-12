<?php 
require_once 'koneksi.php';
$id_trans = $_POST['id_trans'];

if($id_trans =="semua"){
$query = "SELECT cabang.*,transaksi_point.*,transaksi_details_point.*,produk.*,produk_details.*,users.* FROM transaksi_details_point 
INNER JOIN produk ON produk.id_produk = transaksi_details_point.produk 
INNER JOIN produk_details ON produk_details.id_produk = produk.id_produk 
INNER JOIN transaksi_point ON transaksi_point.id_transaksi = transaksi_details_point.id_transaksi 
INNER JOIN users ON users.id_user = transaksi_point.id_user 
INNER JOIN cabang ON cabang.id_cabang = users.cabang  
GROUP BY transaksi_details_point.id_details 
order by produk.id_kategori asc limit 100";
}else{   
$query = "SELECT cabang.*,transaksi_point.*,transaksi_details_point.*,produk.*,produk_details.*,users.* FROM transaksi_details_point 
INNER JOIN produk ON produk.id_produk = transaksi_details_point.produk 
INNER JOIN produk_details ON produk_details.id_produk = produk.id_produk 
INNER JOIN transaksi_point ON transaksi_point.id_transaksi = transaksi_details_point.id_transaksi 
INNER JOIN users ON users.id_user = transaksi_point.id_user 
INNER JOIN cabang ON cabang.id_cabang = users.cabang  
WHERE transaksi_details_point.id_transaksi='$id_trans' 
GROUP BY transaksi_details_point.id_details 
order by produk.id_kategori asc";
}
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