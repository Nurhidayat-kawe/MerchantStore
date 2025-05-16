package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.yyaayyaatt.merchantstore.model.ResponseUsers;
import com.yyaayyaatt.merchantstore.model.Users;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_masuk, btn_daftar;
    EditText ed_user, ed_pass;
    BaseApiService mApiService;
    Context mContext;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    private List<Users> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#F5005A7E"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        btn_masuk = findViewById(R.id.btn_login_masuk);
        ed_user = findViewById(R.id.txt_login_username);
        ed_pass = findViewById(R.id.txt_login_password);

        mContext = LoginActivity.this;
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        btn_masuk.setOnClickListener(this);

        if (sharedPrefManager.getSPSudahLogin()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_login_masuk) {
            if (ed_user.getText().toString().trim().isEmpty()) {
                Toast.makeText(mContext, "Username Masih Kosong!", Toast.LENGTH_SHORT).show();
            } else if (ed_pass.getText().toString().trim().isEmpty()) {
                Toast.makeText(mContext, "Password Masih Kosong!", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Proses Login...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                login(ed_user.getText().toString(), ed_pass.getText().toString());
            }
        }
//        else if (id==R.id.btn_login_daftar){
//            startActivity(new Intent(getApplicationContext(),RegActivity.class));
//        }
    }

    private void login(String username, String pass) {
        mItems.clear();
        Call<ResponseUsers> getdata = mApiService.login(username, pass);
        getdata.enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        mItems = response.body().getResult();
                        if (mItems.isEmpty()) {
                            //Toast.makeText(LoginActivity.this, "Login Gagal.\nHarap cek kembali nik dan passwordnya.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(mContext, "Login Gagal.\nHarap cek kembali passwordnya.", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
//                            if (cb_ingat.isChecked() == true) {
//                                sharedPrefManager.saveSPString(SharedPrefManager.SP_REM_USER, ed_user.getText().toString());
//                                sharedPrefManager.saveSPString(SharedPrefManager.SP_REM_PASS, ed_pass.getText().toString());
//                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_STAT_CHECK, true);
//                            } else {
//                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_STAT_CHECK, false);
//                            }
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_PENGGUNA, String.valueOf(mItems.get(0).getId_user()));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_TELP, String.valueOf(mItems.get(0).getTelp()));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, String.valueOf(mItems.get(0).getNama()));
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(mContext, "Login Gagal.\nHarap cek kembali passwordnya.", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseUsers> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
                Toast.makeText(mContext, "Koneksi terputus...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}