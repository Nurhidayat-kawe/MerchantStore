<?php 
require_once 'koneksi.php';
$status = $_POST['status'];
$id_user = $_POST['id_user'];
if($status=="semua"){
    $query = "SELECT transaksi_point.*,users.* from transaksi_point 
inner join users 
on users.id_user = transaksi_point.id_user where transaksi_point.id_user='$id_user' ORDER BY tanggal DESC, jam DESC ";
}else if($status=="semua_admin"){
    $query = "SELECT transaksi_point.*,users.* from transaksi_point 
inner join users 
on users.id_user = transaksi_point.id_user ORDER BY tanggal DESC, jam DESC ";
}else{
$query = "SELECT transaksi_point.*,users.* from transaksi_point 
inner join users on users.id_user = transaksi_point.id_user 
where transaksi_point.status='$status' and  transaksi_point.id_user='$id_user' ORDER BY tanggal DESC, jam DESC ";
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
