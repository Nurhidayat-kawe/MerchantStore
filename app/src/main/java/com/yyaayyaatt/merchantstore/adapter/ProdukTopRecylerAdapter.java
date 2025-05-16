package com.yyaayyaatt.merchantstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.DetailProdukLarisActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.List;

public class ProdukTopRecylerAdapter extends RecyclerView.Adapter<ProdukTopRecylerAdapter.MyHolder> {
    List<Transaksi> mList = null;
    Context ctx;


    public ProdukTopRecylerAdapter(Context ctx, List<Transaksi> mList) {
        this.mList = mList;
        this.ctx = ctx;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_produk_top, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        NumberFormat nf = NumberFormat.getIntegerInstance();

        holder.nama.setText(mList.get(position).getNama_produk());
        holder.harga.setText("Rp" + nf.format(Double.parseDouble(mList.get(position).getHarga_beli())));
        holder.jml.setText("" + mList.get(position).getJumlah() + " Item");


        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API + "images/" + mList.get(position).getFoto())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DetailProdukLarisActivity.class);
                i.putExtra("nama_produk", mList.get(position).getNama_produk());
                i.putExtra("desc", mList.get(position).getDeskripsi());
                i.putExtra("hrg_beli", mList.get(position).getHarga_beli());
                i.putExtra("hrg_jual", mList.get(position).getHarga_jual());
                i.putExtra("stok", mList.get(position).getStok());
                i.putExtra("jumlah", mList.get(position).getJumlah());
                i.putExtra("foto", mList.get(position).getFoto());
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nama, harga, jml, option;
        ImageView img;

        public MyHolder(View v) {
            super(v);

            nama = v.findViewById(R.id.tv_row_nama_produktop);
            harga = v.findViewById(R.id.tv_row_harga_belitop);
            jml = v.findViewById(R.id.tv_row_terjual);
            img = v.findViewById(R.id.img_row_produktop);
        }

    }
}
