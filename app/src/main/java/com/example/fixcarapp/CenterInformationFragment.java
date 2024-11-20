package com.example.fixcarapp;

import android.app.Activity;
import android.content.ContentValues;
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

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CenterInformationFragment extends DialogFragment {
    EditText edtName,edtAdress,edtSDT,edtEmail,edtDescription;
    Button btnAdd,btnUpdate,btnDelete;
    ImageView imgLogo,imgBack;
    DBHelper dbHelper;
    byte[] logo;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center_information, container, false);
        edtName = view.findViewById(R.id.edtName);
        edtAdress = view.findViewById(R.id.edtAdress);
        edtDescription = view.findViewById(R.id.edtDescription);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtSDT = view.findViewById(R.id.edtSDT);
        imgLogo = view.findViewById(R.id.imgLogo);
        imgBack = view.findViewById(R.id.imgBack);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setVisibility(View.VISIBLE);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        imgBack.setOnClickListener(v-> {
            dismiss();
        });
        imgLogo.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });
        dbHelper = new DBHelper(getActivity());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String adress = edtAdress.getText().toString();
                String sdt = edtSDT.getText().toString();
                String email = edtEmail.getText().toString();
                String mota = edtDescription.getText().toString();
                String error="";
                if(email.isEmpty()||name.isEmpty()||sdt.isEmpty()||adress.isEmpty()) {
                    error = "Không được để trống";
                    if (name.isEmpty())
                        error = error + " tên trung tâm,";
                    if (sdt.isEmpty())
                        error = error + " số điện thoại,";
                    if (adress.isEmpty())
                        error = error + " địa chỉ, ";
                    if (email.isEmpty())
                        error = error + " email ";
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                }
                else {
                    Snackbar.make(view,"Thành công",Snackbar.LENGTH_LONG)
                            .setAction("Đồng ý", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dbHelper.insertCenter(logo,name,sdt,adress,email,mota);
//                                    Item_Center center = new Item_Center();
//                                    center.setLogo(logo);
//                                    center.setTenCenter(name);
//                                    center.setDiachiCenter(adress);
//                                    center.setSdt(sdt);
//                                    center.setMota(mota);
//                                    center.setEmail(email);
                                    dismiss();
                                }
                            }).show();
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            String name = edtName.getText().toString();
            String adress = edtAdress.getText().toString();
            String sdt = edtSDT.getText().toString();
            String email = edtEmail.getText().toString();
            String mota = edtDescription.getText().toString();
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put("tenCenter",name);
                values.put("tenCenter",name);
                values.put("tenCenter",name);
                values.put("tenCenter",name);
                values.put("tenCenter",name);

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
                    logo = imageUriToByteArray(selectedImage);
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
    public void updateCenter()
    {
        if(getView()!=null) {
            btnUpdate.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            imgBack.setOnClickListener(v -> {
                dismiss();
            });
            imgLogo.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);
            });
            dbHelper = new DBHelper(getActivity());
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = edtName.getText().toString();
                    String adress = edtAdress.getText().toString();
                    String sdt = edtSDT.getText().toString();
                    String email = edtEmail.getText().toString();
                    String mota = edtDescription.getText().toString();
                    String error = "";
                    if (email.isEmpty() || name.isEmpty() || sdt.isEmpty() || adress.isEmpty()) {
                        error = "Không được để trống";
                        if (name.isEmpty())
                            error = error + " tên trung tâm,";
                        if (sdt.isEmpty())
                            error = error + " số điện thoại,";
                        if (adress.isEmpty())
                            error = error + " địa chỉ, ";
                        if (email.isEmpty())
                            error = error + " email ";
                        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(view, "Thành công", Snackbar.LENGTH_LONG)
                                .setAction("Đồng ý", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dbHelper.insertCenter(logo, name, sdt, adress, email, mota);
//                                    Item_Center center = new Item_Center();
//                                    center.setLogo(logo);
//                                    center.setTenCenter(name);
//                                    center.setDiachiCenter(adress);
//                                    center.setSdt(sdt);
//                                    center.setMota(mota);
//                                    center.setEmail(email);
                                        dismiss();
                                    }
                                }).show();
                    }
                }
            });
        }
    }
}