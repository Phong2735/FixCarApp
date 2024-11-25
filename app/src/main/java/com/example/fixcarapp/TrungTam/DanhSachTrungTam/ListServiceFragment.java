package com.example.fixcarapp.TrungTam.DanhSachTrungTam;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixcarapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ListServiceFragment extends Fragment {
    ImageView imgClose,imgSearch;
    RecyclerView rcvListCenter;
    EditText edtSearch;
    TextView tvNotice;
    private  AdapterCenter adapter;
    List<Item_Center> itemCenters;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_service, container, false);
        rcvListCenter = view.findViewById(R.id.rcvListCenterHelper);
        edtSearch = view.findViewById(R.id.edtSearch);
        imgSearch = view.findViewById(R.id.imgSearch);
        tvNotice = view.findViewById(R.id.tvNotice);
        edtSearch.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (edtSearch.getRight() - edtSearch.getCompoundDrawables()[2].getBounds().width())) {
                    edtSearch.setText("");
                    return true;
                }
            }
            return false;
        });
        itemCenters = new ArrayList<>();
        List<Item_Center> listCenterSearch = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Centers");
        adapter = new AdapterCenter(getContext(),itemCenters, new AdapterCenter.clickItemListenner() {
            @Override
            public void onClickItem(Item_Center itemCenter) {
                CenterDetailFragment centerDetailFragment = CenterDetailFragment.newInstance(itemCenter);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout1, centerDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        rcvListCenter.setAdapter(adapter);
        rcvListCenter.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemCenters.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item_Center item = snapshot.getValue(Item_Center.class);
                    if (item != null) {
                        String tenCenter = item.getTenCenter();
                        String diachiCenter = item.getDiachiCenter();
                        String email = item.getEmail();
                        String mota = item.getMota();
                        String sdt = item.getSdt();

                        // Kiểm tra tất cả các thuộc tính khác null
                        if (tenCenter != null && !tenCenter.isEmpty() &&
                                diachiCenter != null && !diachiCenter.isEmpty() &&
                                email != null && !email.isEmpty() &&
                                mota != null && !mota.isEmpty() &&
                                sdt != null && !sdt.isEmpty()) {
                            itemCenters.add(item); // Thêm item vào danh sách
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Loi khi lay du lieu",Toast.LENGTH_SHORT).show();
            }
        });
        if(rcvListCenter!=null)
        {
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
            rcvListCenter.addItemDecoration(itemDecoration);
        }
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listCenterSearch.clear();
                String tx1 = removeAccents(edtSearch.getText().toString().trim().toLowerCase());
                if(tx1!=null)
                {
                    tvNotice.setVisibility(View.GONE);
                    for (int i = 0; i < itemCenters.size(); i++) {
                        String tx2 = removeAccents( itemCenters.get(i).getTenCenter().trim().toLowerCase());
                        if(tx2.contains(tx1)) {
                            listCenterSearch.add(itemCenters.get(i));
                        }
                    }
                }
                if(listCenterSearch==null)
                {
                    tvNotice.setVisibility(View.VISIBLE);
                    rcvListCenter.setVisibility(View.GONE);
                }
                adapter.updateAdapter(listCenterSearch);
            }
        });
        return  view;
    }
    public static String removeAccents(String input) {
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}