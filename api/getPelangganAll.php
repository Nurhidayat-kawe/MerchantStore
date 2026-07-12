<?php
require_once 'koneksi.php';
header('Content-Type: application/json');

$role = 'user';
$stmt = $konek->prepare("
    SELECT * FROM users
    WHERE role = ?
    GROUP BY id_user
    ORDER BY id_user DESC
    LIMIT 10
");
$stmt->bind_param("s", $role);
$stmt->execute();
$result = $stmt->get_result();

$data = [];
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

if ($result->num_rows > 0) {
    echo json_encode(["kode" => 1, "result" => $data]);
} else {
    echo json_encode(["kode" => 0, "pesan" => "data tidak ditemukan"]);
}

$stmt->close();
$konek->close();
?>

