package com.example.fixcarapp.TrungTam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fixcarapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {

    private TextView tvId, tvAddress, tvProblem, tvVehicle, tvPhone, tvEmail;
    private ImageView imgScenePhoto, imgvBack, imgvPhone;
    private Button btnComplete;
    private int requestId;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize UI components
        tvId = findViewById(R.id.tvId);
        tvAddress = findViewById(R.id.tvAddress);
        tvProblem = findViewById(R.id.tvProblem);
        tvVehicle = findViewById(R.id.tvVehicle);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        imgScenePhoto = findViewById(R.id.imgScenePhoto);
        imgvBack = findViewById(R.id.imgvBack);
        imgvPhone = findViewById(R.id.imgvPhone);
        btnComplete = findViewById(R.id.btnComplete);

        // Get the data passed from the previous activity
        Intent intent = getIntent();
        requestId = intent.getIntExtra("REQUEST_ID", -1);
        String address = intent.getStringExtra("ADDRESS");
        String problem = intent.getStringExtra("PROBLEM");
        String vehicle = intent.getStringExtra("VEHICLE");
        String phone = intent.getStringExtra("PHONE");
        String email = intent.getStringExtra("EMAIL");
        String scenePhoto = intent.getStringExtra("SCENE_PHOTO");

        // Set the data to views
        tvId.setText(String.valueOf(requestId));
        tvAddress.setText(address);
        tvProblem.setText(problem);
        tvVehicle.setText(vehicle);
        tvPhone.setText(phone);
        tvEmail.setText(email);

        // Load the image using Glide
        Glide.with(this)
                .load(scenePhoto)
                .into(imgScenePhoto);


        imgvBack.setOnClickListener(v -> onBackPressed()); // trở về trang trước


        // Phone button functionality: Gọi điện cho số điện thoại
        imgvPhone.setOnClickListener(v -> {
            String phoneNumber = tvPhone.getText().toString();
            if (!phoneNumber.isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            } else {
                Toast.makeText(DetailActivity.this, "Phone number is not available", Toast.LENGTH_SHORT).show();
            }
        });


        // Complete button functionality
        btnComplete.setOnClickListener(v -> {
            // Mark the request as completed (update Firebase)
            updateRequestStatus(requestId);
        });
    }

    private void updateRequestStatus(int requestId) {
        // Reference to Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests");

        // Update status to "COMPLETED"
        databaseReference.child(String.valueOf(requestId)).child("status").setValue("COMPLETED")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(DetailActivity.this, "Trạng thái đã được cập nhật thành COMPLETED", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DetailActivity.this, "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                });
    }
}
