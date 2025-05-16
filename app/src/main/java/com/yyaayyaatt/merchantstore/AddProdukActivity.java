package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yyaayyaatt.merchantstore.model.Kategori;
import com.yyaayyaatt.merchantstore.model.ResponseKategori;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.model.ResponseSatuan;
import com.yyaayyaatt.merchantstore.model.Satuan;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProdukActivity extends AppCompatActivity {

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Kategori> kategoris = new ArrayList<>();
    List<String> katStrings = new ArrayList<>();
    List<Satuan> satuans = new ArrayList<>();
    List<String> satuanString = new ArrayList<>();

    ImageView iv_foto1, iv_foto2;
    ImageButton ib_foto1, ib_foto2;
    EditText ed_nama, ed_stok, ed_harga_satuan, ed_harga_beli,
            ed_harga_grosir, ed_min_beli, ed_desc, ed_jml_point;
    Spinner sp_kat, sp_satuan;
    AppCompatButton btn_simpan;

    String id_produk, nama, harga_satuan, harga_grosir, harga_beli, desc, kat, sat, user;
    int min_beli, stok, jml_point;
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produk);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        ed_nama = findViewById(R.id.ed_add_nama);
        ed_stok = findViewById(R.id.ed_add_stok);
        ed_harga_beli = findViewById(R.id.ed_add_harga_beli);
        ed_harga_satuan = findViewById(R.id.ed_add_harga);
        ed_harga_grosir = findViewById(R.id.ed_add_harga_grosir);
        ed_min_beli = findViewById(R.id.ed_add_min_beli);
        ed_desc = findViewById(R.id.ed_add_deskripsi);
        ed_jml_point = findViewById(R.id.ed_jml_point);
        sp_kat = findViewById(R.id.sp_add_kat);
        sp_satuan = findViewById(R.id.sp_add_satuan);
        btn_simpan = findViewById(R.id.btn_add_simpan);

        mContext = AddProdukActivity.this;
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        getKat();
        getSat();
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_produk = sdf.format(cal.getTime());
                nama = ed_nama.getText().toString();
                stok = Integer.parseInt(ed_stok.getText().toString());
                harga_grosir = ed_harga_grosir.getText().toString();
                harga_beli = ed_harga_beli.getText().toString();
                harga_satuan = ed_harga_satuan.getText().toString();
                desc = ed_desc.getText().toString();
                jml_point = Integer.parseInt(ed_jml_point.getText().toString());
                min_beli = Integer.parseInt(ed_min_beli.getText().toString());
                kat = sp_kat.getSelectedItem().toString().substring(0, 2);
                sat = sp_satuan.getSelectedItem().toString().substring(0, 2);
                user = sharedPrefManager.getSpIdPengguna();
                Toast.makeText(mContext, "user: " + user, Toast.LENGTH_SHORT).show();
                progressDialog.setMessage("Tunggu...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                simpan(id_produk, nama, stok, harga_grosir, harga_beli, harga_satuan, desc, min_beli, kat, sat, jml_point);
            }
        });

    }

    private void simpan(String id_produk, String nama, int stok, String harga_grosir, String harga_beli, String harga_satuan, String desc, int min_beli, String kat, String sat, int jml_point) {
        Call<ResponseProduk> getdata = mApiService.addProduk(id_produk, nama, kat, harga_beli, sat, desc, "0", "0", user, "0", stok, min_beli, harga_grosir, harga_satuan, jml_point);
        getdata.enqueue(new Callback<ResponseProduk>() {
            @Override
            public void onResponse(Call<ResponseProduk> call, Response<ResponseProduk> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        Toast.makeText(mContext, response.body().getmPesan(), Toast.LENGTH_SHORT).show();
                        finish();
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

    private void getKat() {
        kategoris.clear();
        Call<ResponseKategori> getdata = mApiService.getKat();
        getdata.enqueue(new Callback<ResponseKategori>() {
            @Override
            public void onResponse(Call<ResponseKategori> call, Response<ResponseKategori> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    kategoris = response.body().getResult();
                    if (response.body().getmKode().equals("1")) {
                        for (Kategori p : kategoris) {
                            katStrings.add(p.getId_kategori() + " - " + p.getNama_kategori());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, katStrings);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_kat.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseKategori> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void getSat() {
        satuans.clear();
        Call<ResponseSatuan> getdata = mApiService.getSatuan();
        getdata.enqueue(new Callback<ResponseSatuan>() {
            @Override
            public void onResponse(Call<ResponseSatuan> call, Response<ResponseSatuan> response) {
                if (response.isSuccessful()) {
                    satuans = response.body().getResult();
                    if (response.body().getmKode().equals("1")) {
                        for (Satuan p : satuans) {
                            satuanString.add(p.getId_satuan() + " - " + p.getNama_satuan());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, satuanString);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_satuan.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSatuan> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
}