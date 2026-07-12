package com.yyaayyaatt.merchantstore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yyaayyaatt.merchantstore.model.ResponseStatusServer;
import com.yyaayyaatt.merchantstore.model.ResponseUsers;
import com.yyaayyaatt.merchantstore.model.StatusServer;
import com.yyaayyaatt.merchantstore.model.Users;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    MaterialButton btn_masuk;
    TextInputEditText ed_user, ed_pass;
    TextInputLayout til_user, til_pass;
    ProgressBar pb_login;
    TextView tv_version;
    BaseApiService mApiService;
    Context mContext;
    SharedPrefManager sharedPrefManager;
    private List<Users> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        btn_masuk = findViewById(R.id.btn_login_masuk);
        ed_user = findViewById(R.id.txt_login_username);
        ed_pass = findViewById(R.id.txt_login_password);
        til_user = findViewById(R.id.til_username);
        til_pass = findViewById(R.id.til_password);
        pb_login = findViewById(R.id.pb_login);
        tv_version = findViewById(R.id.tv_login_version);

        mContext = LoginActivity.this;
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        btn_masuk.setOnClickListener(this);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            tv_version.setText("v" + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText("v2.9.2");
        }

        ed_user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                til_user.setError(null);
            }
        });
        ed_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                til_pass.setError(null);
            }
        });

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
            String username = ed_user.getText().toString().trim();
            String password = ed_pass.getText().toString().trim();

            til_user.setError(null);
            til_pass.setError(null);

            if (username.isEmpty()) {
                til_user.setError("Username masih kosong");
                ed_user.requestFocus();
            } else if (password.isEmpty()) {
                til_pass.setError("Password masih kosong");
                ed_pass.requestFocus();
            } else {
                setLoading(true);
                login(username, password);
            }
        }
    }

    private void setLoading(boolean loading) {
        btn_masuk.setEnabled(!loading);
        btn_masuk.setText(loading ? "" : "Masuk");
        pb_login.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void login(String username, String pass) {
        mItems.clear();
        Call<ResponseUsers> getdata = mApiService.login(username, pass);
        getdata.enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getmKode().equals("1")) {
                        mItems = response.body().getResult();
                        if (!mItems.isEmpty()) {
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_ID_PENGGUNA,
                                    String.valueOf(mItems.get(0).getId_user()));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_TELP,
                                    String.valueOf(mItems.get(0).getTelp()));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA,
                                    String.valueOf(mItems.get(0).getNama()));
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                            checkExpiry();
                        } else {
                            Toast.makeText(mContext, "Login Gagal.\nHarap cek kembali passwordnya.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String msg = response.body() != null ? response.body().getmPesan() : "Login Gagal";
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUsers> call, Throwable t) {
                setLoading(false);
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Toast.makeText(mContext, "Koneksi terputus...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkExpiry() {
        Call<ResponseStatusServer> call = mApiService.getServer();
        call.enqueue(new Callback<ResponseStatusServer>() {
            @Override
            public void onResponse(Call<ResponseStatusServer> call, Response<ResponseStatusServer> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getmKode().equals("1")) {
                    List<StatusServer> list = response.body().getResult();
                    if (list != null && !list.isEmpty()) {
                        String tglEd = list.get(0).getTgl_ed();
                        if (isExpired(tglEd)) {
                            showExpiryDialog();
                            return;
                        }
                    }
                }
                goToHome();
            }

            @Override
            public void onFailure(Call<ResponseStatusServer> call, Throwable t) {
                goToHome();
            }
        });
    }

    private boolean isExpired(String tglEd) {
        if (tglEd == null || tglEd.isEmpty()) return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date expired = sdf.parse(tglEd);
            return expired != null && expired.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private void showExpiryDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("Masa Aktif Habis")
                .setMessage("Masa aktif aplikasi sudah habis. Silakan hubungi admin untuk memperpanjang masa aktif agar dapat menggunakan aplikasi kembali.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void goToHome() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}
