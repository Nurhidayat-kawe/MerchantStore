<?php 
require_once 'koneksi.php';
$query = "SELECT produk.*,produk_details.stok,produk_details.harga_jual,transaksi_details.*,sum(transaksi_details.jumlah) as jumlah from produk 
inner join produk_details on produk_details.id_produk=produk.id_produk 
inner join transaksi_details on transaksi_details.produk=produk.id_produk 
where deleted='0000-00-00' 
group by transaksi_details.produk 
order by jumlah desc limit 50";

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