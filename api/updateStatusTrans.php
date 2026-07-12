<?php 
require_once 'koneksi.php'; 

$id     = $_POST['id'] ?? '';
$status = $_POST['status'] ?? '';

if (!empty($id) && !empty($status)) {
    try {
        // Gunakan prepared statement untuk mencegah SQL Injection
        $stmt = $konek->prepare("
            UPDATE transaksi 
            SET status = ? 
            WHERE id = ?
        ");
        $stmt->bind_param("si", $status, $id);

        if ($stmt->execute()) {
            $response = [
                "kode" => 1,
                "pesan" => "Proses berhasil"
            ];
        } else {
            $response = [
                "kode" => 0,
                "pesan" => "Gagal memproses data"
            ];
        }

        $stmt->close();
    } catch (mysqli_sql_exception $e) {
        $response = [
            "kode" => 0,
            "pesan" => "Terjadi kesalahan server"
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
