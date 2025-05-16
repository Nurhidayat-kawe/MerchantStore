package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.adapter.DetailPesananRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPesananPointActivity extends AppCompatActivity {
    Intent i;
    String status, status_bayar;
    double total=0, sub_total=0;
    CircleImageView img;
    TextView tv_toko, tv_tanggal, tv_jml_produk, tv_status, tv_status_bayar, tv_sub_total;
    AppCompatButton btn_proses, btn_siap, btn_batal, btn_bayar, btn_selesai, btn_cetak;
    NumberFormat nf = NumberFormat.getIntegerInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("E, dd MMM yyyy");
    Calendar cal = Calendar.getInstance();
    Date tgl = new Date();
    Date now = new Date();
    Context context;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    List<Transaksi> transaksis = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan_point);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2196F3"));
        actionBar.setBackgroundDrawable(colorDrawable);

        i = getIntent();
        context = DetailPesananPointActivity.this;
        status = i.getStringExtra("status");
        status_bayar = i.getStringExtra("status_bayar");
        img = findViewById(R.id.img_detail_pesanan);
//        tv_jml_produk = findViewById(R.id.tv_detail_pesanan_jumlah);
        tv_status = findViewById(R.id.tv_detail_pesanan_status);
        tv_status_bayar = findViewById(R.id.tv_detail_pesanan_status_bayar);
        tv_tanggal = findViewById(R.id.tv_detail_pesanan_tanggal);
        tv_toko = findViewById(R.id.tv_detail_pesanan_toko);
        tv_sub_total = findViewById(R.id.tv_det_pesanan_sub_total);

        setButton(status, status_bayar);

        mRecycler = findViewById(R.id.rv_detail_pesanan);
        progressDialog = new ProgressDialog(context);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(context);

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(context, 1, RecyclerView.VERTICAL, false));
        progressDialog.setMessage("...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getPesanan(i.getStringExtra("id_trans"));
        try {
            tgl = sdf2.parse(i.getStringExtra("tanggal"));
            now = cal.getTime();
            System.out.println(sdf2.format(now));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_toko.setText(i.getStringExtra("toko") + " (" + i.getStringExtra("nama") + ")");

//        if(sdf.format(tgl).compareTo(sdf2.format(now))==0) {
//            tv_tanggal.setText("Hari ini,"+i.getStringExtra("jam").substring(0,5)+" | "+i.getStringExtra("jml_produk")+" Produk");
//        }else if(sdf.format(tgl).compareTo(sdf2.format(now)) == -1) {
//            tv_tanggal.setText("Kemarin,"+i.getStringExtra("jam").substring(0,5)+" | "+i.getStringExtra("jml_produk")+" Produk");
//        }else{
        tv_tanggal.setText(sdf2.format(tgl) + "," + i.getStringExtra("jam").substring(0, 5));
//        }

        if (i.getStringExtra("status_bayar").equals("belum")) {
            tv_status_bayar.setText("Belum bayar");
            tv_status_bayar.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            tv_status_bayar.setText("Sudah bayar");
            tv_status_bayar.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        Glide.with(context)
                .load(UtilsApi.BASE_URL_API + "images/" + i.getStringExtra("foto"))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(img);
    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        setButton(status, status_bayar);
//        getPesanan(i.getStringExtra("id_trans"));
//    }

    private void getPesanan(String key) {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getPesananPointById(key);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    double sub_total=0;
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
                            mAdapter = new DetailPesananRecylerAdapter(context, transaksis);
                            mRecycler.setAdapter(mAdapter);
                        }
                        System.out.println("idtrans>>>"+transaksis.size());
                        for(int x=0; x < transaksis.size();x++){
                            total = Double.parseDouble(transaksis.get(x).getPoint()) * Double.parseDouble(transaksis.get(x).getJumlah());
                            System.out.println("Total>>>"+total);
                            sub_total += total;
                            System.out.println("subtotal>>>"+sub_total);
                        }
                        tv_sub_total.setText(nf.format(sub_total)+" Point");
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

    private void setButton(String status, String status_bayar) {
        Toast.makeText(this, "Status " + status, Toast.LENGTH_SHORT).show();
        if (status.equals("baru")) {
            tv_status.setText("Pesanan Baru");
        } else if (status.equals("proses")) {
            tv_status.setText("Sedang diproses");
        } else if (status.equals("batal")) {
            tv_status.setText("Pesanan Batal");
        } else if (status.equals("selesai")) {
            tv_status.setText("Selesai");
        } else if (status.equals("siap")) {
            tv_status.setText("siap");
        } else {
            tv_status.setText(status);
        }

        if (status_bayar.equals("sudah")) {
        }
    }

}