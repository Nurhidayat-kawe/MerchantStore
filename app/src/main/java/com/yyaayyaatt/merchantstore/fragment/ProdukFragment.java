package com.yyaayyaatt.merchantstore.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yyaayyaatt.merchantstore.AddProdukActivity;
import com.yyaayyaatt.merchantstore.ProdukFilterFragment;
import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.model.Kategori;
import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

public class ProdukFragment extends Fragment {

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Produk> produks = new ArrayList<>();
    List<Kategori> kategoris = new ArrayList<>();
    List<String> katStrings = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter adapter;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private final SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    AppCompatButton btn_add, btn_cari;
    EditText ed_cari;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produk, container, false);

        mRecycler = view.findViewById(R.id.rv_produk);
        btn_add = view.findViewById(R.id.btn_add_produk);
        mContext = view.getContext();
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        // Initialize array list
        ArrayList<String> arrayList = new ArrayList<>(0);

        // Add title in array list
        arrayList.add("Voucher");
        arrayList.add("Headset");
        arrayList.add("Kabel Data");
        arrayList.add("Handphone");
        arrayList.add("Power Bank");
        arrayList.add("Telkomsel");
        arrayList.add("Adaptor");
        arrayList.add("Indosat");
        arrayList.add("Smartfren");
        arrayList.add("Batrai HP");
        arrayList.add("Konverter");
        arrayList.add("Speaker Bluetooth");
        arrayList.add("Case");
        arrayList.add("Aksesories");
        arrayList.add("Memory Card");
        arrayList.add("Axis");
        arrayList.add("Tri");
        arrayList.add("XL");
        arrayList.add("Charger");
        arrayList.add("STB");
        arrayList.add("Anti Crack");
        arrayList.add("Autofocus");

        // Setup tab layout
        tabLayout.setupWithViewPager(viewPager);

        // Prepare view pager
        prepareViewPager(viewPager, arrayList);
//        viewPager = view.findViewById(R.id.viewPager);
//        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(),
//                tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AddProdukActivity.class));
            }
        });

        return view;
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {
        // Initialize main adapter
        MainAdapter adapter = new MainAdapter(getActivity().getSupportFragmentManager());

        // Initialize main fragment
        ProdukFilterFragment mainFragment = new ProdukFilterFragment();

        // Use for loop
        for (int i = 0; i < arrayList.size(); i++) {
            // Initialize bundle
            Bundle bundle = new Bundle();

            // Put title
            bundle.putString("title", arrayList.get(i));

            // set argument
            mainFragment.setArguments(bundle);

            // Add fragment
            adapter.addFragment(mainFragment, arrayList.get(i));
            mainFragment = new ProdukFilterFragment();
        }
        // set adapter
        viewPager.setAdapter(adapter);
    }

    private class MainAdapter extends FragmentPagerAdapter {
        // Initialize arrayList
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> stringArrayList = new ArrayList<>();

//        int[] imageList={R.drawable.ic_baseline_stop_24,R.drawable.ic_baseline_stop_24,R.drawable.ic_baseline_stop_24};

        // Create constructor
        public void addFragment(Fragment fragment, String s) {
            // Add fragment
            fragmentArrayList.add(fragment);
            // Add title
            stringArrayList.add(s);
        }

        public MainAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            // return fragment position
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            // Return fragment array list size
            return fragmentArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            // Initialize drawable
//            Drawable drawable= ContextCompat.getDrawable(getContext()
//                    ,imageList[position]);

            // set bound
//            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),
//                    drawable.getIntrinsicHeight());

            // Initialize spannable image
            SpannableString spannableString = new SpannableString("" + stringArrayList.get(position));

            // Initialize image span
//            ImageSpan imageSpan=new ImageSpan(drawable,ImageSpan.ALIGN_BOTTOM);

            // Set span
            spannableString.setSpan(null, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // return spannable string
            return spannableString;
        }
    }
}