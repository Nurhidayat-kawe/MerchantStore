<?php 
require_once 'koneksi.php';
$id_user = $_POST['id_user'];
$query = "SELECT * FROM users where id_user ='$id_user'";

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