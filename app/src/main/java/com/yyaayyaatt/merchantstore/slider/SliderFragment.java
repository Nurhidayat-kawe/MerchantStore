package com.yyaayyaatt.merchantstore.slider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.yyaayyaatt.merchantstore.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SliderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SliderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "imgSlider";
    private String imageUrls;

    // TODO: Rename and change types of parameters

    public SliderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SliderFragment newInstance(String param1) {
        SliderFragment fragment = new SliderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imageUrls = getArguments().getString(ARG_PARAM1);
        View view = inflater.inflate(R.layout.fragment_slider, container, false);
        ImageView img = view.findViewById(R.id.img);
        Glide.with(getActivity())
                .load(imageUrls)
                .placeholder(R.drawable.noimage)
                .into(img);
        return view;
    }
}