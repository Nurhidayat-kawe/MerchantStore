package com.yyaayyaatt.merchantstore.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yyaayyaatt.merchantstore.R;
import com.yyaayyaatt.merchantstore.service.BaseApiService;
import com.yyaayyaatt.merchantstore.service.SharedPrefManager;
import com.yyaayyaatt.merchantstore.service.UtilsApi;

public class EbookFragment extends Fragment {

    BaseApiService mApiService;
    SharedPrefManager sharedPrefManager;
    ProgressDialog progressDialog;
    Context mContext;
    //    List<Books> booksList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter adapter;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private final SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ebook, container, false);

        mRecycler = view.findViewById(R.id.rvBooks);
        mContext = view.getContext();
        progressDialog = new ProgressDialog(mContext);
        mApiService = UtilsApi.getAPIService();
        sharedPrefManager = new SharedPrefManager(mContext);

        setHasOptionsMenu(true);
        mRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 2, RecyclerView.VERTICAL, false));
        } else {
            mRecycler.setLayoutManager(new GridLayoutManager(mContext, 3, RecyclerView.VERTICAL, false));
        }
        progressDialog.setMessage("Tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
//        getBooks();
        return view;
    }

//    private void getBooks() {
//        booksList.clear();
//        Call<ResponseBooks> getdata = mApiService.getBooks();
//        getdata.enqueue(new Callback<ResponseBooks>() {
//            @Override
//            public void onResponse(Call<ResponseBooks> call, Response<ResponseBooks> response) {
//                if (response.isSuccessful()) {
//                    progressDialog.dismiss();
//                    if (response.body().getmKode().equals("1")) {
//                        booksList = response.body().getResult();
//                        if (!booksList.isEmpty()) {
//                            mAdapter = new BooksRecylerAdapter(mContext, booksList);
//                            mRecycler.setAdapter(mAdapter);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBooks> call, Throwable t) {
//                Log.e("debug", "onFailure: ERROR > " + t.toString());
//                progressDialog.dismiss();
//            }
//        });
//    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        //Memanggil/Memasang menu item pada toolbar dari layout menu_bar.xml
//        inflater.inflate(R.menu.menu_daftar_ebook, menu);
//        MenuItem searchItem = menu.findItem(R.id.search_buku);
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
//        if(newText.isEmpty()){
//            getBooks();
//        }
//                    return true;
//                }
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Log.i("onQueryTextSubmit", query);
//                    Toast.makeText(mContext, query, Toast.LENGTH_SHORT).show();
//                        getBooksBy(query);
//                    return true;
//                }
//            };
//            searchView.setOnQueryTextListener(queryTextListener);
//        }
//        super.onCreateOptionsMenu(menu,inflater);
//    }
//
//    private void getBooksBy(String key) {
//        booksList.clear();
//        Call<ResponseBooks> getdata = mApiService.getBookByKey(key);
//        getdata.enqueue(new Callback<ResponseBooks>() {
//            @Override
//            public void onResponse(Call<ResponseBooks> call, Response<ResponseBooks> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().getmKode().equals("1")) {
//                        booksList = response.body().getResult();
//                        if (!booksList.isEmpty()) {
//                            adapter = new BooksAllRecylerAdapter(mContext, booksList);
//                            mRecycler.setAdapter(adapter);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBooks> call, Throwable t) {
//                Log.e("debug", "onFailure: ERROR > " + t.toString());
//            }
//        });
//    }
}