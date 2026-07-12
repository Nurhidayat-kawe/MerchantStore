<?php
require_once 'koneksi.php';
header('Content-Type: application/json');

$id_status = $_POST['id_status'] ?? 1;
$tgl_ed = $_POST['tgl_ed'] ?? null;
$tenggang_point = $_POST['tenggang_point'] ?? null;

$fields = [];
if ($tgl_ed !== null) $fields[] = "tgl_ed = '$tgl_ed'";
if ($tenggang_point !== null) {
    if ($tenggang_point === '' || $tenggang_point === 'null') {
        $fields[] = "tenggang_point = NULL";
    } else {
        $fields[] = "tenggang_point = '$tenggang_point'";
    }
}

if (empty($fields)) {
    echo json_encode(["kode" => 0, "pesan" => "Tidak ada data diupdate"]);
    exit;
}

$setClause = implode(", ", $fields);
$query = "UPDATE status_server SET $setClause WHERE id_status = " . intval($id_status);

if (mysqli_query($konek, $query)) {
    echo json_encode(["kode" => 1, "pesan" => "Update berhasil"]);
} else {
    echo json_encode(["kode" => 0, "pesan" => "Gagal: " . mysqli_error($konek)]);
}
