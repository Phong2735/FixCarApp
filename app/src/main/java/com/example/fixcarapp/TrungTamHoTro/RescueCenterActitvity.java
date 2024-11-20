package com.example.fixcarapp.TrungTamHoTro;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixcarapp.CenterInformationFragment;
import com.example.fixcarapp.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RescueCenterActitvity extends AppCompatActivity {
    TextView tvName,tvWarning,tvUpdate,tvChangePass;
    RecyclerView rcvListRequest;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_center_actitvity);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        tvName = findViewById(R.id.tvName1);
        tvUpdate = findViewById(R.id.tvUpdate);
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

        rcvListRequest = findViewById(R.id.rcvListRequest);



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
                    if (oldPassword == newPassword) {
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