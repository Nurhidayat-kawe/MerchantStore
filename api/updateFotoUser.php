<?php 
require_once 'koneksi.php'; 

$id_user   = $_POST['id_user'] ?? '';
$foto_user = $_POST['foto_user'] ?? '';

if (!empty($id_user) && !empty($foto_user)) {
    try {
        // Gunakan prepared statement untuk mencegah SQL Injection
        $stmt = $konek->prepare("
            UPDATE users 
            SET foto_user = ? 
            WHERE id_user = ?
        ");
        $stmt->bind_param("ss", $foto_user, $id_user);

        if ($stmt->execute()) {
            $response = [
                "kode" => 1,
                "pesan" => "Proses berhasil"
            ];
        } else {
            $response = [
                "kode" => 0,
                "pesan" => "Gagal memperbarui data!"
            ];
        }

        $stmt->close();
    } catch (mysqli_sql_exception $e) {
        $response = [
            "kode" => 0,
            "pesan" => "Terjadi kesalahan server!"
        ];
    }
} else {
    $response = [
        "kode" => 0,
        "pesan" => "Data tidak lengkap!"
    ];
}

header('Content-Type: application/json');
echo json_encode($response);
?>
