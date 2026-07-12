// endpoint: get_keranjang_item.php
<?php
require_once 'koneksi.php';

header('Content-Type: application/json');

if (!isset($_POST['id_user']) || !isset($_POST['id_produk'])) {
    echo json_encode(['kode' => 0, 'pesan' => 'Parameter tidak lengkap']);
    exit;
}

$id_user = $_POST['id_user'];
$id_produk = $_POST['id_produk'];

$query = "SELECT jml FROM keranjang 
          WHERE id_user = ? AND id_produk = ? LIMIT 1";

$stmt = mysqli_prepare($konek, $query);
mysqli_stmt_bind_param($stmt, "ss", $id_user, $id_produk);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);

if ($result && mysqli_num_rows($result) > 0) {
    $array = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $array[] = $row;
    }
    echo json_encode(array("kode" => 1, "result" => $array));
} else {
    echo json_encode(array("kode" => 0, "pesan" => "data tidak ditemukan"));
}


mysqli_stmt_close($stmt);
mysqli_close($konek);
?>