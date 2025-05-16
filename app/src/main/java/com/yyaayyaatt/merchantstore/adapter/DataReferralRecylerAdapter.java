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
import com.yyaayyaatt.merchantstore.DetailReferralActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.PelangganTop;
import com.yyaayyaatt.merchantstore.model.Referral;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataReferralRecylerAdapter extends RecyclerView.Adapter<DataReferralRecylerAdapter.MyHolder> {
    List<Referral> mList = null;
    Context ctx;


    public DataReferralRecylerAdapter(Context ctx, List<Referral> mList) {
        this.mList = mList;
        this.ctx = ctx;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_data_referral, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        NumberFormat nf = NumberFormat.getIntegerInstance();

        holder.toko.setText(mList.get(position).getNama_referrer());
        holder.jumlah.setText("Referree: " + nf.format(Double.parseDouble(mList.get(position).getJml_referree()))+" user");


        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API + "images/" + mList.get(position).getFoto())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DetailReferralActivity.class);
                i.putExtra("id_user", mList.get(position).getId_user());
                i.putExtra("nama", mList.get(position).getNama_referrer());
                i.putExtra("jml_referree", mList.get(position).getJml_referree());
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
        TextView toko, trans, jumlah;
        CircleImageView img;

        public MyHolder(View v) {
            super(v);

            toko = v.findViewById(R.id.tv_row_reff_nama);
            jumlah = v.findViewById(R.id.tv_row_reff_jml);
            img = v.findViewById(R.id.img_row_reff);
        }

    }
}
