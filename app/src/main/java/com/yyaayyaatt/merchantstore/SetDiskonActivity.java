package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetDiskonActivity extends AppCompatActivity {

    TextView tv_nama, tv_harga;
    EditText txt_jml_beli, txt_harga_diskon;
    AppCompatButton btn_simpan;
    Intent intent;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_diskon);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        intent = getIntent();
        mContext = SetDiskonActivity.this;
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        intent = getIntent();
        tv_nama = findViewById(R.id.txt_set_diskon_nama);
        tv_harga = findViewById(R.id.txt_set_diskon_harga_jual);
        txt_jml_beli = findViewById(R.id.txt_set_diskon_min_beli);
        txt_harga_diskon = findViewById(R.id.txt_set_diskon_harga_diskon);
        btn_simpan = findViewById(R.id.btn_set_diskon_simpan);

        tv_nama.setText(intent.getStringExtra("nama"));
        tv_harga.setText(intent.getStringExtra("jual"));
        txt_jml_beli.setText("" + intent.getIntExtra("jml_beli", 0));
        txt_harga_diskon.setText(intent.getStringExtra("harga_disc"));

        btn_simpan.setOnClickListener(v -> {
            String id_produk = intent.getStringExtra("id_produk");
            String harga_diskon = txt_harga_diskon.getText().toString();
            String jml_beli = txt_jml_beli.getText().toString();
            if (harga_diskon.isEmpty()) {
                Toast.makeText(mContext, "Harga diskon tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else if (jml_beli.isEmpty()) {
                Toast.makeText(mContext, "Jumlah beli tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else {
                simpan(id_produk, jml_beli, harga_diskon);
            }
        });
    }

    private void simpan(String id_produk, String jml_beli, String harga_diskon) {
        Call<ResponseProduk> getdata = mApiService.setDiskon(id_produk, jml_beli, harga_diskon);
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
}