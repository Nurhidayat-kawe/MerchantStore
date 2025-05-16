package com.yyaayyaatt.merchantstore.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.PelangganBaruActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.Pelanggan;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PelangganBaruRecylerAdapter extends RecyclerView.Adapter<PelangganBaruRecylerAdapter.MyHolder> {
    List<Pelanggan> mList = null;
    Context ctx;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;


    public PelangganBaruRecylerAdapter(Context ctx, List<Pelanggan> mList) {
        this.mList = mList;
        this.ctx = ctx;
        progressDialog = new ProgressDialog(ctx);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(ctx);
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pelanggan_baru, parent, false);
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

        holder.btn_ya.setOnClickListener(view -> {
            new PelangganBaruActivity().updateStatus(mList.get(position).getId_user(), "aktif", mApiService);
            mList.remove(position);
            notifyDataSetChanged();
        });
        holder.btn_tidak.setOnClickListener(view -> {
            new PelangganBaruActivity().updateStatus(mList.get(position).getId_user(), "blokir", mApiService);
            mList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nama_toko, telp, alamat;
        AppCompatButton btn_ya, btn_tidak;
        CircleImageView img;

        public MyHolder(View v) {
            super(v);

            nama_toko = v.findViewById(R.id.tv_row_pelanggan_nama);
            telp = v.findViewById(R.id.tv_row_pelanggan_telp);
            alamat = v.findViewById(R.id.tv_row_pelanggan_alamat);
            img = v.findViewById(R.id.img_row_pelanggan);
            btn_ya = v.findViewById(R.id.btn_pelanggan_baru_konf);
            btn_tidak = v.findViewById(R.id.btn_pelanggan_baru_tolak);
        }

    }
}
