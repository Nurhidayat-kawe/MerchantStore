package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.adapter.PelangganBaruRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Pelanggan;
import com.yyaayyaatt.merchantstore.model.ResponsePelanggan;
import com.yyaayyaatt.merchantstore.model.ResponseUsers;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PelangganBaruActivity extends AppCompatActivity {
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Pelanggan> pelanggans = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan_baru);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        mRecycler = findViewById(R.id.rv_pelanggan_baru);
        mContext = PelangganBaruActivity.this;
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);
        mRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));
        } else {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        }
        progressDialog.setMessage("Tunggu...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        getPelanggan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPelanggan();
    }

    private void getPelanggan() {
        pelanggans.clear();
        Call<ResponsePelanggan> getdata = mApiService.getPelangganMenunggu();
        getdata.enqueue(new Callback<ResponsePelanggan>() {
            @Override
            public void onResponse(Call<ResponsePelanggan> call, Response<ResponsePelanggan> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        pelanggans = response.body().getResult();
                        if (!pelanggans.isEmpty()) {
                            mAdapter = new PelangganBaruRecylerAdapter(mContext, pelanggans);
                            mRecycler.setAdapter(mAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePelanggan> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    public void updateStatus(int id, String status, BaseApiService mApiService) {
        Call<ResponseUsers> getdata = mApiService.updateStatusUser(id, status);
        getdata.enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUsers> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
}