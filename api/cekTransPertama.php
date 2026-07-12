<?php 
require_once 'koneksi.php';
$id_user = $_POST['id_user'];
$array = array();
$query = "SELECT count(transaksi.id_user) as trans,reveral FROM transaksi inner join users on users.id_user = transaksi.id_user where transaksi.id_user = '$id_user' and status ='selesai'";
$result = mysqli_query($konek,$query);
while ($row  = mysqli_fetch_assoc($result))
{
	$array[] = $row; 
}
echo ($result) ? 
json_encode(array("kode" => 1, "result"=>$array)) :
json_encode(array("kode" => 0, "pesan"=>"data tidak ditemukan"));

?>