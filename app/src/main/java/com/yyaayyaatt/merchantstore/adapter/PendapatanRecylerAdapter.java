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

public class PendapatanRecylerAdapter extends RecyclerView.Adapter<PendapatanRecylerAdapter.MyHolder> {
    List<Transaksi> mList = null;
    Context ctx;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;


    public PendapatanRecylerAdapter(Context ctx, List<Transaksi> mList) {
        this.mList = mList;
        this.ctx = ctx;
        progressDialog = new ProgressDialog(ctx);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(ctx);
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pendapatan, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("id", "ID"));
        double total = Double.parseDouble(mList.get(position).getTotal()) - Double.parseDouble(mList.get(position).getDiskon());
        double diskon = 0;
        if (!mList.get(position).getDiskon().equals("0.00")) {
            holder.diskon.setText(" -(" + nf.format(Double.parseDouble(mList.get(position).getDiskon())) + ")");
        }
        holder.untung.setText(nf.format(Double.parseDouble(mList.get(position).getUntung())) + "");
        holder.tanggal.setText(mList.get(position).getTanggal());
        holder.total.setText("" + nf.format(total));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tanggal, diskon, total, untung;

        public MyHolder(View v) {
            super(v);

            tanggal = v.findViewById(R.id.tv_row_pendapatan_per_tanggal);
            total = v.findViewById(R.id.tv_row_pendapatan_per_total);
            untung = v.findViewById(R.id.tv_row_pendapatan_per_untung);
            diskon = v.findViewById(R.id.tv_row_pendapatan_per_diskon);
        }

    }
}
