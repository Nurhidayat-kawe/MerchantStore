<?php
require_once 'koneksi.php';
header('Content-Type: application/json');

$id_produk      = $_POST['id_produk'] ?? '';
$nama_produk    = $_POST['nama_produk'] ?? '';
$id_kategori    = $_POST['id_kategori'] ?? '';
$harga_beli     = $_POST['harga_beli'] ?? '0';
$id_satuan      = $_POST['id_satuan'] ?? '';
$deskripsi      = $_POST['deskripsi'] ?? '';
$user           = $_POST['user'] ?? '';
$harga_jual     = $_POST['harga_jual'] ?? '0';
$jml_beli       = $_POST['jml_beli'] ?? '0';
$harga_diskon   = $_POST['harga_diskon'] ?? '0';
$jml_beli2      = $_POST['jml_beli2'] ?? '0';
$harga_diskon2  = $_POST['harga_diskon2'] ?? '0';
$jml_beli3      = $_POST['jml_beli3'] ?? '0';
$harga_diskon3  = $_POST['harga_diskon3'] ?? '0';
$jml_point      = $_POST['jml_point'] ?? '0';

mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

try {
    $konek->begin_transaction();

    // Update tabel produk
    $stmt1 = $konek->prepare("
        UPDATE produk 
        SET nama_produk = ?, id_kategori = ?, harga_beli = ?, id_satuan = ?, deskripsi = ?, 
            user = ?, updated = NOW(), jml_point = ?
        WHERE id_produk = ?
    ");
    $stmt1->bind_param("ssssssss", $nama_produk, $id_kategori, $harga_beli, $id_satuan,
        $deskripsi, $user, $jml_point, $id_produk);
    $stmt1->execute();

    // Update tabel produk_details
    $stmt2 = $konek->prepare("
        UPDATE produk_details
        SET harga_jual = ?, jml_beli = ?, harga_disc = ?, 
            jml_beli2 = ?, harga_disc2 = ?, 
            jml_beli3 = ?, harga_disc3 = ?
        WHERE id_produk = ?
    ");
    $stmt2->bind_param("ssssssss", $harga_jual, $jml_beli, $harga_diskon, $jml_beli2, 
        $harga_diskon2, $jml_beli3, $harga_diskon3, $id_produk);
    $stmt2->execute();

    $konek->commit();

    echo json_encode(["kode" => 1, "pesan" => "Update produk sukses."]);

} catch (mysqli_sql_exception $e) {
    $konek->rollback();
    echo json_encode(["kode" => 0, "pesan" => "Gagal update: " . $e->getMessage()]);
}
?>
