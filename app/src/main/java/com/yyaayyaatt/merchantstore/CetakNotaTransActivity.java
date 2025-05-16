package com.yyaayyaatt.merchantstore;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.yyaayyaatt.merchantstore.adapter.ProdukTransaksiRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CetakNotaTransActivity extends AppCompatActivity {

    private static int PERMISSION_BLUETOOTH_ADMIN = 1;
    private static int PERMISSION_BLUETOOTH_CONNECT = 1;
    private static int PERMISSION_BLUETOOTH_SCAN = 1;
    TextView tv_nama_toko, tv_telp, tv_kode_trans, tv_total, tv_tanggal, tv_toko, tv_subtotal, tv_preview;
    AppCompatButton btn_cetak;
    ImageButton ib_share;
    RecyclerView rv_cetek;
    private RecyclerView.Adapter mAdapter;
    Intent i;
    Context context;
    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    List<Transaksi> transaksis = new ArrayList<>();
    Transaksi pesanan = new Transaksi();
    Transaksi transaksi = new Transaksi();
    List<Transaksi> produkPesanan = new ArrayList<>();
    NumberFormat nf = NumberFormat.getIntegerInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd M yy");
    Date date;
    public static final int PERMISSION_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_nota_trans);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F5005A7E"));
        actionBar.setBackgroundDrawable(colorDrawable);

        i = getIntent();
        context = CetakNotaTransActivity.this;

        progressDialog = new ProgressDialog(context);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(context);

        tv_nama_toko = findViewById(R.id.tv_cetak_nota_jual_cabang);
        tv_telp = findViewById(R.id.tv_cetak_nota_jual_cabang_telp);
        tv_kode_trans = findViewById(R.id.tv_cetak_nota_jual_nota);
        tv_total = findViewById(R.id.tv_cetak_nota_jual_total_bayar1);
        tv_toko = findViewById(R.id.tv_cetak_nota_jual_toko);
        tv_tanggal = findViewById(R.id.tv_cetak_nota_jual_tanggal);
        tv_subtotal = findViewById(R.id.tv_cetak_nota_jual_subtotal);
        btn_cetak = findViewById(R.id.btn_cetak_nota_jual_cetak);
        ib_share = findViewById(R.id.btn_share_nota);
        rv_cetek = findViewById(R.id.rv_cetak_nota);
        tv_preview = findViewById(R.id.tv_preview);

        rv_cetek.setHasFixedSize(true);
        rv_cetek.setLayoutManager(new GridLayoutManager(context, 1, RecyclerView.VERTICAL, false));

        progressDialog.setMessage("Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getPesanan(i.getStringExtra("id_trans"));
        getBarangTrans(i.getStringExtra("id_trans"));

        btn_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
                BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
                if (!bluetoothAdapter.isEnabled()) {
                    startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                } else {
                    //cetak ke printer bloeetotth
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(CetakNotaTransActivity.this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CetakNotaTransActivity.this, new String[]{Manifest.permission.BLUETOOTH}, CetakNotaTransActivity.PERMISSION_BLUETOOTH);
                    } else if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(CetakNotaTransActivity.this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CetakNotaTransActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, CetakNotaTransActivity.PERMISSION_BLUETOOTH_ADMIN);
                    } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(CetakNotaTransActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CetakNotaTransActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, CetakNotaTransActivity.PERMISSION_BLUETOOTH_CONNECT);
                    } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ContextCompat.checkSelfPermission(CetakNotaTransActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CetakNotaTransActivity.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, CetakNotaTransActivity.PERMISSION_BLUETOOTH_SCAN);
                    } else {
                        doPrint(view);
                    }
                }
            }
        });

        ib_share.setOnClickListener(View -> {
            String message = share();
            // Creating new intent
            Intent intent
                    = new Intent(
                    Intent.ACTION_SEND);

            intent.setType("text/plain");
//            intent.setPackage("com.whatsapp");

            // Give your message here
            intent.putExtra(
                    Intent.EXTRA_TEXT,
                    message);

            // Checking whether Whatsapp
            // is installed or not
            if (intent
                    .resolveActivity(
                            getPackageManager())
                    == null) {
                Toast.makeText(
                                this,
                                "Please install whatsapp first.",
                                Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            // Starting Whatsapp
            startActivity(intent);
        });
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
                            tv_nama_toko.setText(transaksis.get(0).getNama_cabang());
                            tv_telp.setText(transaksis.get(0).getTelp_cabang());
                            tv_kode_trans.setText("#" + transaksis.get(0).getId_transaksi());
                            tv_toko.setText(transaksis.get(0).getToko());
                            tv_tanggal.setText(transaksis.get(0).getTanggal() + " " + transaksis.get(0).getJam().substring(0, 5));

                            pesanan.setNama_cabang(transaksis.get(0).getNama_cabang());
                            pesanan.setTelp_cabang(transaksis.get(0).getTelp_cabang());
                            pesanan.setId_transaksi("#" + transaksis.get(0).getId_transaksi());
                            pesanan.setToko(transaksis.get(0).getToko());
                            pesanan.setTanggal(transaksis.get(0).getTanggal() + " " + transaksis.get(0).getJam().substring(0, 5));
//                            mAdapter = new ProdukTopRecylerAdapter(context, transaksis);
//                            rv_cetek.setAdapter(mAdapter);
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

    private void getBarangTrans(String key) {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getNotaTrans(key);
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
//                            tv_nama_toko.setText(transaksis.get(0).getNama_cabang());
//                            tv_telp.setText(transaksis.get(0).getTelp_cabang());
//                            tv_kode_trans.setText("#"+transaksis.get(0).getId_transaksi());
//                            tv_toko.setText(transaksis.get(0).getToko());
//                            tv_tanggal.setText(transaksis.get(0).getTanggal() +" "+transaksis.get(0).getJam().substring(0,5));
//                            tv_total.setText(nf.format(Double.parseDouble(transaksis.get(0).getTotal())));
//                            tv_subtotal.setText(nf.format(Double.parseDouble(transaksis.get(0).getTotal())));

                            double total = 0;
                            for (int t = 0; t < transaksis.size(); t++) {
                                total += Double.parseDouble(transaksis.get(t).getHarga()) * Double.parseDouble(transaksis.get(t).getJumlah());
                            }
                            tv_total.setText(nf.format(total));
                            tv_subtotal.setText(nf.format(total));
                            pesanan.setTotal("" + total);
                            for (Transaksi t : transaksis) {
                                transaksi.setJumlah(t.getJumlah());
                                transaksi.setNama_produk(t.getNama_produk());
                                transaksi.setHarga(t.getHarga());
                                produkPesanan.add(transaksi);
                            }

                            mAdapter = new ProdukTransaksiRecylerAdapter(context, transaksis);
                            rv_cetek.setAdapter(mAdapter);
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

    public void doPrint(View view) {
        try {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, CetakNotaTransActivity.PERMISSION_BLUETOOTH);
//            } else {
            BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
            if (connection != null) {
                EscPosPrinter printer = new EscPosPrinter(connection, 203, 58f, 32);
                final String text =
                        "[C]<b><font size='big'>" + pesanan.getNama_cabang() + "</font></b>\n" +
                                "[C]" + pesanan.getTelp_cabang() + "\n" +
                                "[C]" + pesanan.getTanggal() + "\n" +
                                "[C]=============================================\n" +
                                "[L]Kode Pesanan <b>" + pesanan.getId_transaksi() + "</b>\n" +
                                "[L]Nama Toko <b>" + pesanan.getToko() + "</b>\n" +
                                "[C]=============================================\n" +
                                produks()
                                +
                                "[C]---------------------------------------------\n" +
                                "[L]TOTAL[R]Rp" + nf.format(Double.parseDouble(pesanan.getTotal())) + "\n" +
                                "[C]---------------------------------------------\n" +
//                                    "[C]<barcode type='ean13' height='10'>202105160005</barcode>\n" +
//                                    "[C]--------------------------------\n" +
                                "[C]Terima Kasih Telah Berbelanja\n" +
                                "[L]\n";

                printer.printFormattedTextAndCut(text);
            } else {

                Toast.makeText(CetakNotaTransActivity.this, "Printer Bluetooth tidak Terhubung!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
//                    tv_preview.setText(text);
//                }
            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }

    private String produks() {
        String text = null;
        StringBuilder sb = new StringBuilder();
        for (Transaksi t : transaksis) {
            sb.append("[L]" + t.getNama_produk() + "\n" +
                    "[L]" + t.getJumlah() + "[L] x " + nf.format(Double.parseDouble(t.getHarga())) + " [R]" + nf.format(Double.parseDouble(t.getJumlah()) * Double.parseDouble(t.getHarga())) + "\n");
        }
        text = sb.toString();
        return text;
    }

    private String share() {
        final String text =
                "INVOICE dari *" + pesanan.getNama_cabang().toUpperCase() +
                        "*\n" + pesanan.getTelp_cabang() + "\n" +
                        pesanan.getTanggal() + "\n\n" +
                        "Kode Pesanan " + pesanan.getId_transaksi() + "\n" +
                        "Toko " + pesanan.getToko() + "\n\n" +
                        produkshare()
                        +
                        "-----------------------------------\n" +
                        "Total belanja " + nf.format(Double.parseDouble(pesanan.getTotal())) + "\n" +
                        "-----------------------------------\n" +
//                                    "[C]<barcode type='ean13' height='10'>202105160005</barcode>\n" +
//                                    "[C]--------------------------------\n" +
                        "Silahkan lakukan pembayaran CASH atau Transfer\n" +
                        "\n";
        return text;
    }

    private String produkshare() {
        String text = null;
        StringBuilder sb = new StringBuilder();
        for (Transaksi t : transaksis) {
            sb.append(t.getNama_produk() + "\n" +
                    t.getJumlah() + " x " + nf.format(Double.parseDouble(t.getHarga())) + "   " + nf.format(Double.parseDouble(t.getJumlah()) * Double.parseDouble(t.getHarga())) + "\n");
        }
        text = sb.toString();
        return text;
    }
}