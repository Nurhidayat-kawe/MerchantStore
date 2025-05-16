package com.yyaayyaatt.merchantstore.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.PelangganBaruActivity;
import com.yyaayyaatt.merchantstore.PesananActivity;
import com.yyaayyaatt.merchantstore.ProdukTerlarisActivity;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.adapter.PelangganTopRecylerAdapter;
import com.yyaayyaatt.merchantstore.adapter.ProdukTopRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Pelanggan;
import com.yyaayyaatt.merchantstore.model.PelangganTop;
import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.model.ResponsePelanggan;
import com.yyaayyaatt.merchantstore.model.ResponsePelangganTop;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.model.ResponseTransaksi;
import com.yyaayyaatt.merchantstore.model.ResponseUsers;
import com.yyaayyaatt.merchantstore.model.Transaksi;
import com.yyaayyaatt.merchantstore.model.Users;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private final SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    NumberFormat nf = NumberFormat.getCurrencyInstance();

    TextView textCartItemCount, textPelangganCount;
    int mCartItemCount = 0, mPelangganCount = 0;

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Users> users = new ArrayList<>();
    TextView tv_pelanggan, tv_produk, tv_pesanan, tv_pendapatan, tv_lihat_produk;
    List<Produk> produks = new ArrayList<>();
    List<Transaksi> transaksis = new ArrayList<>();
    List<Transaksi> pendapatans = new ArrayList<>();
    List<Transaksi> transaksis2 = new ArrayList<>();
    List<PelangganTop> pelangganTops = new ArrayList<>();
    List<Pelanggan> pelanggans = new ArrayList<>();

    private RecyclerView rv_produk;
    private RecyclerView rv_pelanggan_top;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = view.getContext();
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);
        tv_pelanggan = view.findViewById(R.id.tv_pelanggan_count);
        tv_produk = view.findViewById(R.id.tv_produk_count);
        tv_lihat_produk = view.findViewById(R.id.tv_produk_lihat_semua);
        tv_pesanan = view.findViewById(R.id.tv_pesanan_count);
        tv_pendapatan = view.findViewById(R.id.tv_pendapatan_count);
        rv_produk = view.findViewById(R.id.recyclerView2);
        rv_pelanggan_top = view.findViewById(R.id.rv_pelanggan_top);

        mContext = view.getContext();
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        rv_produk.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        rv_produk.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.HORIZONTAL, false));
        rv_pelanggan_top.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        rv_pelanggan_top.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));
        setHasOptionsMenu(true);
        getUsers();
        getProduk();
        getTrans();
        getPendapatan();
        getProdukTop();
        getPelangganTop();

        tv_lihat_produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ProdukTerlarisActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getUsers();
        getProduk();
        getTrans();
        getPendapatan();
        getProdukTop();
        getPelangganTop();
        getTransNotif();
        getPelangganBaru();
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
        if (textPelangganCount != null) {
            if (mPelangganCount == 0) {
                if (textPelangganCount.getVisibility() != View.GONE) {
                    textPelangganCount.setVisibility(View.GONE);
                }
            } else {
                textPelangganCount.setText(String.valueOf(Math.min(mPelangganCount, 99)));
                if (textPelangganCount.getVisibility() != View.VISIBLE) {
                    textPelangganCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //Memanggil/Memasang menu item pada toolbar dari layout menu_bar.xml
        inflater.inflate(R.menu.menu_home, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_home_notif);
        MenuItem menuItemPel = menu.findItem(R.id.menu_item_home_pelanggan);
        View actionView = menuItem.getActionView();
        View actionViewPel = menuItemPel.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        textPelangganCount = actionViewPel.findViewById(R.id.cart_badge_pelanggan);
        getTransNotif();
        getPelangganBaru();
        setupBadge();

        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        actionViewPel.setOnClickListener(v -> onOptionsItemSelected(menuItemPel));
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//
//            queryTextListener = new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    Log.i("onQueryTextChange", newText);
//
//                    return true;
//                }
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Log.i("onQueryTextSubmit", query);
//
//                    Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//            };
//            searchView.setOnQueryTextListener(queryTextListener);
//        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if(id==R.id.menu_item_home_notif) {
                // Not implemented here
                startActivity(new Intent(mContext, PesananActivity.class));
            }
            else if(id==R.id.menu_item_home_pelanggan) {
                startActivity(new Intent(mContext, PelangganBaruActivity.class));
            }
//        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
//
//    private void setupSlider(ImageSlider imageSlider) {
////        bannerSlider.setDurationScroll(800);
////        List<Fragment> fragments = new ArrayList<>();
////
////        //link image
////        fragments.add(SliderFragment.newInstance(UtilsApi.BASE_URL_API+"sampul/masjid_senja.jpg"));
////        fragments.add(SliderFragment.newInstance(UtilsApi.BASE_URL_API+"sampul/banner1.jpg"));
////        fragments.add(SliderFragment.newInstance(UtilsApi.BASE_URL_API+"sampul/banner2.jpg"));
////        fragments.add(SliderFragment.newInstance(UtilsApi.BASE_URL_API+"sampul/banner3.jpg"));
////        mAdapter = new SliderPagerAdapter(getParentFragmentManager(), fragments);
////        bannerSlider.setAdapter(mAdapter);
////        mIndicator = new SliderIndicator(getActivity(), mLinearLayout, bannerSlider, R.drawable.indicator_circle);
////        mIndicator.setPageCount(fragments.size());
////        mIndicator.show();
//        List<SlideModel> slideModels = new ArrayList<>();
//        slideModels.add(new SlideModel(UtilsApi.BASE_URL_API+"sampul/masjid_senja.jpg","Masjid Agung di Senja Hari", ScaleTypes.FIT));
//        slideModels.add(new SlideModel(UtilsApi.BASE_URL_API+"sampul/banner1.jpg","Perpustakaan Keliling",ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(UtilsApi.BASE_URL_API+"sampul/banner2.jpg","Masjid Agung",ScaleTypes.CENTER_CROP));
//        slideModels.add(new SlideModel(UtilsApi.BASE_URL_API+"sampul/banner3.jpg","Pojok Baca Anak-Anak",ScaleTypes.FIT));
//        imageSlider.setImageList(slideModels,ScaleTypes.FIT);
//    }

    private void getUsers() {
        users.clear();
        Call<ResponseUsers> getdata = mApiService.getPelanggan();
        getdata.enqueue(new Callback<ResponseUsers>() {
            @Override
            public void onResponse(Call<ResponseUsers> call, Response<ResponseUsers> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        users = response.body().getResult();
                        if (!users.isEmpty()) {
                            tv_pelanggan.setText(users.get(0).getJml_user());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUsers> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void getProduk() {
        produks.clear();
        Call<ResponseProduk> getdata = mApiService.getProdukCount();
        getdata.enqueue(new Callback<ResponseProduk>() {
            @Override
            public void onResponse(Call<ResponseProduk> call, Response<ResponseProduk> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        produks = response.body().getResult();
                        if (!produks.isEmpty()) {
                            tv_produk.setText(produks.get(0).getJml_produk());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProduk> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void getTrans() {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getTransCount();
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
                            tv_pesanan.setText(transaksis.get(0).getJml_trans());
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

    private void getTransNotif() {
        transaksis.clear();
        Call<ResponseTransaksi> getdata = mApiService.getTransNotif();
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        transaksis = response.body().getResult();
                        if (!transaksis.isEmpty()) {
                            textCartItemCount.setText(transaksis.get(0).getJml_trans());
                            mCartItemCount = Integer.parseInt(transaksis.get(0).getJml_trans());
                            setupBadge();
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

    private void getPelangganBaru() {
        pelanggans.clear();
        Call<ResponsePelanggan> getdata = mApiService.getPelangganBaru();
        getdata.enqueue(new Callback<ResponsePelanggan>() {
            @Override
            public void onResponse(Call<ResponsePelanggan> call, Response<ResponsePelanggan> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        pelanggans = response.body().getResult();
                        if (!pelanggans.isEmpty()) {
                            textPelangganCount.setText(pelanggans.get(0).getPel_baru());
                            mPelangganCount = Integer.parseInt(pelanggans.get(0).getPel_baru());
                            setupBadge();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePelanggan> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void getPendapatan() {
        pendapatans.clear();
        Call<ResponseTransaksi> getdata = mApiService.getPendapatan();
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        pendapatans = response.body().getResult();
                        if (!pendapatans.isEmpty()) {
                            if (pendapatans.get(0).getTotal() == null) {
                                tv_pendapatan.setText("Rp.0");
                            } else {
                                tv_pendapatan.setText(nf.format(Double.parseDouble(pendapatans.get(0).getTotal())));
                            }
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

    private void getProdukTop() {
        transaksis2.clear();
        Call<ResponseTransaksi> getdata = mApiService.getProdukTop();
        getdata.enqueue(new Callback<ResponseTransaksi>() {
            @Override
            public void onResponse(Call<ResponseTransaksi> call, Response<ResponseTransaksi> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        transaksis2 = response.body().getResult();
                        if (!transaksis2.isEmpty()) {
                            mAdapter = new ProdukTopRecylerAdapter(mContext, transaksis2);
                            rv_produk.setAdapter(mAdapter);

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

    private void getPelangganTop() {
        pelangganTops.clear();
        Call<ResponsePelangganTop> getdata = mApiService.getPelangganTop();
        getdata.enqueue(new Callback<ResponsePelangganTop>() {
            @Override
            public void onResponse(Call<ResponsePelangganTop> call, Response<ResponsePelangganTop> response) {
                if (response.isSuccessful()) {
                    if (response.body().getmKode().equals("1")) {
                        pelangganTops = response.body().getResult();
                        if (!pelangganTops.isEmpty()) {
                            mAdapter2 = new PelangganTopRecylerAdapter(mContext, pelangganTops);
                            rv_pelanggan_top.setAdapter(mAdapter2);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePelangganTop> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }
}