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
import android.view.View;
import android.widget.ImageView;

import com.yyaayyaatt.merchantstore.adapter.DataReferralRecylerAdapter;
import com.yyaayyaatt.merchantstore.adapter.PendapatanRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Referral;
import com.yyaayyaatt.merchantstore.model.ResponseReferral;
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

public class ReferralActivity extends AppCompatActivity {

    Context mContext;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    List<Referral> referrals = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    ImageView iv_nunggu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);


        mContext = ReferralActivity.this;
        mRecycler = findViewById(R.id.rv_referral);
        iv_nunggu = findViewById(R.id.iv_referral_nuggu);
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);
        iv_nunggu.setVisibility(View.GONE);

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));

        progressDialog.setMessage("Load Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getReferrals();
    }

    private void getReferrals() {
        referrals.clear();
        Call<ResponseReferral> getdata = mApiService.getReferrals();
        getdata.enqueue(new Callback<ResponseReferral>() {
            @Override
            public void onResponse(Call<ResponseReferral> call, Response<ResponseReferral> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        referrals = response.body().getResult();
                        if (!referrals.isEmpty()) {
                            iv_nunggu.setVisibility(View.GONE);
                            mAdapter = new DataReferralRecylerAdapter(mContext, referrals);
                            mRecycler.setAdapter(mAdapter);
                        } else {
                            iv_nunggu.setVisibility(View.VISIBLE);
                        }
                    } else {
                        iv_nunggu.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseReferral> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });
    }
}