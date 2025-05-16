<?php 
require_once 'koneksi.php';
$query = "SELECT users.*,count(transaksi_details.id_transaksi) as jml_trans, sum(jumlah*harga) as jumlah  
from users inner join transaksi on transaksi.id_user = users.id_user 
inner join transaksi_details on transaksi_details.id_transaksi = transaksi.id_transaksi  
where role='user' or status='selesai' group by id_user order by jumlah desc";

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