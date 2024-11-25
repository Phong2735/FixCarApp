package com.example.fixcarapp.TrangChu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fixcarapp.TrungTam.DanhSachTrungTam.ListServiceFragment;
import com.example.fixcarapp.R;
import com.example.fixcarapp.TaiKhoan.AccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomMenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_menu, container, false);
        BottomNavigationView menu;
        menu = view.findViewById(R.id.bottomNavigationView);
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout1, new HomeFragment())
                    .commit();
        }
        menu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout1,new HomeFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            if (item.getItemId() == R.id.list) {
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ListServiceFragment fragment = new ListServiceFragment();
                fragmentTransaction.replace(R.id.frameLayout1,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            if(item.getItemId() == R.id.account) {
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout1,new AccountFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            return true;
        });
        return view;
    }
    private void replaceChild(Fragment fragment)
    {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout1,new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}