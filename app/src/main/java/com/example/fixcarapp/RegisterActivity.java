package com.example.fixcarapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText etName, etEmail, etPassword, etPasswordConfirm;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.txtiename);
        etEmail = findViewById(R.id.txtieemail);
        etPassword = findViewById(R.id.txtiep);
        etPasswordConfirm = findViewById(R.id.txtierp);
        btnRegister = findViewById(R.id.btnRegister);
        TextView tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        });
        btnRegister.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        // Lấy dữ liệu từ các EditText
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPasswordConfirm.getText().toString().trim();

        // Kiểm tra các trường thông tin và mật khẩu xác nhận
        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng ký người dùng với Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công, lấy thông tin người dùng
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class)); // Thay Login.class bằng màn hình bạn muốn điều hướng tới
                        finish(); // Đóng màn hình đăng ký
                    } else {
                        // Đăng ký thất bại
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
