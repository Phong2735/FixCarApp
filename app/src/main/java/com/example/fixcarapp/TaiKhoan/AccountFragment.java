package com.example.fixcarapp.TaiKhoan;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fixcarapp.DangNhap.LoginActivity;
import com.example.fixcarapp.TrungTam.DanhSachTrungTam.CenterInformationFragment;
import com.example.fixcarapp.R;
import com.example.fixcarapp.TrungTam.RescueCenterActitvity;
import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment {
    private ImageView imgLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        imgLogout = view.findViewById(R.id.imgLogout);
        imgLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        return view;
    }
}