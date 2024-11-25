package com.example.fixcarapp.TrungTam.DanhSachTrungTam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fixcarapp.R;
import  com.example.fixcarapp.TaoYeuCau.SendRequestFragment;

public class CenterDetailFragment extends Fragment {
    TextView tvName,tvSdt,tvAdress,tvDescription,tvEmail;
    ImageView ingBack;
    ImageView imgLogo;
    public static CenterDetailFragment newInstance(Item_Center itemCenter)
    {
        Bundle args = new Bundle();
        CenterDetailFragment fragment = new CenterDetailFragment();
        args.putSerializable("item_center",itemCenter);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_center_detail, container, false);
        tvName = view.findViewById(R.id.tvName);
        tvSdt = view.findViewById(R.id.tvSdt);
        tvDescription = view.findViewById(R.id.tvMota);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAdress = view.findViewById(R.id.tvAdress);
        ingBack = view.findViewById(R.id.imgGoBack);
        imgLogo = view.findViewById(R.id.imgLogo);
        TextView tvAddRequest = view.findViewById(R.id.tvAddRequest);
        ingBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
        Bundle args = getArguments();
        if (args != null) {
            Item_Center itemCenter = (Item_Center) args.getSerializable("item_center");
            if (itemCenter != null) {
                tvName.setText(itemCenter.getTenCenter());
                tvAdress.setText(itemCenter.getDiachiCenter());
                tvDescription.setText(itemCenter.getMota());
                tvEmail.setText("Email:  "+itemCenter.getEmail());
            }
        }
        tvAddRequest.setOnClickListener(view1 -> {
            Item_Center itemCenter = (Item_Center) args.getSerializable("item_center");
            SendRequestFragment sendRequestFragment = SendRequestFragment.newInstance(itemCenter);
            replaceFragment(sendRequestFragment);
        });
        return view;
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout1,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}