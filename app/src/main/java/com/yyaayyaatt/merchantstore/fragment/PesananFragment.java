package com.yyaayyaatt.merchantstore.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.adapter.PesananPointRecylerAdapter;
import com.yyaayyaatt.merchantstore.adapter.PesananRecylerAdapter;
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

public class PesananFragment extends Fragment {
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    AppCompatButton btn_baru, btn_siap, btn_proses, btn_batal, btn_semua, btn_point;
    Context mContext;
    List<Transaksi> transaksis = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private final SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesanan, container, false);
        mRecycler = view.findViewById(R.id.rv_pesanan);
        btn_baru = view.findViewById(R.id.btn_pesanan_baru);
        btn_siap = view.findViewById(R.id.btn_pesanan_siap);
        btn_proses = view.findViewById(R.id.btn_pesanan_proses);
        btn_batal = view.findViewById(R.id.btn_pesanan_batal);
        btn_semua = view.findViewById(R.id.btn_pesanan_semua);
        btn_point = view.findViewById(R.id.btn_pesanan_point);

        mContext = view.getContext();
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        mRecycler.setHasFixedSize(true);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        } else {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false));
        }
        progressDialog.setMessage("Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        setHasOptionsMenu(true);
        getPesanan("semua");

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Memuat Transaksi Batal...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getPesanan("Batal");
            }
        });
        btn_baru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Memuat transaksi terbaru...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getPesanan("Baru");
            }
        });
        btn_proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Memuat transaksi yang sedang di proses...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getPesanan("Proses");
            }
        });
        btn_siap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Memuat transaksi yang siap diambil...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getPesanan("Siap");
            }
        });
        btn_semua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Memuat semua transaksi...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getPesanan("semua");
            }
        });
        btn_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Memuat semua transaksi...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                getPesananPoint("semua_admin");
            }
        });
        return view;
    }

    private void getPesanan() {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getPesanan();
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
                            mAdapter = new PesananRecylerAdapter(mContext, transaksis);
                            mRecycler.setAdapter(mAdapter);
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

    private void getPesanan(String key) {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getPesanan(key);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
                            mAdapter = new PesananRecylerAdapter(mContext, transaksis);
                            mRecycler.setAdapter(mAdapter);
                        } else {
                            transaksis = new ArrayList<>();
                            mAdapter = new PesananRecylerAdapter(mContext, transaksis);
                            mRecycler.setAdapter(mAdapter);
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

    private void getPesananPoint(String key) {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getPesananPoint(key,"");
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
                            mAdapter = new PesananPointRecylerAdapter(mContext, transaksis);
                            mRecycler.setAdapter(mAdapter);
                        } else {
                            transaksis = new ArrayList<>();
                            mAdapter = new PesananPointRecylerAdapter(mContext, transaksis);
                            mRecycler.setAdapter(mAdapter);
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

//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        //Memanggil/Memasang menu item pada toolbar dari layout menu_bar.xml
//        inflater.inflate(R.menu.menu_event, menu);
//        MenuItem searchItem = menu.findItem(R.id.menu_search_event);
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//
//            queryTextListener = new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    Log.i("onQueryTextChange", newText);
//                    if(newText.isEmpty()){
//                        getEvent();
//                    }
//                    return true;
//                }
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Log.i("onQueryTextSubmit", query);
//
//                    getEvent(query);
//                    return true;
//                }
//            };
//            searchView.setOnQuseryTextListener(queryTextListener);
//        }
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        getPesanan("semua");
//    }
}