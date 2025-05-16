package com.yyaayyaatt.merchantstore.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.List;

public class DetailPesananRecylerAdapter extends RecyclerView.Adapter<DetailPesananRecylerAdapter.MyHolder> {
    List<Transaksi> mList = null;
    Context ctx;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;


    public DetailPesananRecylerAdapter(Context ctx, List<Transaksi> mList) {
        this.mList = mList;
        this.ctx = ctx;
        progressDialog = new ProgressDialog(ctx);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(ctx);
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_pesanan, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        double total = Double.parseDouble(mList.get(position).getHarga()) * Double.parseDouble(mList.get(position).getJumlah());
        holder.tv_produk.setText(mList.get(position).getNama_produk());
        holder.tv_harga.setText("Rp." + nf.format(Double.parseDouble(mList.get(position).getHarga())));
        holder.tv_jml_produk.setText(mList.get(position).getJumlah() + " X");
        holder.tv_total.setText(nf.format(total));

        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API + "images/" + mList.get(position).getFoto())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.img);

//        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(ctx, DetailPesananActivity.class);
//                i.putExtra("toko", mList.get(position).getToko());
//                i.putExtra("total", mList.get(position).getTotal());
//                i.putExtra("jam", mList.get(position).getJam());
//                i.putExtra("jml_produk", mList.get(position).getJml_produk());
//                i.putExtra("tanggal", mList.get(position).getTanggal());
//                i.putExtra("nama", mList.get(position).getNama());
//                i.putExtra("status", mList.get(position).getStatus());
//                i.putExtra("status_bayar", mList.get(position).getStatus_bayar());
//                i.putExtra("foto", mList.get(position).getFoto_user());
//                ctx.startActivity(i);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_produk, tv_harga, tv_jml_produk, tv_total;
        ImageView img;

        public MyHolder(View v) {
            super(v);

            tv_produk = v.findViewById(R.id.tv_row_det_pesanan_produk);
            tv_harga = v.findViewById(R.id.tv_row_det_pesanan_harga);
            tv_jml_produk = v.findViewById(R.id.tv_row_det_pesanan_jml_produk);
            img = v.findViewById(R.id.img_row_pesanan);
            tv_total = v.findViewById(R.id.tv_det_trans_total);
        }

    }
}
