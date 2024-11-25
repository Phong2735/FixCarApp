package com.example.fixcarapp.TrungTam.DanhSachTrungTam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixcarapp.R;

import java.util.List;

public class AdapterCenter extends RecyclerView.Adapter<AdapterCenter.CenterViewHolder> {
    private List<Item_Center> list;
    private  clickItemListenner clickItemListenner;
    private Context context;
    public  interface  clickItemListenner {
        void onClickItem(Item_Center itemCenter);
    }

    public AdapterCenter(Context context, List<Item_Center> list,AdapterCenter.clickItemListenner clickItemListenner) {
        this.context = context;
        this.list = list;
        this.clickItemListenner = clickItemListenner;
    }

    @NonNull
    @Override
    public CenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_center_helper,parent,false);
        return new CenterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenterViewHolder holder, int position) {
        final Item_Center itemCenter = list.get(position);
        if(itemCenter==null) {
            return;
        }
        holder.tvName.setText(itemCenter.getTenCenter());
        holder.tvLocation.setText(itemCenter.getDiachiCenter());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItemListenner.onClickItem(itemCenter);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(list!=null)
            return list.size();
        return 0;
    }
    public class CenterViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgLogo;
        private TextView tvName,tvLocation;
        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            tvName = itemView.findViewById(R.id.tvName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
    public void updateAdapter(List<Item_Center> newData)
    {
        this.list = newData;
        notifyDataSetChanged();
    }
}
