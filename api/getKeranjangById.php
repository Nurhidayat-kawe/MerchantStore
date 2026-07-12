<?php
require_once 'koneksi.php';

// Tampilkan semua error sementara untuk debug
error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json');

// Validasi parameter
if (!isset($_POST['id_user']) || empty($_POST['id_user'])) {
    echo json_encode(["kode" => 0, "pesan" => "Parameter id_user tidak lengkap"]);
    exit;
}

$id_user = $_POST['id_user'];

// Query dengan LEFT JOIN untuk aman jika beberapa data kosong
$sql = "SELECT 
            keranjang.*, 
            produk.*, 
            produk_details.*, 
            satuan.nama_satuan, 
            kategori.nama_kategori 
        FROM keranjang 
        INNER JOIN produk ON produk.id_produk = keranjang.id_produk 
        INNER JOIN satuan ON satuan.id_satuan = produk.id_satuan
        INNER JOIN kategori ON kategori.id_kategori = produk.id_kategori 
        LEFT JOIN produk_details ON produk_details.id_produk = produk.id_produk 
        WHERE keranjang.id_user = ? 
          AND (produk.deleted IS NULL OR produk.deleted = '0000-00-00')";

// Prepare statement
$stmt = $konek->prepare($sql);
if (!$stmt) {
    echo json_encode([
        "kode" => 0,
        "pesan" => "Prepare statement gagal: " . $konek->error
    ]);
    exit;
}

// Bind param dan execute
$stmt->bind_param("s", $id_user);

if (!$stmt->execute()) {
    echo json_encode([
        "kode" => 0,
        "pesan" => "Execute statement gagal: " . $stmt->error
    ]);
    exit;
}

// Ambil hasil
$result = $stmt->get_result();
if (!$result) {
    echo json_encode([
        "kode" => 0,
        "pesan" => "Get result gagal: " . $stmt->error
    ]);
    exit;
}

// Simpan hasil ke array
$data = [];
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

// Output JSON
if (count($data) > 0) {
    echo json_encode(["kode" => 1, "result" => $data]);
} else {
    echo json_encode(["kode" => 0, "pesan" => "Data tidak ditemukan"]);
}

// Tutup statement & koneksi
$stmt->close();
$konek->close();
?>

