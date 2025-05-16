package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.model.JamOperasional;
import com.yyaayyaatt.merchantstore.model.ResponseJamOperasional;
import com.yyaayyaatt.merchantstore.model.ResponseSettings;
import com.yyaayyaatt.merchantstore.model.ResponseUsers;
import com.yyaayyaatt.merchantstore.model.Settings;
import com.yyaayyaatt.merchantstore.model.Users;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JamOperasionalActivity extends AppCompatActivity {

    AppCompatButton btn_set;
    List<Settings> settings = new ArrayList<>();
    List<JamOperasional> jamOperasionals = new ArrayList<>();

    Context mContext;
    BaseApiService mApiService;
    ProgressDialog progressDialog;
    TextView tv_senin, tv_selasa, tv_rabu, tv_kamis, tv_jumat, tv_sabtu, tv_minggu;
    Switch status_toko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jam_operasional);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        btn_set = findViewById(R.id.btn_set_jam);
        tv_senin = findViewById(R.id.tv_set_senin);
        tv_selasa = findViewById(R.id.tv_set_selasa);
        tv_rabu = findViewById(R.id.tv_set_rabu);
        tv_kamis = findViewById(R.id.tv_set_kamis);
        tv_jumat = findViewById(R.id.tv_set_jumat);
        tv_sabtu = findViewById(R.id.tv_set_sabtu);
        tv_minggu = findViewById(R.id.tv_set_minggu);
        status_toko = findViewById(R.id.switch_jam_operasional);

        mContext = JamOperasionalActivity.this;
        mApiService = UtilsApi.getAPIService();
        progressDialog = ProgressDialog.show(mContext, "Load Jam Operasional",
                "Harap tunggu...", true, true);
        getSettings();
        getJam();

        status_toko.setOnClickListener(View->{
            if(status_toko.isChecked()== true) {
                updateStatus("1");
                btn_set.setVisibility(View.VISIBLE);
            }else{
                updateStatus("0");
                btn_set.setVisibility(View.GONE);
            }
        });
        btn_set.setOnClickListener(View -> {
            startActivity(new Intent(JamOperasionalActivity.this, SetJamOperasionalActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        getJam();
    }

    private void getSettings() {
        settings.clear();//sharedPrefManager.getSpIdPengguna()
        Call<ResponseSettings> getdata = mApiService.getStatusToko();
        getdata.enqueue(new Callback<ResponseSettings>() {
            @Override
            public void onResponse(Call<ResponseSettings> call, Response<ResponseSettings> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        settings = response.body().getResult();
                        for (Settings u : settings) {
                            Toast.makeText(mContext, ""+u.getStatus_toko(), Toast.LENGTH_SHORT).show();
                            status_toko.setChecked(u.getStatus_toko().equals("1"));
                            if(u.getStatus_toko().equals("1")){
                                btn_set.setVisibility(View.VISIBLE);
                            }else{
                                btn_set.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSettings> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
                Toast.makeText(mContext, "Koneksi terputus...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatus(String status_toko) {
        Call<ResponseSettings> getdata = mApiService.updateStatusToko(status_toko);
        getdata.enqueue(new Callback<ResponseSettings>() {
            @Override
            public void onResponse(Call<ResponseSettings> call, Response<ResponseSettings> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        Toast.makeText(mContext, response.body().getmPesan(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSettings> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
                Toast.makeText(mContext, "Koneksi terputus...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getJam() {
        jamOperasionals.clear();//sharedPrefManager.getSpIdPengguna()
        Call<ResponseJamOperasional> getdata = mApiService.getJamOperasional();
        getdata.enqueue(new Callback<ResponseJamOperasional>() {
            @Override
            public void onResponse(Call<ResponseJamOperasional> call, Response<ResponseJamOperasional> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        jamOperasionals = response.body().getResult();
                        for (JamOperasional u : jamOperasionals) {
                            if(u.getHari().equals("SENIN")) {
                                tv_senin.setText(jam(u.getJam_buka(),u.getJam_tutup()));
                            }else if(u.getHari().equals("SELASA")) {
                                tv_selasa.setText(jam(u.getJam_buka(),u.getJam_tutup()));
                            }else if(u.getHari().equals("RABU")) {
                                tv_rabu.setText(jam(u.getJam_buka(),u.getJam_tutup()));
                            }else if(u.getHari().equals("KAMIS")) {
                                tv_kamis.setText(jam(u.getJam_buka(),u.getJam_tutup()));
                            }else if(u.getHari().equals("JUMAT")) {
                                tv_jumat.setText(jam(u.getJam_buka(),u.getJam_tutup()));
                            }else if(u.getHari().equals("SABTU")) {
                                tv_sabtu.setText(jam(u.getJam_buka(),u.getJam_tutup()));
                            }else if(u.getHari().equals("MINGGU")) {
                                tv_minggu.setText(jam(u.getJam_buka(),u.getJam_tutup()));
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseJamOperasional> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
                Toast.makeText(mContext, "Koneksi terputus...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String jam(String jam_buka, String jam_tutup){
        if(jam_buka.equals("00:00:00") && jam_tutup.equals("00:00:00")){
            return "Libur";
        }else{
            return jam_buka.substring(0,5)+" - "+jam_tutup.substring(0,5);
        }
    }
}