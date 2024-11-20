package com.example.fixcarapp.DangNhap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fixcarapp.TrangChu.MainActivity;
import com.example.fixcarapp.R;
import com.example.fixcarapp.DangKy.RegisterActivity;
import com.example.fixcarapp.TrungTamHoTro.RescueCenterActitvity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private TextInputEditText etEmail, etPassword;
    private Button btLogin;
    private TextView tvRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        etEmail = findViewById(R.id.txtieu);
        etPassword = findViewById(R.id.txtiep);
        btLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.setCancelable(false); // Không cho phép hủy khi đang xử lý

        btLogin.setOnClickListener(view -> loginUser());
        tvRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            databaseReference.child(userId).get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful() && task1.getResult().exists()) {
                                            Intent intent = new Intent(LoginActivity.this, RescueCenterActitvity.class);
                                            String role = task1.getResult().child("role").getValue(String.class);
                                            if ("Người dùng".equals(role)) {
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            } else if ("Trung tâm cứu hộ".equals(role)) {
                                                startActivity(intent);
                                            }
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
