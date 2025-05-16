package com.yyaayyaatt.merchantstore;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.adapter.ProdukTopRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukTerlarisActivity extends AppCompatActivity {

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    Context mContext;
    List<Transaksi> transaksis2 = new ArrayList<>();
    private RecyclerView rv_produk;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk_terlaris);
        mContext = ProdukTerlarisActivity.this;
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);
        rv_produk = findViewById(R.id.rv_produk_terlaris);

        rv_produk.setHasFixedSize(true);
        rv_produk.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));

        getProdukTop();
    }

    private void getProdukTop() {
        transaksis2.clear();
        Call<ResponseTransaksi> getdata = mApiService.getProdukTop();
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        transaksis2 = response.body().getResult();
                        if (!transaksis2.isEmpty()) {
                            mAdapter = new ProdukTopRecylerAdapter(mContext, transaksis2);
                            rv_produk.setAdapter(mAdapter);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaksi> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
}