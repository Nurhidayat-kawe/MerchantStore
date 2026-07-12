<?php 
require_once 'koneksi.php';
$tgl_awal = $_POST['tgl_awal'];
$tgl_ahir = $_POST['tgl_ahir'];
$query = "SELECT produk.*,
transaksi_point.*,
b.* from transaksi_point 
inner join transaksi_details_point as b on b.id_transaksi=transaksi_point.id_transaksi 
inner join produk on produk.id_produk = b.produk 
where transaksi_point.status='selesai' and transaksi_point.tanggal between '$tgl_awal' and '$tgl_ahir' 
order by id_details asc";

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