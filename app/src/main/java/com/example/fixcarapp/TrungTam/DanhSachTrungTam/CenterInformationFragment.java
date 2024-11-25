package com.example.fixcarapp.TrungTam.DanhSachTrungTam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CenterInformationFragment extends DialogFragment {
    EditText edtName,edtAdress,edtSDT,edtDescription;
    Button btnAdd,btnUpdate,btnDelete;
    ImageView imgLogo,imgBack;
    String logo;
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
        imgLogo = view.findViewById(R.id.imgLogo);
        imgBack = view.findViewById(R.id.imgBack);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        Bundle args = getArguments();
        if(args!=null) {
            String name = args.getString("name");
            edtName.setHint(name);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Centers");
        user = FirebaseAuth.getInstance().getCurrentUser();
        imgBack.setOnClickListener(v-> {
            dismiss();
        });
        imgLogo.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String adress = edtAdress.getText().toString();
                String sdt = edtSDT.getText().toString();
                String mota = edtDescription.getText().toString();
                String logo = imgLogo.toString();
                String error= "";
                if(logo==null||mota.isEmpty()||sdt.isEmpty()||adress.isEmpty()) {
                    error = "Không được để trống";
                    if (logo==null)
                        error = error + " logo,";
                    if (sdt.isEmpty())
                        error = error + " số điện thoại,";
                    if (mota.isEmpty())
                        error = error + " mô tả, ";
                    if (adress.isEmpty())
                        error = error + " địa chỉ ";
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                }
                else  {
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("logo", logo);
                    updates.put("sdt", sdt);
                    updates.put("mota", mota);
                    updates.put("diachiCenter", adress);
                    if(user!=null)
                    {
                        String userID = user.getUid();
                        databaseReference.child(userID).updateChildren(updates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Cập nhật thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
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
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    imgLogo.setImageURI(selectedImage);
                }
            });
    public byte[] imageUriToByteArray(Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int next = 0;
            while ((next = inputStream.read()) != -1) {
                byteArrayOutputStream.write(next);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}