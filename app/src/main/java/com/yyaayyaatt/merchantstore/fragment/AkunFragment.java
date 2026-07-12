package com.yyaayyaatt.merchantstore.fragment;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.button.MaterialButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.JamOperasionalActivity;
import com.yyaayyaatt.merchantstore.LapTransaksiPenjualanActivity;
import com.yyaayyaatt.merchantstore.LapTransaksiPenjualanPointActivity;
import com.yyaayyaatt.merchantstore.LaporanPenjualanActivity;
import com.yyaayyaatt.merchantstore.LaporanPenjualanAllActivity;
import com.yyaayyaatt.merchantstore.ListRetailsActivity;
import com.yyaayyaatt.merchantstore.LoginActivity;
import com.yyaayyaatt.merchantstore.PendapatanActivity;
import com.yyaayyaatt.merchantstore.PointMemberActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.ReferralActivity;
import com.yyaayyaatt.merchantstore.Upload.BaseResponse;
import com.yyaayyaatt.merchantstore.Upload.FileUtils;
import com.yyaayyaatt.merchantstore.Upload.UploadService;
import com.yyaayyaatt.merchantstore.model.ResponseStatusServer;
import com.yyaayyaatt.merchantstore.model.ResponseUsers;
import com.yyaayyaatt.merchantstore.model.StatusServer;
import com.yyaayyaatt.merchantstore.model.Users;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkunFragment extends Fragment {

    Context mContext;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    List<Users> users = new ArrayList<>();
    ProgressDialog progressDialog;
    CircleImageView img;
    TextView txtNama, tv_alamat, tv_version;
    TextView tvMasaAktifTgl, tvMasaAktifStatus, tvMasaAktifNama;
    TextView tvTenggangPoint, tvTenggangInfo;
    ImageButton ibEditTenggang;
    String img_url = "";
    AppCompatButton btn_penjualan, btn_penjualan2, btn_rekap_transaksi,
            btn_pendapatan, btn_jam_operasional,btn_akun_penukaran_point, btn_akun_points, btn_data_ref, btn_retail;
    MaterialButton btn_logout;
    //upload
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private Uri uri;
    private static final String TYPE_1 = "multipart";
    private String imageName;
    private UploadService uploadService;
    ImageButton ib_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_akun, container, false);

        img = view.findViewById(R.id.imgAkunFoto);
        ib_image = view.findViewById(R.id.ib_img);
        txtNama = view.findViewById(R.id.txtAkunNama);
        tv_alamat = view.findViewById(R.id.tv_akun_alamat);
        btn_penjualan = view.findViewById(R.id.btn_akun_penjualan);
        btn_penjualan2 = view.findViewById(R.id.btn_akun_penjualan2);
        btn_rekap_transaksi = view.findViewById(R.id.btn_akun_rekap_transaksi);
        btn_akun_penukaran_point = view.findViewById(R.id.btn_akun_penukaran_point);
        btn_pendapatan = view.findViewById(R.id.btn_akun_rekap_pendapatan);
        btn_akun_points = view.findViewById(R.id.btn_akun_point_member);
        btn_data_ref = view.findViewById(R.id.btn_akun_referral);
        btn_retail = view.findViewById(R.id.btn_akun_referral2);
        btn_jam_operasional = view.findViewById(R.id.btn_akun_jam_operasional);
        btn_logout = view.findViewById(R.id.btn_akun_logout);
        tvMasaAktifTgl = view.findViewById(R.id.tv_masa_aktif_tgl);
        tvMasaAktifStatus = view.findViewById(R.id.tv_masa_aktif_status);
        tvMasaAktifNama = view.findViewById(R.id.tv_masa_aktif_nama);
        tvTenggangPoint = view.findViewById(R.id.tv_tenggang_point);
        tvTenggangInfo = view.findViewById(R.id.tv_tenggang_info);
        ibEditTenggang = view.findViewById(R.id.ib_edit_tenggang);

        mContext = view.getContext();
        sharedPrefManager = new SharedPrefManager(mContext);
        mApiService = UtilsApi.getAPIService();
        progressDialog = ProgressDialog.show(mContext, "Load Data User",
                "Harap tunggu...", true, false);
        getUser(view);
        getServer(view);

        btn_penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, LaporanPenjualanActivity.class));
            }
        });
        btn_penjualan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, LaporanPenjualanAllActivity.class));
            }
        });
        btn_rekap_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, LapTransaksiPenjualanActivity.class));
            }
        });
        btn_pendapatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, PendapatanActivity.class));
            }
        });
        btn_jam_operasional.setOnClickListener(View -> {
            startActivity(new Intent(mContext, JamOperasionalActivity.class));
        });
        btn_akun_penukaran_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, LapTransaksiPenjualanPointActivity.class));
            }
        });
        btn_akun_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, PointMemberActivity.class));
            }
        });

        btn_data_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ReferralActivity.class));
            }
        });

        btn_retail.setOnClickListener(view1 -> startActivity(new Intent(mContext, ListRetailsActivity.class)));

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

        ib_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

        ibEditTenggang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditTenggangDialog();
            }
        });

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String versionName = pInfo.versionName;
            int versionCode = pInfo.versionCode;

            // Tampilkan versi di TextView
            tv_version = view.findViewById(R.id.tv_akun_version);
            tv_version.setText("Version: " + versionName +" Released");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void getUser(View v) {
        users.clear();//sharedPrefManager.getSpIdPengguna()
        Call<ResponseUsers> getdata = mApiService.getProfil(Integer.parseInt(sharedPrefManager.getSpIdPengguna()));
        getdata.enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        users = response.body().getResult();
                        for (Users u : users) {
                            txtNama.setText(u.getNama());
                            img_url = UtilsApi.BASE_URL_IMG + "images/" + u.getFoto();
                            Glide.with(v.getContext())
                                    .load(img_url)
                                    .placeholder(R.drawable.noimage)
                                    .error(R.drawable.employeeicon)
                                    .into(img);
                            tv_alamat.setText(u.getAlamat());
                        }
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

    private void updateFoto(String foto) {
        users.clear();//sharedPrefManager.getSpIdPengguna()
        Call<ResponseUsers> getdata = mApiService.updateFoto(Integer.parseInt(sharedPrefManager.getSpIdPengguna()), foto);
        getdata.enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        Glide.with(mContext).load(UtilsApi.BASE_URL_API + "images/" + foto).error(R.drawable.noimage).into(img);
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

    private void getServer(View v) {
        Call<ResponseStatusServer> call = mApiService.getServer();
        call.enqueue(new Callback<ResponseStatusServer>() {
            @Override
            public void onResponse(Call<ResponseStatusServer> call, Response<ResponseStatusServer> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getmKode().equals("1")) {
                    List<StatusServer> list = response.body().getResult();
                    if (list != null && !list.isEmpty()) {
                        StatusServer s = list.get(0);
                        tvMasaAktifNama.setText(s.getNama_server());
                        if (s.getTenggang_point() != null && !s.getTenggang_point().isEmpty()) {
                            tvTenggangPoint.setText(s.getTenggang_point());
                        } else {
                            tvTenggangPoint.setText("-");
                        }
                        if (s.getTgl_ed() != null && !s.getTgl_ed().isEmpty()) {
                            tvMasaAktifTgl.setText(s.getTgl_ed());
                            String remaining = updateExpiryStatus(s.getTgl_ed());
                            if (remaining != null) {
                                sendExpiryNotification(remaining);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseStatusServer> call, Throwable t) {
                Log.e("debug", "getServer error", t);
            }
        });
    }

    private String updateExpiryStatus(String tglEd) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            java.util.Date expiredDate = sdf.parse(tglEd);
            java.util.Date now = new java.util.Date();

            if (expiredDate != null) {
                long diff = expiredDate.getTime() - now.getTime();
                long daysRemaining = diff / (1000 * 60 * 60 * 24);

                if (daysRemaining < 0) {
                    tvMasaAktifStatus.setText("Masa Aktif Habis");
                    tvMasaAktifStatus.setBackgroundResource(R.drawable.btn_round_red);
                } else if (daysRemaining <= 30) {
                    tvMasaAktifStatus.setText("Hampir Habis (" + daysRemaining + " hari lagi)");
                    tvMasaAktifStatus.setBackgroundResource(R.drawable.btn_round_orange);
                    if (daysRemaining <= 14) {
                        return String.valueOf(daysRemaining);
                    }
                } else {
                    tvMasaAktifStatus.setText("Aktif (" + daysRemaining + " hari lagi)");
                    tvMasaAktifStatus.setBackgroundResource(R.drawable.btn_round_green);
                }
            }
        } catch (Exception e) {
            Log.e("debug", "Error parsing date", e);
        }
        return null;
    }

    private void sendExpiryNotification(String daysLeft) {
        String channelId = "fcm_default_channel";
        NotificationManager nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Notifikasi Aplikasi", NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
        }

        Intent intent = new Intent(mContext, getActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivity(mContext, 0, intent,
                android.app.PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Masa Aktif Aplikasi")
                .setContentText("Sisa " + daysLeft + " hari lagi. Segera perpanjang masa aktif!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        nm.notify(1001, builder.build());
    }

    private void showEditTenggangDialog() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String currentDate = tvTenggangPoint.getText().toString();
        if (!currentDate.equals("-")) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                java.util.Date d = sdf.parse(currentDate);
                if (d != null) cal.setTime(d);
            } catch (Exception ignored) {}
        }

        final DatePickerDialog datePicker = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        String tgl = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        updateTenggangPoint(tgl);
                    }
                },
                cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH),
                cal.get(java.util.Calendar.DAY_OF_MONTH));
        datePicker.setTitle("Batas Akhir Penukaran Point");
        datePicker.setButton(DatePickerDialog.BUTTON_NEUTRAL, "Hapus Tanggal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateTenggangPoint("");
            }
        });
        datePicker.show();
    }

    private void updateTenggangPoint(String tgl) {
        final ProgressDialog pd = ProgressDialog.show(mContext, "Menyimpan", "Harap tunggu...", true, false);
        Call<ResponseStatusServer> call = mApiService.updateServer(tgl);
        call.enqueue(new Callback<ResponseStatusServer>() {
            @Override
            public void onResponse(Call<ResponseStatusServer> call, Response<ResponseStatusServer> response) {
                pd.dismiss();
                if (response.isSuccessful() && response.body() != null && response.body().getmKode().equals("1")) {
                    if (tgl.isEmpty()) {
                        tvTenggangPoint.setText("-");
                        Toast.makeText(mContext, "Batas penukaran point dihapus", Toast.LENGTH_SHORT).show();
                    } else {
                        tvTenggangPoint.setText(tgl);
                        Toast.makeText(mContext, "Batas penukaran point berhasil diupdate", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Gagal menyimpan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseStatusServer> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(mContext, "Koneksi terputus", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout(View v) {
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
        startActivity(new Intent(v.getContext(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void choosePhoto() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);

        } else {
            openGallery();
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    private void uploadMultipart(File file) {
        RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo",
                file.getName(), photoBody);

        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), TYPE_1);

        uploadService = new UploadService();
        uploadService.uploadPhotoMultipart(action, photoPart, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();

                if (baseResponse != null) {
                    Toast.makeText(mContext, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    updateFoto(file.getName());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                uri = data.getData();
                String a = FileUtils.getPath(mContext, uri);
                System.out.println("URL >>>> " + a);

                if (uri != null) {
                    File file = FileUtils.getFile(mContext, uri);
                    imageName = file.getName();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                                new FileOutputStream(file));
                    } catch (Throwable t) {
                        Log.e("ERROR", "Error compressing file." + t.toString());
                        t.printStackTrace();
                    }

                    System.out.println("URL>>> " + file.getAbsoluteFile());
                    uploadMultipart(file);

                } else {
                    Toast.makeText(mContext, "You must choose the image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}