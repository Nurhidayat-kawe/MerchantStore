<?php 
require_once 'koneksi.php';
$id_users = $_POST['id_users'];
$array = array();
$query = "SELECT stat_user FROM users where id_user = '$id_users' and role ='user'";
$result = mysqli_query($konek,$query);
while ($row  = mysqli_fetch_assoc($result))
{
	$array[] = $row; 
}
echo ($result) ? 
json_encode(array("kode" => 1, "result"=>$array)) :
json_encode(array("kode" => 0, "pesan"=>"data tidak ditemukan"));

?>