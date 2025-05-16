<?php 
require_once 'koneksi.php';
$query = "SELECT sum(b.jumlah*b.harga)+ongkir-diskon as total from transaksi 
inner join transaksi_details as b on b.id_transaksi=transaksi.id_transaksi 
where transaksi.status='selesai' and transaksi.tanggal=CURRENT_DATE";

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