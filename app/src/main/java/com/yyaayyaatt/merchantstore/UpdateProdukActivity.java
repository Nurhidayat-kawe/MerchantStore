package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProdukActivity extends AppCompatActivity {

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Kategori> kategoris = new ArrayList<>();
    List<String> katStrings = new ArrayList<>();
    List<Satuan> satuans = new ArrayList<>();
    List<String> satuanString = new ArrayList<>();
    Intent intent;

    EditText ed_nama, ed_desc, ed_beli, ed_jual;
    EditText txt_jml_beli,txt_jml_beli2, txt_jml_beli3, txt_harga_diskon, txt_harga_diskon2, txt_harga_diskon3, txt_jml_point;
    Spinner sp_kat, sp_satuan;
    AppCompatButton btn_simpan;
    String id_produk, nama, harga_jual, harga_beli, desc, kat, sat, user, harga_diskon, harga_diskon2, harga_diskon3, jml_beli, jml_beli2, jml_beli3;
    int jml_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_produk);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        intent = getIntent();
        mContext = UpdateProdukActivity.this;
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        ed_nama = findViewById(R.id.ed_update_produk_nama);
        ed_beli = findViewById(R.id.ed_update_produk_beli);
        ed_jual = findViewById(R.id.ed_update_produk_jual);
        ed_desc = findViewById(R.id.ed_update_produk_desc);
        sp_kat = findViewById(R.id.sp_update_produk_kat);
        txt_jml_beli = findViewById(R.id.txt_update_min_beli);
        txt_jml_beli2 = findViewById(R.id.txt_update_min_beli2);
        txt_jml_beli3 = findViewById(R.id.txt_update_min_beli3);
        txt_jml_point = findViewById(R.id.txt_jml_point);
        txt_harga_diskon = findViewById(R.id.txt_update_harga_diskon);
        txt_harga_diskon2 = findViewById(R.id.txt_update_harga_diskon2);
        txt_harga_diskon3 = findViewById(R.id.txt_update_harga_diskon3);
        sp_satuan = findViewById(R.id.sp_update_produk_satuan);
        btn_simpan = findViewById(R.id.btn_update_produk_simpan);

        ed_nama.setText(intent.getStringExtra("nama"));
        ed_beli.setText(intent.getStringExtra("beli"));
        ed_jual.setText(intent.getStringExtra("jual"));
        ed_desc.setText(intent.getStringExtra("desc"));
        txt_jml_point.setText(intent.getIntExtra("jml_point",0)+"");
        txt_jml_beli.setText(intent.getIntExtra("jml_beli", 0) + "");
        txt_jml_beli2.setText(intent.getIntExtra("jml_beli3", 0) + "");
        txt_jml_beli3.setText(intent.getIntExtra("jml_beli4", 0) + "");
        txt_harga_diskon.setText(intent.getStringExtra("harga_disc"));
        txt_harga_diskon2.setText(intent.getStringExtra("harga_disc3"));
        txt_harga_diskon3.setText(intent.getStringExtra("harga_disc4"));
        getKat();
        getSat();

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_produk = intent.getStringExtra("id_produk");
                nama = ed_nama.getText().toString();
                harga_jual = ed_jual.getText().toString();
                harga_beli = ed_beli.getText().toString();
                desc = ed_desc.getText().toString();
                kat = sp_kat.getSelectedItem().toString().substring(0, 2);
                sat = sp_satuan.getSelectedItem().toString().substring(0, 2);
                user = sharedPrefManager.getSpIdPengguna();
                harga_diskon = txt_harga_diskon.getText().toString();
                jml_beli = txt_jml_beli.getText().toString();
                harga_diskon2 = txt_harga_diskon2.getText().toString();
                jml_beli2 = txt_jml_beli2.getText().toString();
                harga_diskon3 = txt_harga_diskon3.getText().toString();
                jml_beli3 = txt_jml_beli3.getText().toString();
                jml_point = Integer.parseInt(txt_jml_point.getText().toString());
                if (nama.isEmpty()) {
                    Toast.makeText(mContext, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else if (harga_jual.isEmpty()) {
                    Toast.makeText(mContext, "Harga jual tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    simpan(id_produk, nama, harga_jual, harga_beli, desc, kat, sat, user, jml_beli, harga_diskon, jml_beli2, harga_diskon2, jml_beli3, harga_diskon3, jml_point);
                }
            }
        });
    }


    private void simpan(String id_produk, String nama, String harga_jual, String harga_beli, String desc, String kat, String sat, String user, String jml_beli, String harga_diskon, String jml_beli2, String harga_diskon2, String jml_beli3, String harga_diskon3, int jml_point) {
        Call<ResponseProduk> getdata = mApiService.updateProduk(id_produk, nama, kat, harga_beli, sat, desc, user, harga_jual, jml_beli, harga_diskon, jml_beli2, harga_diskon2, jml_beli3, harga_diskon3, jml_point);
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

                        sp_kat.setSelection(adapter.getPosition(intent.getStringExtra("kat")));
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
                        sp_satuan.setSelection(adapter.getPosition(intent.getStringExtra("satuan")));
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