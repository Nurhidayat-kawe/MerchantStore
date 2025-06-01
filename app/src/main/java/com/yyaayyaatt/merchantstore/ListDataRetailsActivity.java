package com.yyaayyaatt.merchantstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyaayyaatt.merchantstore.adapter.ProdukRetailRecylerAdapter;
import com.yyaayyaatt.merchantstore.adapter.TransaksiRetailRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDataRetailsActivity extends AppCompatActivity {
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Transaksi> transaksis = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView tv_total;
    ImageView iv;
    NumberFormat nf = NumberFormat.getIntegerInstance();

    double total = 0;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_retails);
        mRecycler = findViewById(R.id.rv_list_data_retail);
        tv_total = findViewById(R.id.tv_list_data_total);
        iv = findViewById(R.id.iv_list_data_retail);
        mContext = ListDataRetailsActivity.this;
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        mRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));
        } else {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        }
        progressDialog.setMessage("Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getPendapatanHarian();
    }
    private void getPendapatanHarian() {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getPendapatanHarianRetail();
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
                            mAdapter = new TransaksiRetailRecylerAdapter(mContext, transaksis);
                            mRecycler.setAdapter(mAdapter);
                            for(Transaksi t : transaksis){
                                total =+ Double.parseDouble(t.getTotal());
                            }
                            setTotal(total);
                        }
                        if(getTotal()!=0) {
                            iv.setVisibility(View.VISIBLE);
                            tv_total.setVisibility(View.VISIBLE);
                            tv_total.setText("Rp. " + nf.format(getTotal()));
                        }else{
                            iv.setVisibility(View.GONE);
                            tv_total.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaksi> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });
    }
}