<?php 
require_once 'koneksi.php';
$username = $_POST['username'];
$password = $_POST['password'];
$array = array();
$query = "SELECT * FROM users where username = '$username' AND password = '$password' and role ='user'";
$result = mysqli_query($konek,$query);
while ($row  = mysqli_fetch_assoc($result))
{
	$array[] = $row; 
}
echo ($result) ? 
json_encode(array("kode" => 1, "result"=>$array)) :
json_encode(array("kode" => 0, "pesan"=>"data tidak ditemukan"));

?>