<?php 
require_once 'koneksi.php';

$hari = $_POST['hari'] ?? null;

try {
    if (empty($hari)) {
        // Query semua data jika hari tidak dikirim
        $stmt = $konek->prepare("SELECT * FROM jam_operasional");
    } else {
        // Query berdasarkan hari tertentu (pakai prepared statement)
        $stmt = $konek->prepare("SELECT * FROM jam_operasional WHERE hari = ?");
        $stmt->bind_param("s", $hari);
    }

    $stmt->execute();
    $result = $stmt->get_result();

    $array = [];
    while ($row = $result->fetch_assoc()) {
        $array[] = $row;
    }

    if (count($array) > 0) {
        $response = [
            "kode" => 1,
            "result" => $array
        ];
    } else {
        $response = [
            "kode" => 0,
            "pesan" => "Data tidak ditemukan"
        ];
    }

    $stmt->close();
} catch (mysqli_sql_exception $e) {
    $response = [
        "kode" => 0,
        "pesan" => "Terjadi kesalahan server!"
    ];
}

header('Content-Type: application/json');
echo json_encode($response);
?>
