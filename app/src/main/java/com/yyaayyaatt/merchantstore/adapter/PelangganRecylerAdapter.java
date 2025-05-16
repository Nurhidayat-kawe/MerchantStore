package com.yyaayyaatt.merchantstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.DetailPelangganActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.PelangganTop;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PelangganRecylerAdapter extends RecyclerView.Adapter<PelangganRecylerAdapter.MyHolder> {
    List<PelangganTop> mList = null;
    Context ctx;


    public PelangganRecylerAdapter(Context ctx, List<PelangganTop> mList) {
        this.mList = mList;
        this.ctx = ctx;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pelanggan, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        holder.nama_toko.setText(mList.get(position).getNama() + " (" + mList.get(position).getToko() + ")");
        holder.telp.setText(mList.get(position).getTelp());
        holder.alamat.setText(mList.get(position).getAlamat());

        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API + "images/" + mList.get(position).getFoto())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.img);

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
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nama_toko, telp, alamat;
        CircleImageView img;

        public MyHolder(View v) {
            super(v);

            nama_toko = v.findViewById(R.id.tv_row_pelanggan_nama);
            telp = v.findViewById(R.id.tv_row_pelanggan_telp);
            alamat = v.findViewById(R.id.tv_row_pelanggan_alamat);
            img = v.findViewById(R.id.img_row_pelanggan);
        }

    }
}
