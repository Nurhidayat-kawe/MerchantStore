<?php 
require_once 'koneksi.php'; 

$hari      = $_POST['hari'] ?? '';
$jam_buka  = $_POST['jam_buka'] ?? '';
$jam_tutup = $_POST['jam_tutup'] ?? '';

if (!empty($hari) && !empty($jam_buka) && !empty($jam_tutup)) {
    try {
        // Siapkan query menggunakan prepared statement
        $stmt = $konek->prepare("
            UPDATE jam_operasional 
            SET jam_buka = ?, jam_tutup = ? 
            WHERE hari = ?
        ");
        $stmt->bind_param("sss", $jam_buka, $jam_tutup, $hari);

        if ($stmt->execute()) {
            $response = [
                "kode" => 1,
                "pesan" => "Update Berhasil"
            ];
        } else {
            $response = [
                "kode" => 0,
                "pesan" => "Gagal update data!"
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

// Output dalam format JSON
header('Content-Type: application/json');
echo json_encode($response);
?>
