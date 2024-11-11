package com.example.fixcarapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        TextView tvAddRequest = view.findViewById(R.id.tvAddRequest);
        Date currentDate = new Date();

        // Định dạng ngày giờ theo tiếng Việt
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", new Locale("vi", "VN"));
        String formattedDate = dateFormat.format(currentDate);
        tvDate.setText(formattedDate);
        tvAddRequest.setOnClickListener(view1 -> {
            SendRequestFragment sendRequestFragment = new SendRequestFragment();
            replaceFragment(sendRequestFragment);
        });
        return view;
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}