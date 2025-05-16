<?php 
require_once 'koneksi.php';
$id = $_POST['id'];
$query = "SELECT sum(jumlah) as jml_trans 
from transaksi_details INNER JOIN transaksi ON transaksi.id_transaksi=transaksi_details.id_transaksi 
where status='selesai' and produk='$id' group by produk";

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