package com.yyaayyaatt.merchantstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.DetailProdukActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.Keranjang;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class KeranjangTransaksiRecylerAdapter extends RecyclerView.Adapter<KeranjangTransaksiRecylerAdapter.MyHolder> {
    List<Keranjang> mList = null;
    Context ctx;
    SharedPrefManager sharedPrefManager;

    BaseApiService mApiService;
    List<Keranjang> produks = new ArrayList<>();

    public KeranjangTransaksiRecylerAdapter(Context ctx, List<Keranjang> mList) {
        this.mList = mList;
        this.ctx = ctx;
        sharedPrefManager = new SharedPrefManager(ctx);
        mApiService = UtilsApi.getAPIService();
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_keranjang_transaksi, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        holder.nama.setText(mList.get(position).getNama_produk());
        if(mList.get(position).getJml_beli()!=0 && mList.get(position).getJml()>= mList.get(position).getJml_beli()) {
            holder.harga.setText("Rp" + nf.format(Double.parseDouble(mList.get(position).getHarga_disc())));
            holder.harga_diskon.setText("Rp" + nf.format(Double.parseDouble(mList.get(position).getHarga_jual())));
            holder.harga_diskon.setPaintFlags(holder.harga_diskon.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.harga.setText("Rp" + nf.format(Double.parseDouble(mList.get(position).getHarga_jual())));
            holder.harga_diskon.setText("");
        }
        holder.jml.setText("x"+mList.get(position).getJml());

        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API +"images/"+mList.get(position).getFoto())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(ctx, DetailProdukActivity.class);
            i.putExtra("id", mList.get(position).getId_produk());
            ctx.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nama,harga, jml,harga_diskon;
        ImageView img;

        public MyHolder(View v) {
            super(v);

            nama = v.findViewById(R.id.tv_row_keranjang_trans_nama);
            harga = v.findViewById(R.id.tv_row_keranjang_trans_harga);
            harga_diskon = v.findViewById(R.id.tv_row_keranjang_trans_harga_diskon);
            jml = v.findViewById(R.id.tv_row_keranjang_trans_jml);
            img = v.findViewById(R.id.iv_row_keranjang_trans);
        }

    }
}
