<?php 
require_once 'koneksi.php';
$tgl_awal = $_POST['tgl_awal'];
$tgl_ahir = $_POST['tgl_ahir'];
$query = "SELECT produk.*,
transaksi.*,
b.*,
sum(transaksi.ongkir) as ongkir,
sum(b.jumlah * (b.harga - produk.harga_beli))as untung,
sum(b.jumlah * b.harga)as total,
sum(b.diskon) as diskon from transaksi 
inner join transaksi_details as b on b.id_transaksi=transaksi.id_transaksi 
inner join produk on produk.id_produk = b.produk 
where transaksi.status='selesai' and transaksi.tanggal between '$tgl_awal' and '$tgl_ahir'   
group by transaksi.tanggal";

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