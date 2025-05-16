package com.yyaayyaatt.merchantstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.adapter.ProdukRecylerAdapter;
import com.yyaayyaatt.merchantstore.model.Kategori;
import com.yyaayyaatt.merchantstore.model.Produk;
import com.yyaayyaatt.merchantstore.model.ResponseProduk;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProdukFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProdukFilterFragment extends Fragment {

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    List<Produk> produks = new ArrayList<>();
    List<Kategori> kategoris = new ArrayList<>();
    List<String> katStrings = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private final SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    AppCompatButton btn_add, btn_cari;
    EditText ed_cari;
    String kat;

    private Parcelable recyclerViewState;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProdukFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProdukFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProdukFilterFragment newInstance(String param1, String param2) {
        ProdukFilterFragment fragment = new ProdukFilterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produk_filter, container, false);
        kat = getArguments().getString("title");
        btn_cari = view.findViewById(R.id.btn_produk_cari);
        ed_cari = view.findViewById(R.id.ed_produk_cari);

        mRecycler = view.findViewById(R.id.rv_produk);
        mContext = view.getContext();
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        setHasOptionsMenu(true);
        mRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false));
        } else {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        }
        progressDialog.setMessage("Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getProduks("", kat);

        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = ed_cari.getText().toString();
                getProduks(key, kat);
            }
        });
        return view;
    }

    private void getProduks(String key, String kat) {
        produks.clear();
        Call<ResponseProduk> getdata = mApiService.getProduks(key, kat);
        getdata.enqueue(new Callback<ResponseProduk>() {
            @Override
            public void onResponse(Call<ResponseProduk> call, Response<ResponseProduk> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getmKode().equals("1")) {
                        produks = response.body().getResult();
                        if (!produks.isEmpty()) {
                            mAdapter = new ProdukRecylerAdapter(mContext, produks);
                            mRecycler.setAdapter(mAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseProduk> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        progressDialog.setMessage("Tunggu...");
//        progressDialog.setCancelable(true);
//        progressDialog.show();
//        String key = ed_cari.getText().toString();
//        getProduks(key,kat);
//        mAdapter.notifyDataSetChanged();
//        mRecycler.scrollToPosition(mAdapter.getItemCount()-1);
    }
}