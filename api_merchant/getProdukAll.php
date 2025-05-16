<?php 
require_once 'koneksi.php';
$key = $_POST['key'];
$kat = $_POST['kat'];
if($key==null){
$query = "SELECT produk.*,produk_details.*,satuan.nama_satuan,kategori.nama_kategori from produk inner join satuan on satuan.id_satuan=produk.id_satuan
inner join kategori on kategori.id_kategori=produk.id_kategori 
inner join produk_details on produk_details.id_produk=produk.id_produk where deleted='0000-00-00' and nama_kategori like '%$kat%'";
}else{
$query = "SELECT produk.*,produk_details.*,satuan.nama_satuan,kategori.nama_kategori from produk inner join satuan on satuan.id_satuan=produk.id_satuan
inner join kategori on kategori.id_kategori=produk.id_kategori 
inner join produk_details on produk_details.id_produk=produk.id_produk 
where produk.nama_produk like '%$key%' and deleted='0000-00-00' and nama_kategori like '%$kat%'";
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