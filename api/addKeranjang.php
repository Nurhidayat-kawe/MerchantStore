<?php
require_once 'koneksi.php';
header('Content-Type: application/json');

// Pastikan semua parameter dikirim
if (!isset($_POST['id_user'], $_POST['id_produk'], $_POST['jml'])) {
    echo json_encode(["kode" => 0, "pesan" => "Parameter tidak lengkap"]);
    exit;
}

$id_user   = $_POST['id_user'];
$id_produk = $_POST['id_produk'];
$jml       = (int) $_POST['jml']; // pastikan integer

// 1️⃣ Cek apakah produk sudah ada di keranjang
$sqlCek = "SELECT id_keranjang FROM keranjang WHERE id_user = ? AND id_produk = ?";
$stmtCek = $konek->prepare($sqlCek);
$stmtCek->bind_param("ss", $id_user, $id_produk);
$stmtCek->execute();
$resultCek = $stmtCek->get_result();

if ($resultCek->num_rows == 0) {
    // 2️⃣ Jika belum ada, insert baru
    $sqlInsert = "INSERT INTO keranjang (id_user, id_produk, jml) VALUES (?, ?, ?)";
    $stmtInsert = $konek->prepare($sqlInsert);
    $stmtInsert->bind_param("ssi", $id_user, $id_produk, $jml);

    if ($stmtInsert->execute()) {
        echo json_encode(["kode" => 1, "pesan" => "Proses berhasil"]);
    } else {
        echo json_encode(["kode" => 0, "pesan" => "Oops! Gagal diproses (insert)"]);
    }

    $stmtInsert->close();
} else {
    // 3️⃣ Jika sudah ada, update jumlah
    $sqlUpdate = "UPDATE keranjang SET jml = jml + ? WHERE id_user = ? AND id_produk = ?";
    $stmtUpdate = $konek->prepare($sqlUpdate);
    $stmtUpdate->bind_param("iss", $jml, $id_user, $id_produk);

    if ($stmtUpdate->execute()) {
        echo json_encode(["kode" => 1, "pesan" => "Proses update berhasil"]);
    } else {
        echo json_encode(["kode" => 0, "pesan" => "Oops! Gagal diproses (update)"]);
    }

    $stmtUpdate->close();
}

$stmtCek->close();
$konek->close();
?>

