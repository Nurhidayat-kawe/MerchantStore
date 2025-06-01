package com.yyaayyaatt.merchantstore.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransaksiRetailRecylerAdapter extends RecyclerView.Adapter<TransaksiRetailRecylerAdapter.MyHolder> {
    List<Transaksi> mList = null;
    Context ctx;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;


    public TransaksiRetailRecylerAdapter(Context ctx, List<Transaksi> mList) {
        this.mList = mList;
        this.ctx = ctx;
        progressDialog = new ProgressDialog(ctx);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(ctx);
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_data_retail, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("id", "ID"));
        double total = (Double.parseDouble(mList.get(position).getHarga()) * Double.parseDouble(mList.get(position).getJumlah())) - Double.parseDouble(mList.get(position).getDiskon());
        double diskon = 0;
        if (!mList.get(position).getDiskon().equals("0.00")) {
            holder.diskon.setText(" -(" + nf.format(Double.parseDouble(mList.get(position).getDiskon())) + ")");
        }
//        holder.id_trans.setText("#" + mList.get(position).getId_transaksi());
        holder.nama.setText(mList.get(position).getNama_produk());
        holder.harga.setText(nf.format(Double.parseDouble(mList.get(position).getHarga())));
        holder.jumlah.setText(mList.get(position).getJumlah() + "x ");
        holder.total.setText("Rp." + nf.format(total));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView nama, harga, jumlah, total, diskon;

        public MyHolder(View v) {
            super(v);

            nama = v.findViewById(R.id.tv_list_data_retail_nama);
            harga = v.findViewById(R.id.tv_list_data_retail_harga);
            jumlah = v.findViewById(R.id.tv_list_data_retail_jumlah);
            total = v.findViewById(R.id.tv_list_data_retail_total);
            diskon = v.findViewById(R.id.tv_list_data_retail_diskon);
        }

    }
}
