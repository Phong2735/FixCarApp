package com.example.fixcarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etName, etEmail, etPassword, etPasswordConfirm;
    private Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Liên kết các view
        etName = findViewById(R.id.et_NameRegister);
        etEmail = findViewById(R.id.et_EmailRegister);
        etPassword = findViewById(R.id.et_PasswordRegister);
        etPasswordConfirm = findViewById(R.id.et_PasswordRegister2);
        btRegister = findViewById(R.id.bt_Register);

        // Thiết lập sự kiện cho nút Đăng ký
        btRegister.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        // Lấy dữ liệu từ các EditText
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPasswordConfirm.getText().toString().trim();

        // Kiểm tra các trường thông tin và mật khẩu xác nhận
        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            Toast.makeText(Register.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordConfirm)) {
            Toast.makeText(Register.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng ký người dùng với Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng ký thành công, lấy thông tin người dùng
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển đến màn hình đăng nhập hoặc màn hình chính sau khi đăng ký thành công
                        startActivity(new Intent(Register.this, Login.class)); // Thay Login.class bằng màn hình bạn muốn điều hướng tới
                        finish(); // Đóng màn hình đăng ký
                    } else {
                        // Đăng ký thất bại
                        Toast.makeText(Register.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
