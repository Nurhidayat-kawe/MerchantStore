package com.yyaayyaatt.merchantstore.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.DetailPesananActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananRecylerAdapter extends RecyclerView.Adapter<PesananRecylerAdapter.MyHolder> {
    List<Transaksi> mList = null;
    Context ctx;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;


    public PesananRecylerAdapter(Context ctx, List<Transaksi> mList) {
        this.mList = mList;
        this.ctx = ctx;
        progressDialog = new ProgressDialog(ctx);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(ctx);
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pesanan, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        NumberFormat nf = NumberFormat.getIntegerInstance();

           String thn = mList.get(position).getTanggal().substring(0,4);
            String bln = mList.get(position).getTanggal().substring(5,7);
            String day = mList.get(position).getTanggal().substring(8,10);
        System.out.println("tanggal>>>"+thn+"-"+day+"-"+bln);

        holder.nama_user.setText(mList.get(position).getToko());

        holder.tgl.setText(day+"-"+bln+"-"+thn + "," + mList.get(position).getJam().substring(0, 5));

        if (mList.get(position).getStatus_bayar().equals("belum")) {
            holder.bayar.setText("Belum bayar");
            holder.bayar.setTextColor(ContextCompat.getColor(ctx, R.color.red));
        } else {
            holder.bayar.setText("Sudah bayar");
            holder.bayar.setTextColor(ContextCompat.getColor(ctx, R.color.green));
        }

        if (mList.get(position).getStatus().equals("baru")) {
            holder.status.setText(mList.get(position).getStatus());
            holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.btn_blue));
        } else if (mList.get(position).getStatus().equals("proses")) {
            holder.status.setText(mList.get(position).getStatus());
            holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.btn_yellow));
        } else if (mList.get(position).getStatus().equals("siap")) {
            holder.status.setText(mList.get(position).getStatus());
            holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.purple_700));
        } else if (mList.get(position).getStatus().equals("batal")) {
            holder.status.setText(mList.get(position).getStatus());
            holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.red));
        } else if (mList.get(position).getStatus().equals("selesai")) {
            holder.status.setText(mList.get(position).getStatus());
            holder.status.setTextColor(ContextCompat.getColor(ctx, R.color.green));
        }

        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API + "images/" + mList.get(position).getFoto_user())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.img);

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DetailPesananActivity.class);
                i.putExtra("id", mList.get(position).getId());
                i.putExtra("id_trans", mList.get(position).getId_transaksi());
                i.putExtra("toko", mList.get(position).getToko());
                i.putExtra("total", mList.get(position).getTotal());
                i.putExtra("jam", mList.get(position).getJam());
                i.putExtra("jml_produk", mList.get(position).getJml_produk());
                i.putExtra("tanggal", mList.get(position).getTanggal());
                i.putExtra("nama", mList.get(position).getNama());
                i.putExtra("status", mList.get(position).getStatus());
                i.putExtra("status_bayar", mList.get(position).getStatus_bayar());
                i.putExtra("foto", mList.get(position).getFoto_user());
                i.putExtra("id_user", mList.get(position).getId_user());
                ctx.startActivity(i);
            }
        });
    }

    private void updateStatusBayar(int id, String status, int pos) {
        Call<ResponseTransaksi> getdata = mApiService.updateStatusBayar(id, status);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaksi> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void updateStatus(int id, String status) {
        Call<ResponseTransaksi> getdata = mApiService.updateStatusTrans(id, status);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaksi> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nama_user, tgl, status, bayar;
        AppCompatButton btn_detail;
        ImageView img;

        public MyHolder(View v) {
            super(v);

            nama_user = v.findViewById(R.id.tv_row_pesanan_user);
            status = v.findViewById(R.id.tv_row_pesanan_status);
            bayar = v.findViewById(R.id.tv_row_pemesanan_bayar);
            tgl = v.findViewById(R.id.tv_row_pesanan_tanggal);
            img = v.findViewById(R.id.img_row_pesanan);
            btn_detail = v.findViewById(R.id.btn_row_pesanan_detail);
        }

    }
}
