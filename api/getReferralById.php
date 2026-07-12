<?php 
require_once 'koneksi.php';
$id_user = $_POST['id_user'];
$query = "SELECT a.id_user,a.nama,a.foto_user, b.id_user AS id_referree, b.nama AS nama_referree, b.foto_user AS foto_referree, b.tgl_daftar  
FROM users AS a INNER JOIN users AS b ON b.reveral = a.id_user 
WHERE b.stat_user = 'aktif' 
AND b.reveral = '$id_user' 
AND b.reveral != 0 
ORDER BY id_referree ASC";

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