package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.adapter.KeranjangTransaksiRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Keranjang;
import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.model.ResponseKeranjang;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransaksiFromKeranjangActivity extends AppCompatActivity {
    public BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Keranjang> keranjangs = new ArrayList<>();
    RecyclerView rv_keranjang;
    private RecyclerView.Adapter mAdapter;
    NumberFormat nf = NumberFormat.getIntegerInstance();
    double total= 0.0, sub_total=0;
    List<Produk> produks = new ArrayList<>();
    AppCompatButton btn_simpan;
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
    String produk, jumlah,harga,diskon,h_beli;
    int id_keranjang;
    TextView tv_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_from_keranjang);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2196F3"));
        actionBar.setBackgroundDrawable(colorDrawable);

        rv_keranjang = findViewById(R.id.rv_trans_keranjang);
        btn_simpan = findViewById(R.id.btn_trans_keranjang_pembelian);
        tv_total = findViewById(R.id.tv_trans_keranjang_beli_total);
        mContext = TransaksiFromKeranjangActivity.this;
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);
        rv_keranjang.setHasFixedSize(true);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv_keranjang.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));
        }else{
            rv_keranjang.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        }

        progressDialog.setMessage("Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getkeranjangs(sharedPrefManager.getSpIdPengguna());

        btn_simpan.setOnClickListener(View->{
            String id_trans = "TP"+sdf.format(cal.getTime());
            String user = sharedPrefManager.getSpIdPengguna();

            for (Keranjang k : keranjangs) {
                if(k.getJml_beli()!=0 && k.getJml()>= k.getJml_beli()) {
                total = Double.parseDouble(String.valueOf(k.getJml())) * Double.parseDouble(k.getHarga_disc());
                setHarga(k.getHarga_disc());
                double diskon = Double.parseDouble(k.getHarga_jual())-Double.parseDouble(k.getHarga_disc());
                setDiskon(""+diskon);
            }else{
                total = Double.parseDouble(String.valueOf(k.getJml())) * Double.parseDouble(k.getHarga_jual());
                setHarga(k.getHarga_jual());
                setDiskon("0");
            }
                setId_keranjang(k.getId_keranjang());
                setProduk(k.getId_produk());
                setJumlah(String.valueOf(k.getJml()));
                setH_beli(k.getHarga_beli());
                simpan(id_trans, getId_keranjang(), user, getProduk(), getJumlah(), getHarga(), getDiskon(),getH_beli());
            }
            Toast.makeText(mContext, "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext,HomeActivity.class));
        });
    }

    private void getkeranjangs(String key) {
        keranjangs.clear();
        Call<ResponseKeranjang> getdata = mApiService.getKeranjangById(key);
        getdata.enqueue(new Callback<ResponseKeranjang>() {
            @Override
            public void onResponse(Call<ResponseKeranjang> call, Response<ResponseKeranjang> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        keranjangs = response.body().getResult();
                        if (!keranjangs.isEmpty()) {
                            mAdapter = new KeranjangTransaksiRecylerAdapter(mContext, keranjangs);
                            rv_keranjang.setAdapter(mAdapter);
                            for (Keranjang k : keranjangs){
                                if(k.getJml_beli()!=0 && k.getJml()>= k.getJml_beli()) {
                                    total = Double.parseDouble(String.valueOf(k.getJml())) * Double.parseDouble(k.getHarga_disc());
                                    setHarga(k.getHarga_disc());
                                    double diskon = Double.parseDouble(k.getHarga_jual())-Double.parseDouble(k.getHarga_disc());
                                    setDiskon(""+diskon);
                                }else{
                                    total = Double.parseDouble(String.valueOf(k.getJml())) * Double.parseDouble(k.getHarga_jual());
                                    setHarga(k.getHarga_jual());
                                    setDiskon("0");
                                }
                                setId_keranjang(k.getId_keranjang());
                                setProduk(k.getId_produk());
                                setJumlah(String.valueOf(k.getJml()));
                                sub_total += total;
                            }
                            tv_total.setText(nf.format(sub_total));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseKeranjang> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t);
                progressDialog.dismiss();
            }
        });
    }

    private void simpan(String id_trans,int id_keranjang, String user, String produk, String jumlah, String harga, String diskon, String h_beli) {
        produks.clear();
        Call<ResponseTransaksi> getdata = mApiService.simpanPesananKeranjang(id_trans,id_keranjang,user,produk,jumlah,harga,diskon, h_beli);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        Log.i("insert to transaksi ",response.message());

//                        new MyFirebaseMessagingService().sendToTopic("Pesanan Baru","Ada Pesanan baru masuk nih");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaksi> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t);
            }
        });
    }


    public String getProduk() {
        return produk;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public int getId_keranjang() {
        return id_keranjang;
    }

    public void setId_keranjang(int id_keranjang) {
        this.id_keranjang = id_keranjang;
    }

    public String getH_beli() {
        return h_beli;
    }

    public void setH_beli(String h_beli) {
        this.h_beli = h_beli;
    }
}