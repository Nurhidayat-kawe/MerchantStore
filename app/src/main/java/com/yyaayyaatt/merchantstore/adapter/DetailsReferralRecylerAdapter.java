package com.yyaayyaatt.merchantstore.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.DetailReferralActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.Referral;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsReferralRecylerAdapter extends RecyclerView.Adapter<DetailsReferralRecylerAdapter.MyHolder> {
    List<Referral> mList = null;
    Context ctx;


    public DetailsReferralRecylerAdapter(Context ctx, List<Referral> mList) {
        this.mList = mList;
        this.ctx = ctx;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_referral, parent, false);
        MyHolder holder = new MyHolder(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Format yang ingin ditampilkan
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");

        // Parsing String ke Date
        Date date = null;
        try {
            date = inputFormat.parse(mList.get(position).getTgl_daftar());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // Ubah Date ke String dengan format baru
        String formattedDate = outputFormat.format(date);
        holder.toko.setText(mList.get(position).getNama_referree());
        holder.tgl_daftar.setText("Tgl.Daftar: "+formattedDate);


        Glide.with(ctx)
                .load(UtilsApi.BASE_URL_API + "images/" + mList.get(position).getFoto_referre())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.img);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(ctx, DetailReferralActivity.class);
//                i.putExtra("id_user", mList.get(position).getId_user());
//                i.putExtra("nama", mList.get(position).getNama_referrer());
//                i.putExtra("jml_referree", mList.get(position).getJml_referree());
//                ctx.startActivity(i);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView toko, tgl_daftar;
        CircleImageView img;

        public MyHolder(View v) {
            super(v);

            toko = v.findViewById(R.id.tv_row_detail_reff_nama);
            tgl_daftar = v.findViewById(R.id.tv_row_detail_reff_tgl_daftar);
            img = v.findViewById(R.id.img_row_detail_reff);
        }

    }
}
