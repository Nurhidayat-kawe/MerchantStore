package com.yyaayyaatt.merchantstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.Transaksi;

import java.text.NumberFormat;
import java.util.List;

public class ProdukTransaksiRecylerAdapter extends RecyclerView.Adapter<ProdukTransaksiRecylerAdapter.MyHolder> {
    List<Transaksi> mList = null;
    Context ctx;


    public ProdukTransaksiRecylerAdapter(Context ctx, List<Transaksi> mList) {
        this.mList = mList;
        this.ctx = ctx;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_produk_trans, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        NumberFormat nf = NumberFormat.getIntegerInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar cal = Calendar.getInstance();
//        Date tgl = new Date();
//        Date now =  new Date();
//        try {
//            tgl = sdf.parse(mList.get(position).getTanggal());
//            now = cal.getTime();
//            System.out.println(sdf2.format(now));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        double total = Double.parseDouble(mList.get(position).getJumlah()) * Double.parseDouble(mList.get(position).getHarga());
        holder.barang.setText(mList.get(position).getNama_produk());
        holder.harga.setText(nf.format(Double.parseDouble(mList.get(position).getHarga())));
        holder.qty.setText("" + mList.get(position).getJumlah());
        holder.total.setText(nf.format(total));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView barang, harga, qty, total;

        public MyHolder(View v) {
            super(v);

            barang = v.findViewById(R.id.tv_row_trans_barang);
            harga = v.findViewById(R.id.tv_row_trans_harga);
            qty = v.findViewById(R.id.tv_row_trans_qty);
            total = v.findViewById(R.id.tv_row_trans_total);
        }

    }
}
