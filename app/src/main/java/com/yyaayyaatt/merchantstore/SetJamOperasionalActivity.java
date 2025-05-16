package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.yyaayyaatt.merchantstore.model.JamOperasional;
import com.yyaayyaatt.merchantstore.model.ResponseJamOperasional;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetJamOperasionalActivity extends AppCompatActivity {

    List<JamOperasional> jamOperasionals = new ArrayList<>();

    Context mContext;
    BaseApiService mApiService;
    ProgressDialog progressDialog;
    Spinner sp_hari_operasional;
    AppCompatButton btn_simpan;
    ImageButton ibtn_jam_buka, ibtn_jam_tutup;
    EditText ed_jam_buka, ed_jam_tutup;
//    List<String> jamList = new ArrayList<>(R.array.days);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_jam_operasional);

        sp_hari_operasional = findViewById(R.id.sp_hari_operasional);
        btn_simpan = findViewById(R.id.btn_simpan_jam);
        ibtn_jam_buka = findViewById(R.id.ibtn_jam_buka);
        ed_jam_buka = findViewById(R.id.ed_jam_buka);
        ibtn_jam_tutup = findViewById(R.id.ibtn_jam_tutup);
        ed_jam_tutup = findViewById(R.id.ed_jam_tutup);


        mContext = SetJamOperasionalActivity.this;
        mApiService = UtilsApi.getAPIService();
        progressDialog = ProgressDialog.show(mContext, "Load Jam Operasional",
                "Harap tunggu...", true, false);
//        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, jamList);
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.activity_set_jam_operasional);
//        sp_hari_operasional.setAdapter(spinnerArrayAdapter);
//        getJam();

        ibtn_jam_buka.setOnClickListener(View-> {
            // on below line we are getting the
            // instance of our calendar.
            timePicker(ed_jam_buka);
        });
        ibtn_jam_tutup.setOnClickListener(View-> {
            // on below line we are getting the
            // instance of our calendar.
            timePicker(ed_jam_tutup);
        });

        sp_hari_operasional.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getJam(sp_hari_operasional.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_simpan.setOnClickListener(View->{
            progressDialog = ProgressDialog.show(mContext, "Menyimpan",
                    "Harap tunggu...", true, false);
            String jam_buka = ed_jam_buka.getText().toString();
                    String jam_tutup = ed_jam_tutup.getText().toString();
                    String hari = sp_hari_operasional.getSelectedItem().toString();
            simpan(hari,jam_buka,jam_tutup);
        });
    }

    private void simpan(String hari, String jam_buka, String jam_tutup) {
        jamOperasionals.clear();//sharedPrefManager.getSpIdPengguna()
        Call<ResponseJamOperasional> getdata = mApiService.updateJamOperasional(jam_buka,jam_tutup,hari);
        getdata.enqueue(new Callback<ResponseJamOperasional>() {
            @Override
            public void onResponse(Call<ResponseJamOperasional> call, Response<ResponseJamOperasional> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        Toast.makeText(mContext, response.body().getmPesan(), Toast.LENGTH_SHORT).show();
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

    private void timePicker(EditText ed) {
        final Calendar c = Calendar.getInstance();

        // on below line we are getting our hour, minute.
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // on below line we are initializing our Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(mContext,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // on below line we are setting selected time
                        // in our text view.
                        ed.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, false);
        // at last we are calling show to
        // display our time picker dialog.
        timePickerDialog.show();
    }


    private void getJam(String day) {
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
                            if(u.getHari().equals(day)) {
                                jam(u.getJam_buka(),u.getJam_tutup());
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
    private void jam(String jam_buka, String jam_tutup){
        ed_jam_buka.setText(jam_buka.substring(0,5));
        ed_jam_tutup.setText(jam_tutup.substring(0,5));
    }
}