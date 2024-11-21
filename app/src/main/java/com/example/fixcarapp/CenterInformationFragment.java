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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CenterInformationFragment extends DialogFragment {
    EditText edtName,edtAdress,edtSDT,edtDescription;
    Button btnAdd,btnUpdate,btnDelete;
    ImageView imgLogo,imgBack;
    DBHelper dbHelper;
    byte[] logo;
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            String userID = user.getUid();
            databaseReference.child(userID).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            oldname = task.getResult().child("name").getValue(String.class);
                            email = task.getResult().child("email").getValue(String.class);
                        }
                    });
        }
        edtName.setHint(oldname);
        imgBack.setOnClickListener(v-> {
            dismiss();
        });
        imgLogo.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });
        dbHelper = new DBHelper(getActivity());
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String adress = edtAdress.getText().toString();
                String sdt = edtSDT.getText().toString();
                String mota = edtDescription.getText().toString();
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
                    if (dbHelper.updateCenter(logo, name, sdt, adress, email, mota) > 0) {
                        Snackbar.make(view,"Cập nhật thành công",Snackbar.LENGTH_LONG)
                                .setAction("Đồng ý", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dismiss();
                                    }
                                }).show();
                    }
                    else {
                        Snackbar.make(view,"Cập nhật thất bại",Snackbar.LENGTH_LONG)
                                .setAction("Đồng ý", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dismiss();
                                    }
                                }).show();
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
}