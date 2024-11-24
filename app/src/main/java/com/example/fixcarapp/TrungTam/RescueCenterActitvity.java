package com.example.fixcarapp.TrungTam;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.request.Request;

import com.example.fixcarapp.TaoYeuCau.Request;

import com.example.fixcarapp.CenterInformationFragment;
import com.example.fixcarapp.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RescueCenterActitvity extends AppCompatActivity {
    TextView tvName,tvWarning,tvUpdate,tvChangePass, tvCompletedRequests;
    RecyclerView rcvListRequest;
    FirebaseUser user;
    private DatabaseReference databaseReference;

    private RequestAdapter requestAdapter;
    private List<Request> requestList;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_center_actitvity);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        tvName = findViewById(R.id.tvName1);
        tvUpdate = findViewById(R.id.tvUpdate);
        tvUpdate.setText("Cập nhật thông tin");
        tvUpdate.setTextColor(Color.BLUE);
        tvUpdate.setPaintFlags(tvUpdate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvChangePass = findViewById(R.id.tvChangePass);
        tvChangePass.setTextColor(Color.BLUE);
        tvChangePass.setPaintFlags(tvUpdate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvWarning = findViewById(R.id.tvWarning);
        tvUpdate.setOnClickListener(view1 -> {
            CenterInformationFragment centerInformationFragment = new CenterInformationFragment();
            centerInformationFragment.show(getSupportFragmentManager(),"Update ");
        });
        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePass();
            }
        });
        if(user!=null)
        {
            String userID = user.getUid();
            databaseReference.child(userID).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            String name = task.getResult().child("name").getValue(String.class);
                            tvName.setVisibility(View.VISIBLE);
                            tvName.setText(name);
                        }
                    });
        }


        tvCompletedRequests = findViewById(R.id.tvCompletedRequests);
        tvCompletedRequests.setOnClickListener(v -> {
            Intent intent = new Intent(RescueCenterActitvity.this, CompleteActivity.class);
            startActivity(intent);
        });



        // Firebase authentication
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        // Reference to Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");

        // Get user information (optional)
        if (user != null) {
            String userID = user.getUid();
            databaseReference.child(userID).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String name = task.getResult().child("name").getValue(String.class);
                    tvName.setText(name);
                    tvName.setVisibility(View.VISIBLE);
                }
            });
        }

        // RecyclerView setup
        rcvListRequest = findViewById(R.id.rcvListRequest);
        requestList = new ArrayList<>();
        requestAdapter = new RequestAdapter(this, requestList);
        rcvListRequest.setLayoutManager(new LinearLayoutManager(this));
        rcvListRequest.setAdapter(requestAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Request> updatedRequestList = new ArrayList<>(); // Tạo danh sách mới để lưu dữ liệu
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Request request = snapshot.getValue(Request.class);
                    if (request != null && !"COMPLETED".equals(request.getStatus())) {
                        updatedRequestList.add(request); // Thêm dữ liệu vào danh sách mới nếu chưa hoàn thành
                    }
                }
                // Cập nhật adapter thông qua phương thức setData
                requestAdapter.setData(updatedRequestList);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("RescueCenter", "Database error: " + databaseError.getMessage());
            }
        });



    }
    private void changePass()
    {
        LayoutInflater inflater =LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.fragment_change_password,null);
        EditText edtOldPass,edtNewPass;
        Button btnAccept;
        TextView tvWarning;
        ImageView imgClose;
        edtOldPass = view.findViewById(R.id.edtOldPass);
        edtNewPass = view.findViewById(R.id.edtNewpass);
        btnAccept = view.findViewById(R.id.btnAccept);
        imgClose = view.findViewById(R.id.imgClose);
        tvWarning = view.findViewById(R.id.tvWarning);
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String oldPassword = edtOldPass.getText().toString().trim();
                    String newPassword = edtNewPass.getText().toString().trim();
                    if(oldPassword == null && newPassword == null)
                    {
                        tvWarning.setVisibility(View.VISIBLE);
                        tvWarning.setText("Vui lòng nhập đủ mật khẩu cũ và mật khẩu mới");
                    }
                    if (oldPassword.equals(newPassword)) {
                        tvWarning.setVisibility(View.VISIBLE);
                        tvWarning.setText("Mật khẩu mới phải khác mật khẩu cũ");
                        return; // Dừng lại nếu mật khẩu cũ và mới giống nhau
                    }
                    if (user != null) {
                        String email = user.getEmail();
                        if (!email.isEmpty() && !oldPassword.isEmpty() && !newPassword.isEmpty()) {
                            AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
                            user.reauthenticate(credential).addOnCompleteListener(reauthTask -> {
                                if (reauthTask.isSuccessful()) {
                                    // Nếu xác thực lại thành công, cập nhật mật khẩu
                                    user.updatePassword(newPassword)
                                            .addOnCompleteListener(updateTask -> {
                                                if (updateTask.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Đổi mật khẩu thất bại: " + updateTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            });
                                } else {
                                    tvWarning.setVisibility(View.VISIBLE);
                                    tvWarning.setText("Mật khẩu cũ không đúng");
                                }
                            });
                        } else {
                            tvWarning.setVisibility(View.VISIBLE);
                            tvWarning.setText("Vui lòng nhập đủ mật khẩu cũ và mật khẩu mới");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        imgClose.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
    }
}