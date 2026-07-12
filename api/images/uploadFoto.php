<?php
header('Content-Type: application/json');

$response = ["success" => false, "message" => "Invalid request"];
$uploadDir = __DIR__ . "/"; // folder simpan gambar

// Pastikan folder ada
if (!file_exists($uploadDir)) {
    mkdir($uploadDir, 0777, true);
}

$action = $_POST['action'] ?? '';

/* =========================
   MULTIPART UPLOAD
========================= */
if ($action === "multipart") {

    if (!isset($_FILES["photo"])) {
        echo json_encode(["success" => false, "message" => "File tidak ditemukan"]);
        exit;
    }

    $fileError = $_FILES["photo"]["error"];
    $fileTmp   = $_FILES["photo"]["tmp_name"];

    // NAMA FILE DARI ANDROID (APA ADANYA)
    $fileName  = basename($_FILES["photo"]["name"]);
    $fileExt   = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));

    $allowed = ["jpg", "jpeg", "png", "gif", "webp"];

    if ($fileError !== UPLOAD_ERR_OK) {
        $response["message"] = "Upload error code: " . $fileError;

    } elseif (!in_array($fileExt, $allowed)) {
        $response["message"] = "Ekstensi file tidak diizinkan";

    } elseif (@getimagesize($fileTmp) === false) {
        $response["message"] = "Bukan file gambar yang valid";

    } else {
        // SANITASI NAMA FILE (WAJIB)
        $safeFileName = preg_replace('/[^a-zA-Z0-9._-]/', '_', $fileName);
        $targetPath = $uploadDir . $safeFileName;

        if (move_uploaded_file($fileTmp, $targetPath)) {
            $response["success"]  = true;
            $response["message"]  = "Upload berhasil";
            $response["filename"] = $safeFileName; // <- PENTING
        } else {
            $response["message"] = "Gagal menyimpan file";
        }
    }

    echo json_encode($response);
    exit;
}

/* =========================
   BASE64 UPLOAD (TETAP)
========================= */
if ($action === "base64") {

    $photo = $_POST['photo'] ?? '';
    if (!$photo) {
        echo json_encode(["success" => false, "message" => "Data base64 kosong"]);
        exit;
    }

    $photo = preg_replace('#^data:image/\w+;base64,#i', '', $photo);
    $photo = str_replace(' ', '+', trim($photo));

    $data = base64_decode($photo, true);
    if ($data === false) {
        echo json_encode(["success" => false, "message" => "Base64 tidak valid"]);
        exit;
    }

    // base64 tidak punya nama asli → pakai uniqid
    $fileName = uniqid("img_") . ".png";
    $filePath = $uploadDir . $fileName;

    if (file_put_contents($filePath, $data)) {
        echo json_encode([
            "success" => true,
            "message" => "Upload berhasil",
            "filename" => $fileName
        ]);
    } else {
        echo json_encode([
            "success" => false,
            "message" => "Gagal menulis file"
        ]);
    }
    exit;
}

echo json_encode($response);
?>
