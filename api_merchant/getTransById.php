<?php 
require_once 'koneksi.php';
$id_trans = $_POST['id_trans'];   
$query = "SELECT cabang.*,transaksi.*,transaksi_details.*,produk.*,produk_details.*,users.* FROM transaksi_details 
INNER JOIN produk ON produk.id_produk = transaksi_details.produk 
INNER JOIN produk_details ON produk_details.id_produk = produk.id_produk 
INNER JOIN transaksi ON transaksi.id_transaksi = transaksi_details.id_transaksi 
INNER JOIN users ON users.id_user = transaksi.id_user 
INNER JOIN cabang ON cabang.id_cabang = users.cabang  
WHERE transaksi_details.id_transaksi='$id_trans' 
GROUP BY transaksi_details.id_details 
order by produk.id_kategori asc";

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