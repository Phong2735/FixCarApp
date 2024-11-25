package com.example.fixcarapp.TaiKhoan;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.fixcarapp.DangNhap.LoginActivity;
import com.example.fixcarapp.LichSuYeuCau.HistoryActivity;
import com.example.fixcarapp.TrungTam.DanhSachTrungTam.CenterInformationFragment;
import com.example.fixcarapp.R;
import com.example.fixcarapp.TrungTam.RescueCenterActitvity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccountFragment extends Fragment {
    private Button btnLogout,btnHistory,btnUpdatePassword,btnUpdateAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN"));
        String formattedDate = dateFormat.format(currentDate);
        tvDate.setText(formattedDate);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(), "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        btnHistory = view.findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), HistoryActivity.class);
            startActivity(intent);
        });

        btnUpdateAccount = view.findViewById(R.id.btnUpdateAccount);
        btnUpdateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), UpdateAccountActivity.class);
            startActivity(intent);
        });

        btnUpdatePassword = view.findViewById(R.id.btnUpdatePassword);
        btnUpdatePassword.setOnClickListener(v -> {
        });
        return view;
    }
}