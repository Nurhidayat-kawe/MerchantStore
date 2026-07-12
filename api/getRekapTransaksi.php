<?php 
require_once 'koneksi.php';
$tgl_awal = $_POST['tgl_awal'];
$tgl_ahir = $_POST['tgl_ahir'];
$query = "SELECT produk.*,
transaksi.*,
b.* from transaksi 
inner join transaksi_details as b on b.id_transaksi=transaksi.id_transaksi 
inner join produk on produk.id_produk = b.produk 
where transaksi.status='selesai' and transaksi.tanggal between '$tgl_awal' and '$tgl_ahir' 
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