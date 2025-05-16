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
import com.yyaayyaatt.merchantstore.DetailProdukActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.UbahStokActivity;
import com.yyaayyaatt.merchantstore.UpdateProdukActivity;
import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProdukRecylerAdapter extends RecyclerView.Adapter<ProdukRecylerAdapter.MyHolder> {
    List<Produk> mList = null;
    Context ctx;
    BaseApiService mApiService;


    public ProdukRecylerAdapter(Context ctx, List<Produk> mList) {
        this.mList = mList;
        this.ctx = ctx;
        mApiService = UtilsApi.getAPIService();
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_produk, parent, false);
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

        holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(ctx, holder.option);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_row_produk);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                            if(id==R.id.menu_row_produk_opname) {
                                //handle menu1 click
                                Intent i = new Intent(ctx, UbahStokActivity.class);
                                i.putExtra("id_produk", mList.get(position).getId_produk());
                                i.putExtra("nama", mList.get(position).getNama_produk());
                                i.putExtra("harga", mList.get(position).getHarga_jual());
                                i.putExtra("stok", mList.get(position).getStok());
                                ctx.startActivity(i);
                            }
                            else if(id==R.id.menu_row_produk_ubah) {
                                Intent in = new Intent(ctx, UpdateProdukActivity.class);
                                in.putExtra("id_produk", mList.get(position).getId_produk());
                                in.putExtra("nama", mList.get(position).getNama_produk());
                                in.putExtra("jual", mList.get(position).getHarga_jual());
                                in.putExtra("beli", mList.get(position).getHarga_beli());
                                in.putExtra("desc", mList.get(position).getDeskripsi());
                                in.putExtra("jml_beli", mList.get(position).getJml_beli());
                                in.putExtra("jml_point", mList.get(position).getJml_point());
                                in.putExtra("harga_disc", mList.get(position).getHarga_disc());
                                in.putExtra("satuan", mList.get(position).getId_satuan() + " - " + mList.get(position).getNama_satuan());
                                in.putExtra("kat", mList.get(position).getId_kategori() + " - " + mList.get(position).getNama_kategori());
                                ctx.startActivity(in);
                            }
                            else if(id==R.id.menu_row_produk_hapus){
                                hapusProduk(mList.get(position).getId_produk());
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(ctx, DetailProdukActivity.class);
            i.putExtra("id", mList.get(position).getId_produk());
            ctx.startActivity(i);
        });
    }

    private void hapusProduk(String id) {
        Call<ResponseProduk> getdata = mApiService.hapusProduk(id);
        getdata.enqueue(new Callback<ResponseProduk>() {
            @Override
            public void onResponse(Call<ResponseProduk> call, Response<ResponseProduk> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        Toast.makeText(ctx, "Hapus Produk Berhasil", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ctx, "Hapus Produk Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProduk> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nama, harga, stok, option;
        ImageView img;

        public MyHolder(View v) {
            super(v);

            nama = v.findViewById(R.id.tv_row_nama_produk);
            harga = v.findViewById(R.id.tv_row_harga_jual);
            stok = v.findViewById(R.id.tv_row_stok);
            img = v.findViewById(R.id.img_row_produk);
            option = v.findViewById(R.id.tv_row_produk_option);
        }

    }
}
