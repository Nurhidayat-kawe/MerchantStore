package com.yyaayyaatt.merchantstore.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.PelanggansActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.adapter.PelangganRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.PelangganTop;
import com.yyaayyaatt.merchantstore.model.ResponsePelangganTop;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PelangganFragment extends Fragment {
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    AppCompatButton btn_cari, btn_pelanggans;
    EditText ed_cari;
    Context mContext;
    List<PelangganTop> pelanggans = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private final SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pelanggan, container, false);
        setHasOptionsMenu(true);
        mRecycler = view.findViewById(R.id.rv_event);
        ed_cari = view.findViewById(R.id.ed_pelanggan_cari);
        btn_cari = view.findViewById(R.id.btn_pelanggan_cari);
        btn_pelanggans = view.findViewById(R.id.btn_pelanggans);
        mContext = view.getContext();
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        mRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));
        } else {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        }
        progressDialog.setMessage("Tunggu...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        getPelanggan();

        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPelanggans(ed_cari.getText().toString());
            }
        });

        btn_pelanggans.setOnClickListener(View -> {
            startActivity(new Intent(mContext, PelanggansActivity.class));
        });
        return view;
    }

    private void getPelanggan() {
        pelanggans.clear();
        Call<ResponsePelangganTop> getdata = mApiService.getPelangganAll();
        getdata.enqueue(new Callback<ResponsePelangganTop>() {
            @Override
            public void onResponse(Call<ResponsePelangganTop> call, Response<ResponsePelangganTop> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        pelanggans = response.body().getResult();
                        if (!pelanggans.isEmpty()) {
                            mAdapter = new PelangganRecylerAdapter(mContext, pelanggans);
                            mRecycler.setAdapter(mAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePelangganTop> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void getPelanggans(String key) {
        pelanggans.clear();
        Call<ResponsePelangganTop> getdata = mApiService.getPelangganBy(key);
        getdata.enqueue(new Callback<ResponsePelangganTop>() {
            @Override
            public void onResponse(Call<ResponsePelangganTop> call, Response<ResponsePelangganTop> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        pelanggans = response.body().getResult();
                        if (!pelanggans.isEmpty()) {
                            mAdapter = new PelangganRecylerAdapter(mContext, pelanggans);
                            mRecycler.setAdapter(mAdapter);
                        } else {
                            Toast.makeText(mContext, "Data tidak ditemukan!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePelangganTop> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });
    }
}