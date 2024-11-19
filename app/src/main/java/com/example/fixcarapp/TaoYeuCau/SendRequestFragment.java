package com.example.fixcarapp.TaoYeuCau;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.fixcarapp.R;

import java.util.Arrays;
import java.util.List;

public class SendRequestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_send_request, container, false);
        Spinner spinTypeVehicle = (Spinner) view.findViewById(R.id.spinTypeVehicle);
        ImageView imgClose = view.findViewById(R.id.imgGoBack);
        List<String> vehicleTypes = Arrays.asList("Xe máy", "Ô tô", "Xe tải");

        // Tạo adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, vehicleTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTypeVehicle.setAdapter(adapter);
        spinTypeVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedVehicle = parentView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        imgClose.setOnClickListener(view1 -> {
            getParentFragmentManager().popBackStack();
        });
        return view;
    }
}