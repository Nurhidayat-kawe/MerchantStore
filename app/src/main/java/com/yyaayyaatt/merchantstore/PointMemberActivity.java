package com.yyaayyaatt.merchantstore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.yyaayyaatt.merchantstore.adapter.DetailPesananRecylerAdapter;
import com.yyaayyaatt.merchantstore.adapter.PointMemberRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Pelanggan;
import com.yyaayyaatt.merchantstore.model.ResponsePelanggan;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointMemberActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    CircleImageView img;
    BaseApiService mApiService;
    List<Pelanggan> pelanggans = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_member);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);
        context = PointMemberActivity.this;
        mApiService = UtilsApi.getAPIService();
        mRecycler = findViewById(R.id.rv_point_member);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(context, 1, RecyclerView.VERTICAL, false));
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Memuat...");
        progressDialog.setCancelable(false);
        getPoint();
    }

    private void getPoint() {
        pelanggans.clear();
        Call<ResponsePelanggan> getdata = mApiService.getPointMembers();
        getdata.enqueue(new Callback<ResponsePelanggan>() {
            @Override
            public void onResponse(Call<ResponsePelanggan> call, Response<ResponsePelanggan> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        pelanggans = response.body().getResult();
                        if (!pelanggans.isEmpty()) {
                            mAdapter = new PointMemberRecylerAdapter(context, pelanggans);
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
}