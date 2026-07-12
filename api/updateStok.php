<?php 
require_once 'koneksi.php'; 

$id_produk = $_POST['id_produk'] ?? '';
$stok      = $_POST['stok'] ?? 0;

if (!empty($id_produk) && is_numeric($stok)) {
    try {
        // Gunakan prepared statement untuk mencegah SQL injection
        $stmt = $konek->prepare("
            UPDATE produk_details 
            SET stok = GREATEST(stok - ?, 0)
            WHERE id_produk = ?
        ");
        $stmt->bind_param("is", $stok, $id_produk);

        if ($stmt->execute()) {
            $response = [
                "kode" => 1,
                "pesan" => "Simpan Stok Berhasil"
            ];
        } else {
            $response = [
                "kode" => 0,
                "pesan" => "Gagal menyimpan stok!"
            ];
        }

        $stmt->close();
    } catch (mysqli_sql_exception $e) {
        $response = [
            "kode" => 0,
            "pesan" => "Terjadi kesalahan server."
        ];
    }
} else {
    $response = [
        "kode" => 0,
        "pesan" => "Data tidak valid!"
    ];
}

header('Content-Type: application/json');
echo json_encode($response);
?>
