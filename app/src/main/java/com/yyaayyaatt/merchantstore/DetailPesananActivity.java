package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.adapter.DetailPesananRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Point;
import com.yyaayyaatt.merchantstore.model.ResponsePoint;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.ResponseUsers;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPesananActivity extends AppCompatActivity {
    Intent i;
    String status, status_bayar;
    double total = 0;
    CircleImageView img;
    TextView tv_toko, tv_jumlah, tv_tanggal, tv_jml_produk, tv_status, tv_status_bayar, tv_sub_total;
    AppCompatButton btn_proses, btn_siap, btn_batal, btn_bayar, btn_selesai, btn_cetak;
    NumberFormat nf = NumberFormat.getIntegerInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("E, dd MMM yyyy");
    Calendar cal = Calendar.getInstance();
    Date tgl = new Date();
    Date now = new Date();
    Context context;
    double subTotal=0;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    List<Transaksi> transaksis = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    String nominal="";

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        i = getIntent();
        context = DetailPesananActivity.this;
        status = i.getStringExtra("status");
        status_bayar = i.getStringExtra("status_bayar");
        img = findViewById(R.id.img_detail_pesanan);
//        tv_jml_produk = findViewById(R.id.tv_detail_pesanan_jumlah);
        tv_status = findViewById(R.id.tv_detail_pesanan_status);
        tv_status_bayar = findViewById(R.id.tv_detail_pesanan_status_bayar);
        tv_tanggal = findViewById(R.id.tv_detail_pesanan_tanggal);
        tv_toko = findViewById(R.id.tv_detail_pesanan_toko);
        tv_jumlah = findViewById(R.id.tv_detail_pesanan_total);
        tv_sub_total = findViewById(R.id.tv_det_pesanan_sub_total);

        listenerButton(i.getIntExtra("id", 0));

        setButton(status, status_bayar);

        mRecycler = findViewById(R.id.rv_detail_pesanan);
        progressDialog = new ProgressDialog(context);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(context);

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(context, 1, RecyclerView.VERTICAL, false));
        progressDialog.setMessage("...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getPesanan(i.getStringExtra("id_trans"));
        String thn = i.getStringExtra("tanggal").substring(0,4);
        String bln = i.getStringExtra("tanggal").substring(5,7);
        String day = i.getStringExtra("tanggal").substring(8,10);

        tv_toko.setText(i.getStringExtra("toko") + " (" + i.getStringExtra("nama") + ")");
//        tv_jumlah.setText("Rp."+nf.format(Double.parseDouble(i.getStringExtra("total"))));
//        tv_sub_total.setText("Rp."+nf.format(Double.parseDouble(i.getStringExtra("total"))));

        tv_tanggal.setText(day+"-"+bln+"-"+thn + "," + i.getStringExtra("jam").substring(0, 5));
        //TODO Cek Bonus Point
        cekBonusPoint();
        if (i.getStringExtra("status_bayar").equals("belum")) {
            tv_status_bayar.setText("Belum bayar");
            tv_status_bayar.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            tv_status_bayar.setText("Sudah bayar");
            tv_status_bayar.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        Glide.with(context)
                .load(UtilsApi.BASE_URL_API + "images/" + i.getStringExtra("foto"))
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(img);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setButton(status, status_bayar);
        getPesanan(i.getStringExtra("id_trans"));
    }

    private void listenerButton(int id_trans) {
        btn_batal = findViewById(R.id.btn_batal);
        btn_proses = findViewById(R.id.btn_proses);
        btn_siap = findViewById(R.id.btn_siap);
        btn_bayar = findViewById(R.id.btn_bayar);
        btn_selesai = findViewById(R.id.btn_selesai);
        btn_cetak = findViewById(R.id.btn_det_pesanan_cetak);

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(id_trans, "batal");
            }
        });
        btn_proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(id_trans, "proses");
            }
        });
        btn_siap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(id_trans, "siap");
            }
        });
        btn_selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(id_trans, "selesai");
                System.out.println("Nominal>>>"+Double.parseDouble(getNominal().toString()));
                if(getSubTotal() >= Double.parseDouble(getNominal().toString())) {
                    updatePoint(""+i.getIntExtra("id_user",0),""+calculatePoints(getSubTotal()));
                }
            }
        });
        btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusBayar(id_trans, "sudah");
            }
        });
        btn_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CetakNotaTransActivity.class);
                intent.putExtra("id_trans", i.getStringExtra("id_trans"));
                startActivity(intent);
            }
        });

    }

    public int calculatePoints(double totalPurchase) {
        double rupiahPerPoint = Double.parseDouble(getNominal());
        return (int) Math.floor(totalPurchase / rupiahPerPoint);
    }

    private void getPesanan(String key) {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getPesananById(key);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
                            mAdapter = new DetailPesananRecylerAdapter(context, transaksis);
                            mRecycler.setAdapter(mAdapter);
                        }
                        double sub_total = 0;
                        for (int x = 0; x < transaksis.size(); x++) {
                            total = Double.parseDouble(transaksis.get(x).getHarga()) * Double.parseDouble(transaksis.get(x).getJumlah());
                            System.out.println("Total>>>" + total);
                            sub_total += total;
                            System.out.println("subtotal>>>" + sub_total);
                        }
                        setSubTotal(sub_total);
                        tv_sub_total.setText("Rp." + nf.format(sub_total));
                        tv_jumlah.setText("Rp." + nf.format(sub_total));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaksi> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    private void setButton(String status, String status_bayar) {
        Toast.makeText(this, "Status " + status, Toast.LENGTH_SHORT).show();
        if (status.equals("baru")) {
            tv_status.setText("Pesanan Baru");
            btn_siap.setVisibility(View.GONE);
            btn_selesai.setVisibility(View.GONE);
        } else if (status.equals("proses")) {
            tv_status.setText("Sedang diproses");
            btn_proses.setVisibility(View.GONE);
            btn_selesai.setVisibility(View.GONE);
            btn_siap.setVisibility(View.VISIBLE);
            btn_bayar.setVisibility(View.GONE);
        } else if (status.equals("batal")) {
            tv_status.setText("Pesanan Batal");
            btn_proses.setVisibility(View.GONE);
            btn_batal.setVisibility(View.GONE);
            btn_siap.setVisibility(View.GONE);
            btn_selesai.setVisibility(View.GONE);
            btn_bayar.setVisibility(View.GONE);
        } else if (status.equals("selesai")) {
            tv_status.setText("Selesai");
            btn_proses.setVisibility(View.GONE);
            btn_batal.setVisibility(View.GONE);
            btn_siap.setVisibility(View.GONE);
            btn_selesai.setVisibility(View.GONE);
            btn_bayar.setVisibility(View.GONE);
        } else if (status.equals("siap")) {
            tv_status.setText("siap");
            btn_proses.setVisibility(View.GONE);
            btn_batal.setVisibility(View.GONE);
            btn_siap.setVisibility(View.GONE);
            btn_selesai.setVisibility(View.VISIBLE);
            btn_bayar.setVisibility(View.GONE);
        } else {
            tv_status.setText(status);
        }

        if (status_bayar.equals("sudah")) {
            btn_bayar.setVisibility(View.GONE);
        }
    }

    private void updateStatusBayar(int id, String status) {
        Call<ResponseTransaksi> getdata = mApiService.updateStatusBayar(id, status);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        btn_bayar.setVisibility(View.GONE);
                        tv_status_bayar.setText("Sudah bayar");
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
                        setButton(status, "");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaksi> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
    private void updatePoint(String id_user, String jml_bonus_point) {
        Call<ResponseUsers> getdata = mApiService.updatePointUser(id_user, jml_bonus_point);
        getdata.enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        Toast.makeText(context, "Transaksi Selesai, Bonus point telah ditambahkan ke User.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUsers> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
    private void cekTransPertama(String id_user) {
        Call<ResponseTransaksi> getdata = mApiService.cekTransPertama(id_user);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        if(response.body().getResult().get(0).getTrans()==1){
                            
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseTransaksi> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
    private void cekBonusPoint() {
        Call<ResponsePoint> getdata = mApiService.cekBonusPoint();
        getdata.enqueue(new Callback<ResponsePoint>() {
            @Override
            public void onResponse(Call<ResponsePoint> call, Response<ResponsePoint> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        setNominal(response.body().getResult().get(0).getNominal());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePoint> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
}