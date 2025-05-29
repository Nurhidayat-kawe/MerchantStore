package com.yyaayyaatt.merchantstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.yyaayyaatt.merchantstore.DetailProdukActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.UbahStokActivity;
import com.yyaayyaatt.merchantstore.UpdateProdukActivity;
import com.yyaayyaatt.merchantstore.model.Keranjang;
import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.model.ResponseKeranjang;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukRetailRecylerAdapter extends RecyclerView.Adapter<ProdukRetailRecylerAdapter.MyHolder> {
    List<Produk> mList = null;
    Context ctx;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;

    List<Produk> produks = new ArrayList<>();
    List<Keranjang> keranjangs = new ArrayList<>();

    public ProdukRetailRecylerAdapter(Context ctx, List<Produk> mList) {
        this.mList = mList;
        this.ctx = ctx;
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(ctx);
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_produk_retail, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        NumberFormat nf = NumberFormat.getIntegerInstance();

        holder.nama.setText(mList.get(position).getNama_produk());
        holder.harga.setText("Rp" + nf.format(Double.parseDouble(mList.get(position).getHarga_jual())));
        holder.stok.setText("Stok " + mList.get(position).getStok());


        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API + "images/" + mList.get(position).getFoto())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.img);

        holder.btn_plus.setOnClickListener(view -> {
            int x = Integer.parseInt(holder.jml.getText().toString());
            cekStokBarang(mList.get(position).getId_produk(),x,"plus",position,holder);
        });
        holder.btn_min.setOnClickListener(view -> {
            int x = Integer.parseInt(holder.jml.getText().toString());
            if(x>1) {
                cekStokBarang(mList.get(position).getId_produk(),x,"min",position,holder);
            }else{
                cekStokBarang(mList.get(position).getId_produk(),x,"hapus",position,holder);
            }
        });
        getkeranjang(sharedPrefManager.getSpIdPengguna(),mList.get(position).getId_produk(),holder,position);
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(ctx, DetailProdukActivity.class);
            i.putExtra("id", mList.get(position).getId_produk());
            ctx.startActivity(i);
        });
    }
    private void cekStokBarang(String id_produk,int stok_keranjang,String aksi, int position, MyHolder holder) {
        produks.clear();
        Call<ResponseProduk> getdata = mApiService.cekStokProduk(id_produk);
        getdata.enqueue(new Callback<ResponseProduk>() {
            @Override
            public void onResponse(Call<ResponseProduk> call, Response<ResponseProduk> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        produks = response.body().getResult();
                        if(aksi.equalsIgnoreCase("plus")) {
                            if (stok_keranjang < produks.get(0).getStok()) {
                                keranjang(sharedPrefManager.getSpIdPengguna(), mList.get(position).getId_produk(), aksi, position, holder);
                                notifyDataSetChanged();
//                            Toast.makeText(ctx, "Maaf, Stok tidak cukup...", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ctx, "Maaf, Stok tidak cukup...", Toast.LENGTH_LONG).show();
                            }
                        }else if (aksi.equalsIgnoreCase("min")){
                            keranjang(sharedPrefManager.getSpIdPengguna(), mList.get(position).getId_produk(), aksi, position, holder);
                            notifyDataSetChanged();
                        }else{
                            keranjang(sharedPrefManager.getSpIdPengguna(), mList.get(position).getId_produk(), aksi, position, holder);
                            notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProduk> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
    private void keranjang(String id_user,String id_produk,String aksi,int position,MyHolder holder) {
        Call<ResponseKeranjang> getdata = mApiService.addKeranjang(id_user,id_produk,aksi);
        getdata.enqueue(new Callback<ResponseKeranjang>() {
            @Override
            public void onResponse(Call<ResponseKeranjang> call, Response<ResponseKeranjang> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
//                        Toast.makeText(ctx, "Tambah Keranjang Berhasil", Toast.LENGTH_SHORT).show();

                        getkeranjang(sharedPrefManager.getSpIdPengguna(),mList.get(position).getId_produk(),holder,position);
                    } else {
                        Toast.makeText(ctx, "Tambah Keranjang Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseKeranjang> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
    private void getkeranjang(String id_user,String id_produk, MyHolder holder, int pos) {
        Call<ResponseKeranjang> getdata = mApiService.getKeranjang(id_user,id_produk);
        getdata.enqueue(new Callback<ResponseKeranjang>() {
            @Override
            public void onResponse(Call<ResponseKeranjang> call, Response<ResponseKeranjang> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        if (!response.body().getResult().isEmpty()) {
                            holder.jml.setText(""+response.body().getResult().get(pos).getJml());
                        }else{
                            holder.jml.setText("0");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseKeranjang> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nama, harga, stok, jml;
        ImageView img;
        MaterialButton btn_plus, btn_min;

        public MyHolder(View v) {
            super(v);

            nama = v.findViewById(R.id.tv_row_nama_produk_retail);
            harga = v.findViewById(R.id.tv_row_harga_jual_retail);
            stok = v.findViewById(R.id.tv_row_stok_retail);
            jml = v.findViewById(R.id.tv_jml_retail);
            img = v.findViewById(R.id.img_row_produk_retail);
            btn_plus = v.findViewById(R.id.btn_plus_retail);
            btn_min = v.findViewById(R.id.btn_min_retail);
        }

    }
}
