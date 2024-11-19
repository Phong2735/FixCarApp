package com.example.fixcarapp;

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

import com.example.fixcarapp.TaoYeuCau.SendRequestFragment;
import com.example.fixcarapp.TrungTamHoTro.ListServiceFragment;

public class CenterDetailFragment extends Fragment {
    TextView tvName,tvSdt,tvAdress,tvDescription,tvEmail;
    ImageView ingBack;
    ImageView imgLogo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            byte[] logo = bundle.getByteArray("logo");
            String ten = bundle.getString("ten");
            String sdt = bundle.getString("sdt");
            String diachi = bundle.getString("diachi");
            String email = bundle.getString("email");
            String mota = bundle.getString("mota");

            // Cập nhật UI với dữ liệu
            tvName.setText(ten);
            tvSdt.setText(sdt);
            tvAdress.setText(diachi);
            tvEmail.setText(email);
            tvDescription.setText(mota);

            // Chuyển đổi logo từ byte[] thành Bitmap và đặt vào ImageView
            if (logo != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(logo, 0, logo.length);
                imgLogo.setImageBitmap(bitmap);
            }
        }
        tvAddRequest.setOnClickListener(view1 -> {
            SendRequestFragment sendRequestFragment = new SendRequestFragment();
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