package com.yyaayyaatt.merchantstore;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.yyaayyaatt.merchantstore.adapter.DataReferralRecylerAdapter;
import com.yyaayyaatt.merchantstore.adapter.DetailsReferralRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Referral;
import com.yyaayyaatt.merchantstore.model.ResponseReferral;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailReferralActivity extends AppCompatActivity {

    Intent i;
    int id_user = 0;

    Context mContext;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    List<Referral> referrals = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_referral);

        i=getIntent();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(i.getStringExtra("nama"));
        actionBar.setSubtitle("Referree: "+i.getStringExtra("jml_referree")+" user");

        id_user = i.getIntExtra("id_user",0);

        mContext = DetailReferralActivity.this;
        mRecycler = findViewById(R.id.rv_detail_referral);
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));

        progressDialog.setMessage("Load Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getReferrals(id_user);
    }

    private void getReferrals(int id_user) {
        referrals.clear();
        Call<ResponseReferral> getdata = mApiService.getReferralById(id_user);
        getdata.enqueue(new Callback<ResponseReferral>() {
            @Override
            public void onResponse(Call<ResponseReferral> call, Response<ResponseReferral> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        referrals = response.body().getResult();
                        if (!referrals.isEmpty()) {
                            mAdapter = new DetailsReferralRecylerAdapter(mContext, referrals);
                            mRecycler.setAdapter(mAdapter);
                        }
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