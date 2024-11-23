package com.example.fixcarapp.TaoYeuCau;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fixcarapp.ApiService;
import com.example.fixcarapp.MapActivity;
import com.example.fixcarapp.R;
import com.example.fixcarapp.ScenePhoto;
import com.example.fixcarapp.TaoYeuCau.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendRequestFragment extends Fragment implements LocationListener {
    private static final int REQUEST_READ_STORAGE = 100;
    private static final int REQUEST_FINE_LOCATION = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 102;
    private LocationManager locationManager;
    private EditText edtPhone, edtIncident,edtProblem;
    private double longitude, latitude;
    private String currentLocation,vehicle;
    private TextView tvLocation;
    private ImageView imvLocation,imvScenePhoto;
    private Button btnCamera,btnPhotoGallery,btnSendRequest;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRequestsRef = database.getReference("Requests");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private Uri photoGalleryUri,cameraUri,imageToUseUri;
    private ProgressDialog progressDialog;

    ActivityResultLauncher<Uri> activityResultLauncherCamera;
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
                Glide.with(requireActivity().getApplicationContext()).load(uriPhoto).into(imvScenePhoto);
            }else {
                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    });
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_send_request, container, false);
        Spinner spinTypeVehicle = (Spinner) view.findViewById(R.id.spinTypeVehicle);
        ImageView imgClose = view.findViewById(R.id.imgGoBack);
        List<String> vehicleTypes = Arrays.asList("Xe máy", "Ô tô", "Xe tải");

        // Tạo adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, vehicleTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTypeVehicle.setAdapter(adapter);
        spinTypeVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedVehicle = parentView.getItemAtPosition(position).toString();
                vehicle = selectedVehicle;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        imgClose.setOnClickListener(view1 -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        tvLocation =view.findViewById(R.id.tvLocation);
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        } else {
            startLocationUpdates();
        }

        imvLocation = view.findViewById(R.id.imvLocation);
        imvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), MapActivity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                intent.putExtra("currentLocation", currentLocation);
                startActivity(intent);
            }
        });

        edtPhone = view.findViewById(R.id.edtPhone);
        edtIncident = view.findViewById(R.id.edtIncident);
        edtProblem = view.findViewById(R.id.edtProblem);
        btnCamera = view.findViewById(R.id.btnCamera);
        btnPhotoGallery = view.findViewById(R.id.btnPhotoGallery);
        imvScenePhoto = view.findViewById(R.id.imvScenePhoto);
        progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Vui lòng đợi trong giây lát ...");

        btnPhotoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10 (API 29) và cao hơn
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_READ_STORAGE);
                    } else {
                        openPhotoGallery();
                    }
                } else {
                    // Android 9 (API 28) và thấp hơn
                    if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE);
                    } else {
                        openPhotoGallery();
                    }
                }

            }
        });

        btnSendRequest = view.findViewById(R.id.btnSendRequest);
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
        registerCameraLauncher();
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, hh:mm:ss dd/MM/yyyy", new Locale("vi", "VN"));
        String formattedDate = dateFormat.format(currentDate);
        Log.e("time", formattedDate);

        return view;
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }else {
            cameraUri = createUri();
            activityResultLauncherCamera.launch(cameraUri);
        }
    }

    private Uri createUri(){
        File imageFile = new File(requireActivity().getApplicationContext().getFilesDir(), "camera_photo.jpg");
        return FileProvider.getUriForFile(
                requireActivity().getApplicationContext(),
                "com.example.fixcarapp.fileProvider",
                imageFile
        );
    }

    private void registerCameraLauncher(){
        activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.TakePicture(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result != null && result) {
                    imvScenePhoto.setImageURI(cameraUri);
                } else {
                    Toast.makeText(requireActivity(), "Lỗi khi mở camera:", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void sendRequest() {
        progressDialog.show();
        if (!validateField()) {
            return;
        }
        // Chọn Uri ảnh phù hợp
        imageToUseUri = (photoGalleryUri != null) ? photoGalleryUri : cameraUri;
        try {
            MultipartBody.Part muPartBodyScenePhoto;

            if (imageToUseUri != null) {
                // Chuyển đổi URI thành InputStream
                InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageToUseUri);

                if (inputStream != null) {
                    RequestBody requestBodyScenePhoto = RequestBody.create(MediaType.parse("multipart/form-data"), getBytes(inputStream));
                    muPartBodyScenePhoto = MultipartBody.Part.createFormData("image", getFileName(imageToUseUri), requestBodyScenePhoto);
                } else {
                    muPartBodyScenePhoto = MultipartBody.Part.createFormData("image", "", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
                }
            } else {
                // Gửi chuỗi rỗng nếu ảnh không được chọn
                muPartBodyScenePhoto = MultipartBody.Part.createFormData("image", "", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
            }

            // Gửi ảnh lên API
            apiUploadImage(muPartBodyScenePhoto);

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(requireActivity(), "Lỗi xử lý ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void apiUploadImage(MultipartBody.Part muPartBodyScenePhoto){
        ApiService.apiService.uploadScenePhoto(muPartBodyScenePhoto).enqueue(new Callback<ScenePhoto>() {
            @Override
            public void onResponse(Call<ScenePhoto> call, Response<ScenePhoto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ScenePhoto scenePhotoRes = response.body();
                    String scenePhoto = scenePhotoRes.getImage();
                    String phone = edtPhone.getText().toString();
                    String incident = edtIncident.getText().toString();
                    String problem = edtProblem.getText().toString();
                    createRequest(phone, incident, problem, scenePhoto);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(requireActivity(), "Tạo mới yêu cầu không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ScenePhoto> call, Throwable throwable) {
                progressDialog.dismiss();
                Toast.makeText(requireActivity(), "Gọi API thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createRequest(String phone, String incident, String problem, String scenePhoto){
        myRequestsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int nextId = 1;
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String lastKey = child.getKey();
                        if (lastKey != null) {
                            nextId = Integer.parseInt(lastKey) + 1;
                        }
                    }
                }
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, hh:mm:ss dd/MM/yyyy", new Locale("vi", "VN"));
                String time = dateFormat.format(currentDate);
                Request request = new Request(nextId,phone, incident, problem, longitude, latitude,currentLocation, vehicle,scenePhoto, "PENDING",1,user.getEmail(),time);
                myRequestsRef.child(String.valueOf(nextId)).setValue(request, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        resetField();
                        if (error == null) {
                            Toast.makeText(requireActivity(), "Tạo yêu cầu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(), "Lỗi khi tạo yêu cầu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(requireActivity(), "Lỗi khi truy cập dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validateField() {
        if (edtPhone.getText().toString().trim().isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(requireActivity(), "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            edtPhone.requestFocus();
            return false;
        }
        if (edtIncident.getText().toString().trim().isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(requireActivity(), "Vui lòng nhập sự cố", Toast.LENGTH_SHORT).show();
            edtIncident.requestFocus();
            return false;
        }
        if (edtProblem.getText().toString().trim().isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(requireActivity(), "Vui lòng nhập tình trạng", Toast.LENGTH_SHORT).show();
            edtProblem.requestFocus();
            return false;
        }
        return true;
    }

    public void resetField(){
        progressDialog.dismiss();
        photoGalleryUri = null;
        cameraUri = null;
        imageToUseUri = null;
        edtPhone.setText("");
        edtIncident.setText("");
        edtPhone.requestFocus();
        edtProblem.setText("");
        imvScenePhoto.setImageResource(R.drawable.ic_notfield);
    }

    // Chuyển đổi InputStream thành byte[]
    @NonNull
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
            Cursor cursor = requireActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                fileName = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        return fileName.isEmpty() ? "image.jpg" : fileName;
    }

    private void openPhotoGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncherPhotoGallery.launch(intent);
        } catch (Exception e) {
            Toast.makeText(requireActivity(), "Không thể truy cập tệp ảnh. Vui lòng kiểm tra quyền truy cập.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        getAddressFromLocation(latitude, longitude);
    }
    public void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                currentLocation = addressText;
                tvLocation.setText(addressText);
            } else {
                tvLocation.setText("Không thể lấy vị trí.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            tvLocation.setText("Lỗi khi lấy vị trí.");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openPhotoGallery();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(requireActivity(), "Bạn cần cấp quyền trong Cài đặt để sử dụng chức năng này.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireActivity(), "Bạn cần cấp quyền để chọn tệp ảnh.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
                    Toast.makeText(requireActivity(), "Bạn cần cấp quyền trong Cài đặt để sử dụng chức năng này.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireActivity(), "Bạn cần cấp quyền để mở camera.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(requireActivity(), "Bạn cần cấp quyền trong Cài đặt để sử dụng chức năng này.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireActivity(), "Bạn cần cấp quyền để mở vị trí hiện tại.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}