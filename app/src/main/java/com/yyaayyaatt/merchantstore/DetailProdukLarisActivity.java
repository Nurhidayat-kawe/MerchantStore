package com.yyaayyaatt.merchantstore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;

public class DetailProdukLarisActivity extends AppCompatActivity {

    ImageView img;
    TextView tv_nama, tv_desc, tv_jml_trans, tv_hrg_beli, tv_hrg_jual, tv_stok;
    Intent i;
    Context ctx;
    NumberFormat nf = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk_laris);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        img = findViewById(R.id.iv_detail_produk_laris);
        tv_nama = findViewById(R.id.tv_detail_produk_laris_nama);
        tv_desc = findViewById(R.id.tv_detail_produk_laris_desc);
        tv_hrg_beli = findViewById(R.id.tv_detail_produk_laris_harga_beli);
        tv_jml_trans = findViewById(R.id.tv_detail_produk_laris_jml);
        tv_hrg_jual = findViewById(R.id.tv_detail_produk_laris_jual);
        tv_stok = findViewById(R.id.tv_detail_produk_laris_stok);
        ctx = DetailProdukLarisActivity.this;

        i = getIntent();
        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API + "images/" + i.getStringExtra("foto"))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(img);

        tv_nama.setText(i.getStringExtra("nama_produk"));
        tv_desc.setText(i.getStringExtra("desc"));
        tv_hrg_beli.setText(nf.format(Double.parseDouble(i.getStringExtra("hrg_beli"))));
        tv_hrg_jual.setText(nf.format(Double.parseDouble(i.getStringExtra("hrg_jual"))));
        tv_stok.setText(i.getStringExtra("stok") + " Item");
        tv_jml_trans.setText("Terjual: " + i.getStringExtra("jumlah") + " Item");
    }
}