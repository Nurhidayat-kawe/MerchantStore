<?php 
require_once 'koneksi.php';

header('Content-Type: application/json');

// Validasi input
if (!isset($_POST['kat'])) {
    echo json_encode(['kode' => 0, 'pesan' => 'Parameter kategori diperlukan']);
    exit;
}

$key = isset($_POST['key']) ? trim($_POST['key']) : null;
$kat = trim($_POST['kat']);
$id_user = isset($_POST['id_user']) ? $_POST['id_user'] : null;

// Escape special characters for LIKE query
$searchKey = $key !== null ? str_replace(['%', '_'], ['\%', '\_'], $key) : null;
$searchKat = str_replace(['%', '_'], ['\%', '\_'], $kat);

// Gunakan prepared statement untuk mencegah SQL injection
if ($key === null || $key === '') {
    // Query tanpa pencarian keyword
    $query = "SELECT 
                p.*,
                pd.*,
                s.nama_satuan,
                k.nama_kategori 
              FROM produk p
              INNER JOIN satuan s ON s.id_satuan = p.id_satuan
              INNER JOIN kategori k ON k.id_kategori = p.id_kategori 
              INNER JOIN produk_details pd ON pd.id_produk = p.id_produk 
              WHERE p.deleted = '0000-00-00' 
              AND k.nama_kategori LIKE CONCAT('%', ?, '%')";
    
    $stmt = mysqli_prepare($konek, $query);
    mysqli_stmt_bind_param($stmt, "s", $searchKat);
} else {
    // Query dengan pencarian keyword
    $query = "SELECT 
            p.*,
            pd.*,
            s.nama_satuan,
            k.nama_kategori 
          FROM produk p
          INNER JOIN satuan s ON s.id_satuan = p.id_satuan 
          INNER JOIN kategori k ON k.id_kategori = p.id_kategori 
          INNER JOIN produk_details pd ON pd.id_produk = p.id_produk 
          WHERE 
            p.nama_produk LIKE CONCAT('%', ?, '%') 
            AND p.deleted = '0000-00-00' 
            AND k.nama_kategori LIKE CONCAT('%', ?, '%')";

$stmt = mysqli_prepare($konek, $query);
mysqli_stmt_bind_param($stmt, "ss", $searchKey, $searchKat);
}

// Eksekusi query
if (!mysqli_stmt_execute($stmt)) {
    echo json_encode(['kode' => 0, 'pesan' => 'Gagal mengeksekusi query']);
    exit;
}

$result = mysqli_stmt_get_result($stmt);
$array = [];

while ($row = mysqli_fetch_assoc($result)) {
    $array[] = $row; 
}

if (!empty($array)) {
    // Kompres data numerik
    echo json_encode([
        'kode' => 1, 
        'result' => $array
    ], JSON_NUMERIC_CHECK);
} else {
    echo json_encode([
        'kode' => 0, 
        'pesan' => 'Data tidak ditemukan'
    ]);
}

// Tutup statement
mysqli_stmt_close($stmt);
mysqli_close($konek);
?>