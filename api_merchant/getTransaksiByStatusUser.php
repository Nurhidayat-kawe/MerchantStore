<?php 
require_once 'koneksi.php';
$status = $_POST['status'];
$id_user = $_POST['id_user'];
if($status=="semua"){
    $query = "SELECT transaksi.*,users.* from transaksi 
inner join users 
on users.id_user = transaksi.id_user where transaksi.id_user='$id_user' ORDER BY tanggal DESC, jam DESC ";
}else{
$query = "SELECT transaksi.*,users.* from transaksi 
inner join users on users.id_user = transaksi.id_user 
where transaksi.status='$status' and  transaksi.id_user='$id_user' ORDER BY tanggal DESC, jam DESC ";
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
