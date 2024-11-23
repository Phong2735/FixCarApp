package com.example.fixcarapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fixcarapp.TaoYeuCau.Request;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{

    private final Context context;
    private final ArrayList<Request> requests;

    public HistoryAdapter(Context context, ArrayList<Request> requests) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        Request request = requests.get(position);
        if (request == null){
            return;
        }
        holder.tv_incident.setText(String.format("Sự cố: %s", request.getIncident()));
        holder.tv_problem.setText(String.format("Tình trạng: %s", request.getProblem()));
        holder.tv_vehicle.setText(String.format("Loại phương tiện: %s", request.getVehicle()));
        holder.tv_address.setText(request.getAddress());
        holder.tv_phone.setText(String.format("Số điện thoại: %s", request.getPhone()));
        holder.tv_time.setText(String.format("Thời gian: %s", request.getTime()));
        holder.tv_status.setText(String.format("Trạng thái: %s", "PENDING".equals(request.getStatus()) ? "Chờ xác nhận" : "Đã xác nhận"));

        Glide.with(context).load(request.getScenePhoto()).placeholder(R.drawable.ic_notfield).into(holder.imv_scene_photo);

        holder.imv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("longitude", request.getLongitude());
                intent.putExtra("latitude", request.getLatitude());
                intent.putExtra("currentLocation", request.getAddress());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        if(requests !=null)
            return requests.size();
        return 0;
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_incident, tv_problem, tv_vehicle, tv_address,tv_phone,tv_status,tv_time;
        ImageView imv_scene_photo,imv_location;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_incident = itemView.findViewById(R.id.tv_incident);
            tv_problem = itemView.findViewById(R.id.tv_problem);
            tv_vehicle = itemView.findViewById(R.id.tv_vehicle);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_status = itemView.findViewById(R.id.tv_status);
            imv_scene_photo = itemView.findViewById(R.id.imv_scene_photo);
            imv_location = itemView.findViewById(R.id.imv_location);
            tv_time = itemView.findViewById(R.id.tv_time);

        }
    }
}