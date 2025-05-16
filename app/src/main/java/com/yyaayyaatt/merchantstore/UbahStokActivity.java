package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UbahStokActivity extends AppCompatActivity {
    TextView tv_nama, tv_harga, tv_stok;
    EditText ed_stok_baru;
    AppCompatButton btn_simpan;

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Produk> produks = new ArrayList<>();
    String id_produk;
    int stok_baru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_stok);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        mContext = UbahStokActivity.this;
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);
        Intent i = getIntent();

        tv_nama = findViewById(R.id.tv_ubah_stok_nama);
        tv_harga = findViewById(R.id.tv_ubah_stok_harga);
        tv_stok = findViewById(R.id.tv_ubah_stok_sekarang);
        ed_stok_baru = findViewById(R.id.ed_ubah_stok_baru);
        btn_simpan = findViewById(R.id.btn_ubah_stok_simpan);

        tv_nama.setText(i.getStringExtra("nama"));
        tv_harga.setText(i.getStringExtra("harga"));
        tv_stok.setText("" + i.getIntExtra("stok", 0));
        id_produk = i.getStringExtra("id_produk");

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stok_baru = Integer.parseInt(ed_stok_baru.getText().toString());

                progressDialog.setMessage("Tunggu...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                simpan(id_produk, stok_baru);
            }
        });
    }

    private void simpan(String id_produk, int stok_baru) {
        Call<ResponseProduk> getdata = mApiService.opname(id_produk, stok_baru);
        getdata.enqueue(new Callback<ResponseProduk>() {
            @Override
            public void onResponse(Call<ResponseProduk> call, Response<ResponseProduk> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        Toast.makeText(mContext, response.body().getmPesan(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mContext, response.body().getmPesan(), Toast.LENGTH_SHORT).show();
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
}