package com.example.fixcarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button btLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login); // Liên kết layout chính

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Liên kết các view
        etEmail = findViewById(R.id.et_Email);
        etPassword = findViewById(R.id.et_Password);
        btLogin = findViewById(R.id.bt_Login);
        tvRegister = findViewById(R.id.tv_DangKy);

        // Lắng nghe sự kiện click vào nút Đăng nhập
        btLogin.setOnClickListener(view -> {
            loginUser();
        });

        // Lắng nghe sự kiện click vào nút Đăng ký
        tvRegister.setOnClickListener(view -> {
            // Chuyển sang màn hình đăng ký
            startActivity(new Intent(Login.this, Register.class));
        });

        // Thiết lập window insets cho edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Phương thức đăng nhập người dùng
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Kiểm tra nếu email và mật khẩu trống
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Đăng nhập với Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, lấy thông tin người dùng
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        // Chuyển sang màn hình chính hoặc màn hình khác sau khi đăng nhập thành công
                         startActivity(new Intent(Login.this, MainActivity.class));
                    } else {
                        Toast.makeText(Login.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
