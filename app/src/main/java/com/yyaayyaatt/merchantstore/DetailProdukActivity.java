package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.Upload.BaseResponse;
import com.yyaayyaatt.merchantstore.Upload.UploadService;
import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProdukActivity extends AppCompatActivity {
    Intent i;
    String id;
    BaseApiService mApiService;
    Context mContext;
    TextView tv_detail_produk, tv_beli, tv_jual, tv_desc;
    ImageView img;
    ImageButton ib_foto_produk;
    CardView cardGrosir1, cardGrosir2, cardGrosir3;
    TextView tvMinBeli1, tvMinBeli2, tvMinBeli3;
    TextView tvHargaGrosir1, tvHargaGrosir2, tvHargaGrosir3;
    TextView tvHeaderGrosir, tvEmptyGrosir;
    LinearLayout linearGrosir;
    List<Produk> transaksis2 = new ArrayList<>();
    NumberFormat nf = NumberFormat.getCurrencyInstance();

    //upload
    private static final int PICK_IMAGE = 1;
    private static final String TYPE_1 = "multipart";
    private String imageName, id_produk;
    private UploadService uploadService;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        i = getIntent();
        mContext = DetailProdukActivity.this;
        mApiService = UtilsApi.getAPIService();
        img = findViewById(R.id.iv_detail_produk);
        ib_foto_produk = findViewById(R.id.ib_foto_produk);
        tv_detail_produk = findViewById(R.id.tv_detail_produk);
        tv_beli = findViewById(R.id.tv_detail_produk_harga_beli);
        tv_jual = findViewById(R.id.tv_detail_produk_harga_jual);
        tv_desc = findViewById(R.id.tv_detail_produk_desc);

        cardGrosir1 = findViewById(R.id.card_grosir_1);
        cardGrosir2 = findViewById(R.id.card_grosir_2);
        cardGrosir3 = findViewById(R.id.card_grosir_3);
        tvMinBeli1 = findViewById(R.id.tv_min_beli_1);
        tvMinBeli2 = findViewById(R.id.tv_min_beli_2);
        tvMinBeli3 = findViewById(R.id.tv_min_beli_3);
        tvHargaGrosir1 = findViewById(R.id.tv_harga_grosir_1);
        tvHargaGrosir2 = findViewById(R.id.tv_harga_grosir_2);
        tvHargaGrosir3 = findViewById(R.id.tv_harga_grosir_3);
        tvHeaderGrosir = findViewById(R.id.tv_header_grosir);
        tvEmptyGrosir = findViewById(R.id.tv_empty_grosir);
        linearGrosir = findViewById(R.id.linear_grosir);

        setupGrosirClick(cardGrosir1);
        setupGrosirClick(cardGrosir2);
        setupGrosirClick(cardGrosir3);

        ib_foto_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        id_produk = i.getStringExtra("id_produk");
        id = i.getStringExtra("id");
        progressDialog = ProgressDialog.show(mContext, "Load Data User",
                "Harap tunggu...", true, false);
        getProduk(id);
    }

    private void getProduk(String id) {
        transaksis2.clear();
        Call<ResponseProduk> getdata = mApiService.getProduk(id);
        getdata.enqueue(new Callback<ResponseProduk>() {
            @Override
            public void onResponse(Call<ResponseProduk> call, Response<ResponseProduk> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        transaksis2 = response.body().getResult();
                        if (!transaksis2.isEmpty()) {
                            tv_detail_produk.setText(transaksis2.get(0).getNama_produk());
                            id_produk = transaksis2.get(0).getId_produk();
                            Glide.with(mContext)
                                    .load(UtilsApi.BASE_URL_API + "images/" + transaksis2.get(0).getFoto())
                                    .placeholder(R.drawable.noimage)
                                    .error(R.drawable.noimage)
                                    .into(img);

                            tv_beli.setText("Harga Beli :\n" + nf.format(Double.parseDouble(transaksis2.get(0).getHarga_beli())));
                            tv_jual.setText("Harga Jual :\n" + nf.format(Double.parseDouble(transaksis2.get(0).getHarga_jual())));
                            tv_desc.setText(transaksis2.get(0).getDeskripsi());
                            populateGrosir(transaksis2.get(0));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProduk> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void updateFoto(String id_produk, String foto) {
        transaksis2.clear();//sharedPrefManager.getSpIdPengguna()
        Call<ResponseProduk> getdata = mApiService.updateFotoProduk(id_produk, foto);
        getdata.enqueue(new Callback<ResponseProduk>() {
            @Override
            public void onResponse(Call<ResponseProduk> call, Response<ResponseProduk> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        Glide.with(mContext).load(UtilsApi.BASE_URL_API + "images/" + foto).error(R.drawable.noimage).into(img);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProduk> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
                Toast.makeText(mContext, "Koneksi terputus...", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void populateGrosir(Produk p) {
        boolean hasAny = false;

        if (p.getJml_beli() > 0 && p.getHarga_disc() != null && !p.getHarga_disc().equals("0") && !p.getHarga_disc().isEmpty()) {
            tvMinBeli1.setText("Min. Pembelian: " + p.getJml_beli() + " " + p.getNama_satuan());
            tvHargaGrosir1.setText(nf.format(Double.parseDouble(p.getHarga_disc())));
            cardGrosir1.setVisibility(View.VISIBLE);
            hasAny = true;
            animateCard(cardGrosir1, 0);
        } else {
            cardGrosir1.setVisibility(View.GONE);
        }

        if (p.getJml_beli2() > 0 && p.getHarga_disc2() != null && !p.getHarga_disc2().equals("0") && !p.getHarga_disc2().isEmpty()) {
            tvMinBeli2.setText("Min. Pembelian: " + p.getJml_beli2() + " " + p.getNama_satuan());
            tvHargaGrosir2.setText(nf.format(Double.parseDouble(p.getHarga_disc2())));
            cardGrosir2.setVisibility(View.VISIBLE);
            hasAny = true;
            animateCard(cardGrosir2, 150);
        } else {
            cardGrosir2.setVisibility(View.GONE);
        }

        if (p.getJml_beli3() > 0 && p.getHarga_disc3() != null && !p.getHarga_disc3().equals("0") && !p.getHarga_disc3().isEmpty()) {
            tvMinBeli3.setText("Min. Pembelian: " + p.getJml_beli3() + " " + p.getNama_satuan());
            tvHargaGrosir3.setText(nf.format(Double.parseDouble(p.getHarga_disc3())));
            cardGrosir3.setVisibility(View.VISIBLE);
            hasAny = true;
            animateCard(cardGrosir3, 300);
        } else {
            cardGrosir3.setVisibility(View.GONE);
        }

        if (hasAny) {
            tvHeaderGrosir.setVisibility(View.VISIBLE);
            linearGrosir.setVisibility(View.VISIBLE);
            tvEmptyGrosir.setVisibility(View.GONE);
        } else {
            tvHeaderGrosir.setVisibility(View.GONE);
            linearGrosir.setVisibility(View.GONE);
            tvEmptyGrosir.setVisibility(View.VISIBLE);
        }
    }

    private void animateCard(View view, long delay) {
        view.setAlpha(0f);
        view.setTranslationY(30f);
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setStartDelay(delay)
                .start();
    }

    private void setupGrosirClick(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate()
                        .scaleX(0.92f).scaleY(0.92f)
                        .setDuration(80)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                v.animate()
                                        .scaleX(1f).scaleY(1f)
                                        .setDuration(80)
                                        .start();
                            }
                        })
                        .start();
            }
        });
    }

    public void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE);
    }

    private void uploadMultipart(File file) {
        RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo",
                file.getName(), photoBody);

        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), TYPE_1);

        uploadService = new UploadService();
        uploadService.uploadPhotoMultipart(action, photoPart, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();

                if (baseResponse != null) {
                    Toast.makeText(mContext, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    updateFoto(id_produk, file.getName());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                uploadFromUri(data.getData());
            } else {
                Toast.makeText(mContext, "Gagal memilih gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFromUri(Uri uri) {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            File cacheDir = getCacheDir();
            String fileName = "upload_" + System.currentTimeMillis() + ".jpg";
            final File tempFile = new File(cacheDir, fileName);

            inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                Toast.makeText(mContext, "Gagal membaca gambar", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                Toast.makeText(mContext, "Gagal decode gambar", Toast.LENGTH_SHORT).show();
                return;
            }

            fos = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();

            bitmap.recycle();

            imageName = tempFile.getName();
            uploadMultipart(tempFile);
        } catch (Exception e) {
            Log.e("debug", "Error uploading image", e);
            progressDialog.dismiss();
            Toast.makeText(mContext, "Gagal upload gambar", Toast.LENGTH_SHORT).show();
        } finally {
            try { if (inputStream != null) inputStream.close(); } catch (Exception ignored) {}
            try { if (fos != null) fos.close(); } catch (Exception ignored) {}
        }
    }
}