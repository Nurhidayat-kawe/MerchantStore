<?php 
require_once 'koneksi.php';
$query = "SELECT 
    a.id_user,
    a.nama,
    a.telp,
    a.foto_user, 
    COUNT(b.id_user) as jml_referree 
FROM 
    users as a 
INNER JOIN 
    users as b ON b.reveral = a.id_user  -- Hanya user yang direferensikan oleh orang lain
WHERE 
    a.stat_user = 'aktif' 
GROUP BY 
    a.id_user  -- Kelompokkan berdasarkan user
HAVING 
    COUNT(b.id_user) > 0  -- Hanya tampilkan yang punya referree
ORDER BY 
    a.id_user ASC";

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