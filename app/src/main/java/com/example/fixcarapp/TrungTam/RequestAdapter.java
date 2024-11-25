package com.example.fixcarapp.TrungTam;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.fixcarapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private Context context;
    private List<Request> requestList;

    public RequestAdapter(Context context, List<Request> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.tvId.setText(String.valueOf(request.getId()));
        holder.tvAddress.setText(request.getAddress());
        holder.tvProblem.setText(request.getProblem());
        holder.tvVehicle.setText(request.getVehicle());
        holder.tvPhone.setText(request.getPhone());    // Hiển thị số điện thoại


        // Xử lý sự kiện khi nhấn nút "Xác nhận"
//        holder.btnConfirm.setOnClickListener(v -> {
//            // Cập nhật trạng thái "accepted" và gửi email
//            updateRequestStatus(request.getId(), "accepted", request.getEmail());
//        });

        // Sự kiện cho nút "Xác nhận"
        holder.btnConfirm.setOnClickListener(v -> {
            updateRequestStatus(request.getId()); // Gọi hàm cập nhật trạng thái
        });

        // Set an OnClickListener on the entire item (or a button specifically)
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to navigate to the Detail activity
            Intent intent = new Intent(context, DetailActivity.class);

            // Pass the necessary data
            intent.putExtra("REQUEST_ID", request.getId());
            intent.putExtra("ADDRESS", request.getAddress());
            intent.putExtra("PROBLEM", request.getProblem());
            intent.putExtra("VEHICLE", request.getVehicle());
            intent.putExtra("PHONE", request.getPhone());
            intent.putExtra("SCENE_PHOTO", request.getScenePhoto());

            // Start the Detail activity
            context.startActivity(intent);
        });


        // Sử dụng Glide để tải ảnh từ URL
        Glide.with(context)
                .load(request.getScenePhoto()) // Link ảnh từ Firebase
//                .placeholder(R.drawable.placeholder) // Ảnh mặc định khi đang tải
//                .error(R.drawable.error_image) // Ảnh hiển thị khi lỗi
                .into(holder.imgScenePhoto); // ImageView hiển thị ảnh
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress, tvProblem, tvVehicle, tvStatus, tvPhone, tvId, tv_Email;
        ImageView imgScenePhoto;

        Button btnConfirm ; // Thêm nút "Xác nhận"

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvProblem = itemView.findViewById(R.id.tvProblem);
            tvVehicle = itemView.findViewById(R.id.tvVehicle);
            tvPhone = itemView.findViewById(R.id.tvPhone);   // Ánh xạ TextView phone
            btnConfirm = itemView.findViewById(R.id.btnConfirm); // ánh xạ nút
            imgScenePhoto = itemView.findViewById(R.id.imgScenePhoto);
        }
    }

//    private void updateRequestStatus(int requestId, String newStatus, String email) {
//        // Tham chiếu đến Firebase Database
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("requests");
//
//        // Tạo HashMap để cập nhật nhiều trường
//        HashMap<String, Object> updates = new HashMap<>();
//        updates.put("status", newStatus);  // Trường status
//        updates.put("email", email);      // Trường email
//
//        // Cập nhật trạng thái mới và email
//        databaseReference.child(String.valueOf(requestId)).updateChildren(updates)
//                .addOnSuccessListener(aVoid -> {
//                    Toast.makeText(context, "Đã xác nhận cứu hộ!", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(context, "Lỗi khi xác nhận cứu hộ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }
private void updateRequestStatus(int requestId) {
    // Tham chiếu đến nút "requests" trong Firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests");

    // Cập nhật trường status thành "accepted"
    databaseReference.child(String.valueOf(requestId)).child("status").setValue("ACCEPTED")
            .addOnSuccessListener(aVoid -> {
                // Thông báo thành công
                Toast.makeText(context, "Trạng thái đã được cập nhật thành accepted!", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                // Thông báo lỗi
                Toast.makeText(context, "Cập nhật trạng thái thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
}
}

