package com.example.fixcarapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixcarapp.TaoYeuCau.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private RecyclerView rcvHistoryRequests;
    private HistoryAdapter historyAdapter;
    private ArrayList<Request> requestList;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRequestsRef = database.getReference("Requests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.history), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        rcvHistoryRequests = findViewById(R.id.rcvHistoryRequests);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvHistoryRequests.setLayoutManager(linearLayoutManager);
        requestList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(HistoryActivity.this,requestList);
        rcvHistoryRequests.setAdapter(historyAdapter);

        myRequestsRef.orderByChild("email").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Request request = postSnapshot.getValue(Request.class);
                    requestList.add(request);
                }
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}