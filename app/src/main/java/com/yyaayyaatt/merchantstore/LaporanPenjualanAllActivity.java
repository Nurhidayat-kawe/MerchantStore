package com.yyaayyaatt.merchantstore;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.adapter.PedapatanHarianRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanPenjualanAllActivity extends AppCompatActivity {

    ImageButton btn_pick_1, btn_pick_2;
    TextView tv_date_awal, tv_date_ahir;
    AppCompatButton btn_cari;
    Context context;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    List<Transaksi> transaksis = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    TextView tv_lap_penjualan_total;
    NumberFormat nf = NumberFormat.getIntegerInstance(new Locale("id", "ID"));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_penjualan_all);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        context = LaporanPenjualanAllActivity.this;
        btn_pick_1 = findViewById(R.id.btn_lap_penjualan_tgl_awal);
        btn_pick_2 = findViewById(R.id.btn_lap_penjualan_tgl_ahir);
        tv_date_awal = findViewById(R.id.tv_lap_penjualan_tgl_awal);
        tv_date_ahir = findViewById(R.id.tv_lap_penjualan_tgl_ahir);
        btn_cari = findViewById(R.id.btn_lap_penjualan_cari);
        mRecycler = findViewById(R.id.rv_lap_penjualan_all);
        tv_lap_penjualan_total = findViewById(R.id.tv_lap_penjualan_all_total);
        progressDialog = new ProgressDialog(context);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(context);

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(context, 1, RecyclerView.VERTICAL, false));
        progressDialog.setMessage("Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final Calendar cal = Calendar.getInstance();
        int year2 = cal.get(Calendar.YEAR);
        int month2 = cal.get(Calendar.MONTH);
        int day2 = cal.get(Calendar.DAY_OF_MONTH);
        tv_date_awal.setText(year2 + "-" + (String.format("%02d", month2 + 1)) + "-" + String.format("%02d", day2));
        tv_date_ahir.setText(year2 + "-" + (String.format("%02d", month2 + 1)) + "-" + String.format("%02d", day2));
        getPendapatan(cal.toString(), cal.toString());

        btn_pick_1.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            // on below line we are getting
            // our day, month and year.
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // on below line we are creating a variable for date picker dialog.
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    // on below line we are passing context.
                    context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // on below line we are setting date to our text view.
//                            tv_date_awal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            tv_date_awal.setText(year + "-" + (String.format("%02d", monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));

                        }
                    },
                    // on below line we are passing year,
                    // month and day for selected date in our date picker.
                    year, month, day);
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog.show();
        });

        btn_pick_2.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();

            // on below line we are getting
            // our day, month and year.
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // on below line we are creating a variable for date picker dialog.
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    // on below line we are passing context.
                    context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // on below line we are setting date to our text view.
//                            tv_date_ahir.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            tv_date_ahir.setText(year + "-" + (String.format("%02d", monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));

                        }
                    },
                    // on below line we are passing year,
                    // month and day for selected date in our date picker.
                    year, month, day);
            // at last we are calling show to
            // display our date picker dialog.
            datePickerDialog.show();
        });

        btn_cari.setOnClickListener(view -> {
            Date tgl = Date.valueOf(tv_date_awal.getText().toString());
            Date tgl2 = Date.valueOf(tv_date_ahir.getText().toString());
            String date_awal = sdf.format(tgl);
            String date_ahir = sdf.format(tgl2);
            progressDialog.setMessage("Mohon tunggu...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            getPendapatan(date_awal, date_ahir);
        });
    }

    private void getPendapatan(String tgl_awal, String tgl_ahir) {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getLapTransaksi(tgl_awal, tgl_ahir);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
                            mAdapter = new PedapatanHarianRecylerAdapter(context, transaksis);
                            mRecycler.setAdapter(mAdapter);
                            double total = 0;
                            for (int x = 0; x < transaksis.size(); x++) {
                                total += (Double.parseDouble(transaksis.get(x).getHarga()) * Double.parseDouble(transaksis.get(x).getJumlah())) - Double.parseDouble(transaksis.get(x).getDiskon());
                                tv_lap_penjualan_total.setText("Subtotal:   Rp." + nf.format(total));
                            }
                        }
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
}