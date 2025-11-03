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
import com.yyaayyaatt.merchantstore.DetailPelangganActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.PelangganTop;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PelangganTopRecylerAdapter extends RecyclerView.Adapter<PelangganTopRecylerAdapter.MyHolder> {
    List<PelangganTop> mList = null;
    Context ctx;


    public PelangganTopRecylerAdapter(Context ctx, List<PelangganTop> mList) {
        this.mList = mList;
        this.ctx = ctx;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pelanggan_top, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        holder.toko.setText(mList.get(position).getToko() + " (" + mList.get(position).getNama() + ")");
        holder.jumlah.setText("Tot.Blanja: " + nf.format(Double.parseDouble(mList.get(position).getJumlah())));
        holder.trans.setText("Trans. " + mList.get(position).getJml_trans() + " kali");

        String foto = mList.get(position).getFoto();
        if (foto == null || foto.trim().isEmpty()) {
            holder.img.setImageResource(R.drawable.noimage);
        } else {
            Glide.with(ctx)
                    .load(UtilsApi.BASE_URL_API + "images/" + foto)
                    .timeout(4000)
                    .placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage)
                    .fallback(R.drawable.noimage)
                    .into(holder.img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DetailPelangganActivity.class);
                i.putExtra("id_user", mList.get(position).getId_user());
                i.putExtra("foto", mList.get(position).getFoto());
                i.putExtra("jml_trans", mList.get(position).getJml_trans());
                i.putExtra("jumlah", mList.get(position).getJumlah());
                i.putExtra("nama", mList.get(position).getNama());
                i.putExtra("toko", mList.get(position).getToko());
                i.putExtra("alamat", mList.get(position).getAlamat());
                i.putExtra("telp", mList.get(position).getTelp());
                i.putExtra("status", mList.get(position).getStatus());
                i.putExtra("tgl_daftar", mList.get(position).getTgl_daftar());
                i.putExtra("point", mList.get(position).getPoint());
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView toko, trans, jumlah;
        CircleImageView img;

        public MyHolder(View v) {
            super(v);

            toko = v.findViewById(R.id.tv_row_nama_toko_top);
            trans = v.findViewById(R.id.tv_row_trans_top);
            jumlah = v.findViewById(R.id.tv_row_total_belanja_top);
            img = v.findViewById(R.id.img_row_pelanggan_top);
        }

    }
}
