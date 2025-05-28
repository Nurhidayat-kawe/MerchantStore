package com.yyaayyaatt.merchantstore.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.JamOperasionalActivity;
import com.yyaayyaatt.merchantstore.LapTransaksiPenjualanActivity;
import com.yyaayyaatt.merchantstore.LapTransaksiPenjualanPointActivity;
import com.yyaayyaatt.merchantstore.LaporanPenjualanActivity;
import com.yyaayyaatt.merchantstore.LaporanPenjualanAllActivity;
import com.yyaayyaatt.merchantstore.LoginActivity;
import com.yyaayyaatt.merchantstore.PendapatanActivity;
import com.yyaayyaatt.merchantstore.PointMemberActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.ReferralActivity;
import com.yyaayyaatt.merchantstore.Upload.BaseResponse;
import com.yyaayyaatt.merchantstore.Upload.FileUtils;
import com.yyaayyaatt.merchantstore.Upload.UploadService;
import com.yyaayyaatt.merchantstore.model.ResponseUsers;
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
    String img_url = "";
    AppCompatButton btn_penjualan, btn_penjualan2, btn_rekap_transaksi,
            btn_pendapatan, btn_jam_operasional,btn_akun_penukaran_point, btn_akun_points, btn_data_ref, btn_retail, btn_logout;
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

        mContext = view.getContext();
        sharedPrefManager = new SharedPrefManager(mContext);
        mApiService = UtilsApi.getAPIService();
        progressDialog = ProgressDialog.show(mContext, "Load Data User",
                "Harap tunggu...", true, false);
        getUser(view);

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

        btn_retail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ReferralActivity.class));
            }
        });

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