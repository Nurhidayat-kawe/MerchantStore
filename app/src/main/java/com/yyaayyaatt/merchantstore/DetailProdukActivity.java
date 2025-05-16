package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.Upload.BaseResponse;
import com.yyaayyaatt.merchantstore.Upload.FileUtils;
import com.yyaayyaatt.merchantstore.Upload.UploadService;
import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.io.File;
import java.io.FileOutputStream;
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
    List<Produk> transaksis2 = new ArrayList<>();
    NumberFormat nf = NumberFormat.getCurrencyInstance();

    //upload
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private Uri uri;
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

        ib_foto_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
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


    private void choosePhoto() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DetailProdukActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);

        } else {
            openGallery();
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    private void uploadMultipart(File file) {
        int compressionRatio = 90; //1 == originalImage, 2 = 50% compression, 4=25% compress
        try {
            Bitmap bitmap = BitmapFactory.decodeFile (file.getPath ());
            bitmap.compress (Bitmap.CompressFormat.JPEG, compressionRatio, new FileOutputStream (file));
        }
        catch (Throwable t) {
            Log.e("ERROR", "Error compressing file." + t.toString ());
            t.printStackTrace ();
        }
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
            if (data != null) {
                uri = data.getData();
                String a = FileUtils.getPath(mContext, uri);
                System.out.println("URL >>>> " + a);

                if (uri != null) {
                    File file = FileUtils.getFile(mContext, uri);
                    imageName = file.getName();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                                new FileOutputStream(file));
                    } catch (Throwable t) {
                        Log.e("ERROR", "Error compressing file." + t.toString());
                        t.printStackTrace();
                    }

                    System.out.println("URL>>> " + file.getAbsoluteFile());
                    uploadMultipart(file);

                } else {
                    Toast.makeText(mContext, "You must choose the image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}