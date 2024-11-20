package com.example.fixcarapp.TaiKhoan;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.fixcarapp.CenterInformationFragment;
import com.example.fixcarapp.R;

import org.checkerframework.checker.units.qual.C;

public class AccountFragment extends Fragment {
    TextView tvAddCenterHelper,tvUpdate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        tvAddCenterHelper = view.findViewById(R.id.tvAddCenterHelper);
        tvUpdate = view.findViewById(R.id.tvUpdate);
        tvAddCenterHelper.setOnClickListener(view1 -> {
            CenterInformationFragment centerInformationFragment = new CenterInformationFragment();
            centerInformationFragment.show(getChildFragmentManager(),"Information Center");
        });
        return view;
    }
}