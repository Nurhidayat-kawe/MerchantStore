<?php
require_once 'koneksi.php';
header('Content-Type: application/json');

$id_transaksi = $_POST['id_transaksi'] ?? '';
$id_keranjang = $_POST['id_keranjang'] ?? '';
$id_user      = $_POST['id_user'] ?? '';
$produk       = $_POST['produk'] ?? '';
$jumlah       = floatval($_POST['jumlah'] ?? 0);
$harga        = floatval($_POST['harga'] ?? 0);
$diskon       = floatval($_POST['diskon'] ?? 0);
$h_beli       = floatval($_POST['h_beli'] ?? 0);

mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

try {
    $konek->begin_transaction();

    // 🔍 Cek stok produk dulu
    $stmt_stok = $konek->prepare("SELECT stok FROM produk_details WHERE id_produk = ? FOR UPDATE");
    $stmt_stok->bind_param("s", $produk);
    $stmt_stok->execute();
    $result_stok = $stmt_stok->get_result();

    if ($result_stok->num_rows === 0) {
        throw new Exception("Produk tidak ditemukan!");
    }

    $row_stok = $result_stok->fetch_assoc();
    $stok_saat_ini = floatval($row_stok['stok']);

    if ($stok_saat_ini < $jumlah) {
        throw new Exception("Stok tidak mencukupi! Sisa stok: $stok_saat_ini");
    }

    // 🔍 Cek apakah id_transaksi sudah ada
    $stmt_cek = $konek->prepare("SELECT id FROM transaksi WHERE id_transaksi = ?");
    $stmt_cek->bind_param("s", $id_transaksi);
    $stmt_cek->execute();
    $result_cek = $stmt_cek->get_result();

    // 🧾 Jika belum ada, buat transaksi baru
    if ($result_cek->num_rows == 0) {
        $stmt_insert_trans = $konek->prepare("
            INSERT INTO transaksi (
                id_transaksi, id_user, tanggal, status, ongkir, status_bayar, jam
            ) VALUES (?, ?, CURDATE(), 'baru', '0', 'belum', CURTIME())
        ");
        $stmt_insert_trans->bind_param("ss", $id_transaksi, $id_user);
        $stmt_insert_trans->execute();
    }

    // 🛒 Insert ke transaksi_details
    $stmt_detail = $konek->prepare("
        INSERT INTO transaksi_details (
            id_transaksi, produk, jumlah, harga, diskon, h_beli
        ) VALUES (?, ?, ?, ?, ?, ?)
    ");
    $stmt_detail->bind_param("ssdddd", $id_transaksi, $produk, $jumlah, $harga, $diskon, $h_beli);
    $stmt_detail->execute();

    // 📦 Update stok produk (hanya jika stok cukup)
    $stmt_update_stok = $konek->prepare("
        UPDATE produk_details 
        SET stok = stok - ? 
        WHERE id_produk = ?
    ");
    $stmt_update_stok->bind_param("ds", $jumlah, $produk);
    $stmt_update_stok->execute();

    // 🗑️ Hapus dari keranjang
    $stmt_delete_cart = $konek->prepare("DELETE FROM keranjang WHERE id_keranjang = ?");
    $stmt_delete_cart->bind_param("s", $id_keranjang);
    $stmt_delete_cart->execute();

    $konek->commit();

    echo json_encode([
        "kode"  => 1,
        "pesan" => "Tambah transaksi sukses."
    ]);

} catch (Exception $e) {
    $konek->rollback();
    echo json_encode([
        "kode"  => 0,
        "pesan" => "Gagal simpan transaksi: " . $e->getMessage()
    ]);
}
?>
