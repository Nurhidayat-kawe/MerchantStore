<?php 
require_once 'koneksi.php'; 

$id_user = $_POST['id_user'] ?? '';
$jml_point_potongan = $_POST['jml_point_potongan'] ?? 0;

if (!empty($id_user) && is_numeric($jml_point_potongan)) {
    try {
        // Siapkan query dengan prepared statement
        $stmt = $konek->prepare("
            UPDATE users 
            SET point = point - ? 
            WHERE id_user = ?
        ");
        $stmt->bind_param("is", $jml_point_potongan, $id_user);

        if ($stmt->execute()) {
            $response = [
                "kode" => 1,
                "pesan" => "Update Point Berhasil"
            ];
        } else {
            $response = [
                "kode" => 0,
                "pesan" => "Gagal update point!"
            ];
        }

        $stmt->close();
    } catch (mysqli_sql_exception $e) {
        $response = [
            "kode" => 0,
            "pesan" => "Terjadi kesalahan pada server."
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
