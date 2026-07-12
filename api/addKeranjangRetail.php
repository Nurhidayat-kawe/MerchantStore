<?php
require_once 'koneksi.php';

// Untuk debugging lokal (hapus di produksi)
error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json; charset=utf-8');

// Inisialisasi respon default
$response = ["kode" => 0, "pesan" => "Aksi tidak valid"];

// Validasi input wajib
if (empty($_POST['id_user']) || empty($_POST['id_produk']) || empty($_POST['aksi'])) {
    $response["pesan"] = "Parameter tidak lengkap!";
    echo json_encode($response);
    exit;
}

$id_user   = trim($_POST['id_user']);
$id_produk = trim($_POST['id_produk']);
$aksi      = trim($_POST['aksi']);

// Cek apakah produk sudah ada di keranjang
$check_query = "SELECT id_keranjang FROM keranjang WHERE id_user = ? AND id_produk = ?";
$stmt = $konek->prepare($check_query);
$stmt->bind_param("ss", $id_user, $id_produk);
$stmt->execute();
$stmt->store_result();

if ($stmt->num_rows == 0) {
    // Jika belum ada → insert baru
    $stmt->close();
    $insert_query = "INSERT INTO keranjang (id_user, id_produk, jml) VALUES (?, ?, 1)";
    $stmt = $konek->prepare($insert_query);
    $stmt->bind_param("ss", $id_user, $id_produk);

    if ($stmt->execute()) {
        $response = ["kode" => 1, "pesan" => "Tambah keranjang berhasil"];
    } else {
        $response["pesan"] = "Gagal menambah keranjang: " . $stmt->error;
    }
} else {
    // Sudah ada → lakukan aksi
    $stmt->close();

    if ($aksi === "plus") {
        $query = "UPDATE keranjang SET jml = jml + 1 WHERE id_user = ? AND id_produk = ?";
    } elseif ($aksi === "min") {
        // Jika jml jadi 0 → hapus otomatis
        $query = "UPDATE keranjang SET jml = jml - 1 WHERE id_user = ? AND id_produk = ? AND jml > 0";
    } elseif ($aksi === "hapus") {
        $query = "DELETE FROM keranjang WHERE id_user = ? AND id_produk = ?";
    } else {
        $response["pesan"] = "Aksi tidak dikenali!";
        echo json_encode($response);
        exit;
    }

    $stmt = $konek->prepare($query);
    $stmt->bind_param("ss", $id_user, $id_produk);

    if ($stmt->execute()) {
        // Cek apakah item dihapus karena jml habis
        if ($aksi === "min") {
            $cek

