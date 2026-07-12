<?php
require_once 'koneksi.php';
header('Content-Type: application/json');

// Ambil semua input dengan aman
$id_transaksi = $_POST['id_transaksi'] ?? '';
$id_keranjang = $_POST['id_keranjang'] ?? '';
$id_user      = $_POST['id_user'] ?? '';
$produk       = $_POST['produk'] ?? '';
$jumlah       = $_POST['jumlah'] ?? '0';
$harga        = $_POST['harga'] ?? '0';
$diskon       = $_POST['diskon'] ?? '0';
$h_beli       = $_POST['h_beli'] ?? '0';

// Pastikan MySQL lempar error sebagai Exception
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

try {
    // Mulai transaksi database
    $konek->begin_transaction();

    // 🔍 Cek apakah transaksi sudah ada
    $stmt_cek = $konek->prepare("SELECT id FROM transaksi WHERE id_transaksi = ?");
    $stmt_cek->bind_param("s", $id_transaksi);
    $stmt_cek->execute();
    $result_cek = $stmt_cek->get_result();

    // 🧾 Jika belum ada, buat transaksi baru
    if ($result_cek->num_rows == 0) {
        $stmt_insert_trans = $konek->prepare("
            INSERT INTO transaksi (
                id_transaksi,
                id_user,
                tanggal,
                status,
                ongkir,
                status_bayar,
                jam,
                penjualan
            ) VALUES (?, ?, CURDATE(), 'selesai', '0', 'sudah', CURTIME(), 'retail')
        ");
        $stmt_insert_trans->bind_param("ss", $id_transaksi, $id_user);
        $stmt_insert_trans->execute();
    }

    // 🧾 Tambahkan detail transaksi
    $stmt_detail = $konek->prepare("
        INSERT INTO transaksi_details (
            id_transaksi, produk, jumlah, harga, diskon, h_beli
        ) VALUES (?, ?, ?, ?, ?, ?)
    ");
    $stmt_detail->bind_param("ssdddd", $id_transaksi, $produk, $jumlah, $harga, $diskon, $h_beli);
    $stmt_detail->execute();

    // 📦 Kurangi stok produk
    $stmt_update_stok = $konek->prepare("
        UPDATE produk_details 
        SET stok = stok - ? 
        WHERE id_produk = ?
    ");
    $stmt_update_stok->bind_param("ds", $jumlah, $produk);
    $stmt_update_stok->execute();

    // 🗑️ Hapus data keranjang
    $stmt_delete_cart = $konek->prepare("DELETE FROM keranjang WHERE id_keranjang = ?");
    $stmt_delete_cart->bind_param("s", $id_keranjang);
    $stmt_delete_cart->execute();

    // ✅ Commit jika semua sukses
    $konek->commit();

    echo json_encode([
        "kode"  => 1,
        "pesan" => "Tambah transaksi sukses."
    ]);

} catch (mysqli_sql_exception $e) {
    // ❌ Rollback jika error
    $konek->rollback();
    echo json_encode([
        "kode"  => 0,
        "pesan" => "Oops! Gagal tambah transaksi: " . $e->getMessage()
    ]);
}
?>
