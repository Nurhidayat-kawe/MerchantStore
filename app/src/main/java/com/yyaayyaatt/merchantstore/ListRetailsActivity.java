package com.yyaayyaatt.merchantstore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.yyaayyaatt.merchantstore.adapter.ProdukRecylerAdapter;
import com.yyaayyaatt.merchantstore.adapter.ProdukRetailRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Kategori;
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

public class ListRetailsActivity extends AppCompatActivity {

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Produk> produks = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    AppCompatButton btn_lanjut;

    private int currentPage = 1;
    private boolean isLastPage = false;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_retails);
        mRecycler = findViewById(R.id.rv_retail);
        btn_lanjut = findViewById(R.id.btn_retail_lanjut);
        mContext = ListRetailsActivity.this;
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
        getProduks("", "Retail");

        btn_lanjut.setOnClickListener(v -> {
            startActivity(new Intent(mContext, TransaksiFromKeranjangActivity.class));
        });
    }

    private void getProduks(String key, String kat) {
        produks.clear();
        Call<ResponseProduk> getdata = mApiService.getProduksRetail(key, kat);
        getdata.enqueue(new Callback<ResponseProduk>() {
            @Override
            public void onResponse(Call<ResponseProduk> call, Response<ResponseProduk> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        produks = response.body().getResult();
                        if (!produks.isEmpty()) {
                            mAdapter = new ProdukRetailRecylerAdapter(mContext, produks);
                            mRecycler.setAdapter(mAdapter);
                        }
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getProduks("", "Retail");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_retail, menu);
        // Inisialisasi SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Atur hint dan listener
        searchView.setQueryHint("Cari produk...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Pencarian saat tombol submit ditekan (Enter)
                getProduks(query,"Retail");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Pencarian real-time saat teks berubah
                getProduks(newText,"Retail");
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_list_retail) {
            // Aksi untuk search
            startActivity(new Intent(mContext, ListDataRetailsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}