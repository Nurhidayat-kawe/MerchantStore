package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.model.ResponseUsers;
import com.yyaayyaatt.merchantstore.model.Users;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPelangganActivity extends AppCompatActivity {

    Intent i;
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    CircleImageView img;
    TextView tv_trans, tv_belanja, tv_nama, tv_toko, tv_alamat, tv_telp, tv_tgl, tv_aktif, tv_point;
    AppCompatButton btn_blokir, btn_aktif;
    Context ctx;

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    List<Users> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pelanggan);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        i = getIntent();

        ctx = DetailPelangganActivity.this;
        img = findViewById(R.id.iv_detail_pelanggan);
        tv_trans = findViewById(R.id.tv_detail_pelanggan_total_trans);
        tv_belanja = findViewById(R.id.tv_detail_pelanggan_total_belanja);
        tv_nama = findViewById(R.id.tv_detail_pelanggan_nama);
        tv_toko = findViewById(R.id.tv_detail_pelanggan_toko);
        tv_alamat = findViewById(R.id.tv_detail_pelanggan_alamat);
        tv_telp = findViewById(R.id.tv_detail_pelanggan_telp);
        tv_point = findViewById(R.id.tv_detail_pelanggan_point);
        tv_tgl = findViewById(R.id.tv_detail_pelanggan_tgl_daftar);
        tv_aktif = findViewById(R.id.tv_detail_pelanggan_aktif);
        btn_blokir = findViewById(R.id.btn_detail_pelanggan_blokir);
        btn_aktif = findViewById(R.id.btn_detail_pelanggan_aktif);

        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API + "images/" + i.getStringExtra("foto"))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(img);

        tv_trans.setText(i.getStringExtra("jml_trans"));
        tv_belanja.setText(nf.format(Double.parseDouble(i.getStringExtra("jumlah"))));
        tv_nama.setText(i.getStringExtra("nama"));
        tv_toko.setText(" (" + i.getStringExtra("toko") + ")");
        tv_alamat.setText(i.getStringExtra("alamat"));
        tv_telp.setText(i.getStringExtra("telp"));
        tv_tgl.setText(sdf.format(Date.valueOf(i.getStringExtra("tgl_daftar"))));
        tv_aktif.setText(i.getStringExtra("status"));
        tv_point.setText(i.getStringExtra("point"));

        progressDialog = new ProgressDialog(ctx);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(ctx);

        btn_aktif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusUser(i.getIntExtra("id_user", 0), "aktif");
            }
        });
        btn_blokir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusUser(i.getIntExtra("id_user", 0), "blokir");
            }
        });

    }

    private void updateStatusUser(int id_user, String stat_user) {
        Call<ResponseUsers> getdata = mApiService.updateStatusUser(id_user, stat_user);
        getdata.enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        Toast.makeText(ctx, stat_user + " User Berhasil.", Toast.LENGTH_SHORT).show();
                        tv_aktif.setText(stat_user);
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