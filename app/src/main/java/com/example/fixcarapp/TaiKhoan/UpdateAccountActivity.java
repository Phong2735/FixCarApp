package com.example.fixcarapp.TaiKhoan;

import static java.security.AccessController.getContext;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.fixcarapp.ApiService;
import com.example.fixcarapp.R;
import com.example.fixcarapp.ScenePhoto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAccountActivity extends AppCompatActivity {
    private static final int REQUEST_READ_STORAGE = 100;
    private ImageView imvGoBack,imvAvatar;
    private EditText edtName,edtLicensePlate,edtPhone;
    private Button btnUpdate,btnAvatar;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myUsersRef = database.getReference("Users");
    private Uri photoGalleryUri;
    private ProgressDialog progressDialog;
    private String currentAvatar;

    ActivityResultLauncher<Intent> activityResultLauncherPhotoGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                if(data == null){
                    return;
                }
                Uri uriPhoto = data.getData();
                photoGalleryUri = uriPhoto;
                Glide.with(getApplicationContext()).load(uriPhoto).into(imvAvatar);
            }else {
                Toast.makeText(UpdateAccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imvGoBack = findViewById(R.id.imvGoBack);
        imvGoBack.setOnClickListener(v -> {
            onBackPressed();
        });

        findUserByEmail();

        edtName = findViewById(R.id.edtName);
        edtLicensePlate = findViewById(R.id.edtLicensePlate);
        edtPhone = findViewById(R.id.edtPhone);
        imvAvatar = findViewById(R.id.imvAvatar);
        btnAvatar = findViewById(R.id.btnAvatar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng đợi trong giây lát ...");

        btnAvatar.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10 (API 29) và cao hơn
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_READ_STORAGE);
                } else {
                    openPhotoGallery();
                }
            } else {
                // Android 9 (API 28) và thấp hơn
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
                } else {
                    openPhotoGallery();
                }
            }
        });


        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(v -> {
            updateAccount();
        });
    }

    private void updateAccount() {
        progressDialog.show();
        try {
            MultipartBody.Part muPartBodyAvatar;
            if (photoGalleryUri != null) {
                InputStream inputStream = getContentResolver().openInputStream(photoGalleryUri);
                if (inputStream != null) {
                    RequestBody requestBodyScenePhoto = RequestBody.create(MediaType.parse("multipart/form-data"), getBytes(inputStream));
                    muPartBodyAvatar = MultipartBody.Part.createFormData("image", getFileName(photoGalleryUri), requestBodyScenePhoto);
                } else {
                    muPartBodyAvatar = MultipartBody.Part.createFormData("image", currentAvatar, RequestBody.create(MediaType.parse("multipart/form-data"), currentAvatar));
                }
                apiUploadImage(muPartBodyAvatar);
            } else {
                String name = edtName.getText().toString();
                String licensePlate = edtLicensePlate.getText().toString();
                String phone = edtPhone.getText().toString();
                updateAccountToDatabase(name,licensePlate,phone,currentAvatar);
            }

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(UpdateAccountActivity.this, "Lỗi xử lý ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void apiUploadImage(MultipartBody.Part muPartBodyAvatar){
        progressDialog.dismiss();
        ApiService.apiService.upload(muPartBodyAvatar).enqueue(new Callback<ScenePhoto>() {
            @Override
            public void onResponse(Call<ScenePhoto> call, Response<ScenePhoto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ScenePhoto avatarRes = response.body();
                    String avatar = avatarRes.getImage();
                    String name = edtName.getText().toString();
                    String licensePlate = edtLicensePlate.getText().toString();
                    String phone = edtPhone.getText().toString();
                    updateAccountToDatabase(name,licensePlate, phone, avatar);
                } else {
                    Toast.makeText(UpdateAccountActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ScenePhoto> call, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(UpdateAccountActivity.this, "Gọi API thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateAccountToDatabase(String name,String licensePlate,String phone,String avatar){
        progressDialog.dismiss();
        if (user != null) {
            myUsersRef.orderByChild("email").equalTo(user.getEmail())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                progressDialog.dismiss();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String userId = snapshot.getKey();
                                    snapshot.getRef().child("name").setValue(name);
                                    snapshot.getRef().child("licensePlate").setValue(licensePlate);
                                    snapshot.getRef().child("phone").setValue(phone);
                                    snapshot.getRef().child("avatar").setValue(avatar);
                                    progressDialog.dismiss();
                                    photoGalleryUri = null;
                                    Toast.makeText(UpdateAccountActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(UpdateAccountActivity.this, "Không tìm thấy user với email này!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(UpdateAccountActivity.this, "Lỗi truy vấn: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private byte[] getBytes(@NonNull InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private String getFileName(@NonNull Uri uri) {
        String fileName = "";
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                fileName = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        return fileName.isEmpty() ? "image.jpg" : fileName;
    }

    public void openPhotoGallery(){
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncherPhotoGallery.launch(intent);
        } catch (Exception e) {
            Toast.makeText(UpdateAccountActivity.this, "Không thể truy cập tệp ảnh. Vui lòng kiểm tra quyền truy cập.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void findUserByEmail() {
        if (user != null) {
            String currentEmail = user.getEmail();
            myUsersRef.orderByChild("email").equalTo(currentEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User userObject = snapshot.getValue(User.class);
                                    if (userObject != null) {
                                        edtName.setText(userObject.getName());
                                        edtPhone.setText(userObject.getPhone());
                                        edtLicensePlate.setText(userObject.getLicensePlate());
                                        currentAvatar = userObject.getAvatar();
                                        Glide.with(UpdateAccountActivity.this).load(userObject.getAvatar()).placeholder(R.drawable.ic_person).into(imvAvatar);
                                    }
                                }
                            } else {
                                Toast.makeText(UpdateAccountActivity.this, "Không tìm thấy user có email này!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(UpdateAccountActivity.this, "Lỗi truy vấn: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(UpdateAccountActivity.this, "Người dùng chưa đăng nhập!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openPhotoGallery();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(UpdateAccountActivity.this, "Bạn cần cấp quyền trong Cài đặt để sử dụng chức năng này.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UpdateAccountActivity.this, "Bạn cần cấp quyền để chọn tệp ảnh.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}