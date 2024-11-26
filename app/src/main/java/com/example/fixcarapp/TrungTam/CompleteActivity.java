package com.example.fixcarapp.TrungTam;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixcarapp.R;
import com.example.fixcarapp.TaoYeuCau.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompleteActivity extends AppCompatActivity {
    private RecyclerView rcvCompletedRequests;
    private RequestAdapter requestAdapter;
    private List<Request> allRequestList;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        // Lấy userID từ Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userID = currentUser.getUid();  // Lấy userID của người dùng hiện tại
        } else {
            // Nếu không có người dùng đăng nhập, xử lý lỗi hoặc thông báo
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        rcvCompletedRequests = findViewById(R.id.rcvCompletedRequests);
        rcvCompletedRequests.setLayoutManager(new LinearLayoutManager(this));

        allRequestList = new ArrayList<>();
        requestAdapter = new RequestAdapter(this, new ArrayList<>()); // Adapter rỗng ban đầu
        rcvCompletedRequests.setAdapter(requestAdapter);

        // Lấy dữ liệu từ Firebase
        fetchRequestsFromFirebase();
    }

private void fetchRequestsFromFirebase() {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            allRequestList.clear(); // Xóa dữ liệu cũ

            // Lọc yêu cầu theo centerId và trạng thái "COMPLETED"
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                Request request = dataSnapshot.getValue(Request.class);

                if (request != null) {
                    // Kiểm tra centerId có khớp với userID hay không
                    if (request.getCenterId() != null && request.getCenterId().equals(userID)) {
                        // Thêm yêu cầu có centerId khớp với userID và trạng thái là "COMPLETED"
                        if ("COMPLETED".equals(request.getStatus())) {
                            allRequestList.add(request);
                        }
                    }
                }
            }

            // Cập nhật giao diện với danh sách yêu cầu hoàn thành của trung tâm hiện tại
            requestAdapter.setCompletedData(allRequestList);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(CompleteActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}


}
