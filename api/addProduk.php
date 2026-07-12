<?php 
require_once 'koneksi.php'; 

// Ambil data POST dengan pengecekan (biar tidak undefined index)
$id_produk   = $_POST['id_produk'] ?? '';
$nama_produk = $_POST['nama_produk'] ?? '';
$id_kategori = $_POST['id_kategori'] ?? '';
$harga_beli  = $_POST['harga_beli'] ?? '';
$id_satuan   = $_POST['id_satuan'] ?? '';
$deskripsi   = $_POST['deskripsi'] ?? '';
$foto        = $_POST['foto'] ?? '';
$foto2       = $_POST['foto2'] ?? '';
$user        = $_POST['user'] ?? '';
$cabang      = $_POST['cabang'] ?? '';
$stok        = $_POST['stok'] ?? 0;
$jml_beli    = $_POST['jml_beli'] ?? 0;
$jml_beli2   = $_POST['jml_beli2'] ?? 0;
$jml_beli3   = $_POST['jml_beli3'] ?? 0;
$harga_disc  = $_POST['harga_disc'] ?? 0;
$harga_disc2 = $_POST['harga_disc2'] ?? 0;
$harga_disc3 = $_POST['harga_disc3'] ?? 0;
$harga_jual  = $_POST['harga_jual'] ?? 0;
$jml_point   = $_POST['jml_point'] ?? 0;
$foto  = ($foto === '0') ? '' : $foto;
$foto2 = ($foto2 === '0') ? '' : $foto2;

// Jalankan transaksi
mysqli_begin_transaction($konek);

try {
    // Query 1: insert ke tabel produk
    $stmt1 = $konek->prepare("
        INSERT INTO produk (
            id_produk, nama_produk, id_kategori, harga_beli, id_satuan,
            deskripsi, foto, foto2, user, created, updated, deleted, barcode, jml_point
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), CURDATE(), '0000-00-00', '', ?)
    ");
$stmt1->bind_param(
    "sssisssssi",
    $id_produk,
    $nama_produk,
    $id_kategori,
    $harga_beli,
    $id_satuan,
    $deskripsi,
    $foto,
    $foto2,
    $user,
    $jml_point
);
    $stmt1->execute();

    // Query 2: insert ke tabel produk_details
    $stmt2 = $konek->prepare("
        INSERT INTO produk_details (
            id_produk, stok, jml_beli, jml_beli2, jml_beli3,
            harga_disc, harga_disc2, harga_disc3, harga_jual, cabang
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    ");
    $stmt2->bind_param("siiiiiiiis",
        $id_produk, $stok, $jml_beli, $jml_beli2, $jml_beli3,
        $harga_disc, $harga_disc2, $harga_disc3, $harga_jual, $cabang
    );
    $stmt2->execute();

    // Jika semua berhasil, commit transaksi
    mysqli_commit($konek);

    $response = [
        "kode" => 1,
        "pesan" => "Tambah Produk Sukses."
    ];
} catch (mysqli_sql_exception $exception) {
    mysqli_rollback($konek);

    $response = [
        "kode" => 0,
        "pesan" => "Oops! Gagal tambah produk."
    ];
}

// Output JSON
header('Content-Type: application/json');
echo json_encode($response);
?>
