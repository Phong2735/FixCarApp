package com.example.fixcarapp.TrungTam.DanhSachTrungTam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fixcarapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CenterInformationFragment extends DialogFragment {
    EditText edtName,edtAdress,edtSDT,edtDescription;
    Button btnAdd,btnUpdate,btnDelete;
    ImageView imgBack;
    String email,oldname;
    private DatabaseReference databaseReference;
    FirebaseUser user;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center_information, container, false);
        edtName = view.findViewById(R.id.edtName);
        edtAdress = view.findViewById(R.id.edtAdress);
        edtDescription = view.findViewById(R.id.edtDescription);
        edtSDT = view.findViewById(R.id.edtSDT);
        imgBack = view.findViewById(R.id.imgBack);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        Bundle args = getArguments();
        if(args!=null) {
            String name = args.getString("name");
            String sdt = args.getString("sdt");
            String mota = args.getString("mota");
            String diachi = args.getString("diachiCenter");
            edtName.setHint(name);
            edtDescription.setHint(mota);
            edtAdress.setHint(diachi);
            edtSDT.setHint(sdt);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Centers");
        user = FirebaseAuth.getInstance().getCurrentUser();
        imgBack.setOnClickListener(v-> {
            dismiss();
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String name1 = edtName.getHint().toString();
                String adress = edtAdress.getText().toString();
                String adress1 = edtAdress.getHint().toString();
                String sdt = edtSDT.getText().toString();
                String sdt1 = edtSDT.getHint().toString();
                String mota = edtDescription.getText().toString();
                String mota1 = edtDescription.getHint().toString();
                String error = "";
                Map<String, Object> updates = new HashMap<>();
                if(name.isEmpty()) {
                    if(!adress1.isEmpty()) {
                        updates.put("tenCenter", name1);
                    }
                }
                else {
                    updates.put("tenCenter",name);
                }

                if(adress.isEmpty()) {
                    if(!adress1.isEmpty()) {
                        updates.put("diachiCenter", adress1);
                    }
                }
                else {
                    updates.put("diachiCenter",adress);
                }

                if(mota.isEmpty()) {
                    if(!mota1.isEmpty()) {
                        updates.put("mota", mota1);
                    }
                }
                else {
                    updates.put("mota",mota);
                }

                if(sdt.isEmpty()) {
                    if(!sdt1.isEmpty()) {
                        updates.put("sdt", sdt1);
                    }
                }
                else {
                    updates.put("sdt",sdt);
                }
                    if(user!=null)
                    {
                        String userID = user.getUid();
                        databaseReference.child(userID).updateChildren(updates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    String updatedName = snapshot.child("tenCenter").getValue(String.class);
                                                    String updatedAddress = snapshot.child("diachiCenter").getValue(String.class);
                                                    String updatedSdt = snapshot.child("sdt").getValue(String.class);
                                                    String updatedDescription = snapshot.child("mota").getValue(String.class);

                                                    edtName.setHint(updatedName != null ? updatedName : "");
                                                    edtAdress.setHint(updatedAddress != null ? updatedAddress : "");
                                                    edtSDT.setHint(updatedSdt != null ? updatedSdt : "");
                                                    edtDescription.setHint(updatedDescription != null ? updatedDescription : "");
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(getContext(), "Lỗi khi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getContext(), "Cập nhật thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
            }
        });
        return  view;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}