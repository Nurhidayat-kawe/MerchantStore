<?php
require_once 'koneksi.php';

$id = $_POST['id'] ?? '';
$id_user = $_POST['id_user'] ?? '';

if (!empty($id)) {
    $stmt = $konek->prepare("DELETE FROM `keranjang` WHERE `id_keranjang` = ?");
    $stmt->bind_param("s", $id);

    if ($stmt->execute()) {
        $response = [
            "kode" => 1,
            "pesan" => "Hapus Berhasil"
        ];
    } else {
        $response = [
            "kode" => 0,
            "pesan" => "Gagal Hapus!"
        ];
    }

    $stmt->close();
} else {
    $response = [
        "kode" => 0,
        "pesan" => "ID tidak valid!"
    ];
}

echo json_encode($response);
