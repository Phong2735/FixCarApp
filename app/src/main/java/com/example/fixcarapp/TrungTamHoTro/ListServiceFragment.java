package com.example.fixcarapp.TrungTamHoTro;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixcarapp.CenterDetailFragment;
import com.example.fixcarapp.DBHelper;
import com.example.fixcarapp.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ListServiceFragment extends Fragment {
    DBHelper dbHelper;
    ImageView imgClose,imgSearch;
    RecyclerView rcvListCenter;
    EditText edtSearch;
    TextView tvNotice;
    private  AdapterCenter adapter;
    SQLiteDatabase db;
    public static ListServiceFragment newInstance()
    {
        Bundle args = new Bundle();
        ListServiceFragment fragment = new ListServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_service, container, false);
        imgClose = view.findViewById(R.id.imgGoBack);
        rcvListCenter = view.findViewById(R.id.rcvListCenterHelper);
        dbHelper = new DBHelper(getActivity());
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
        List<Item_Center> itemCenters = new ArrayList<>();
        List<Item_Center> listCenterSearch = new ArrayList<>();
        Cursor cursor = dbHelper.getData("select * from db_center");
        while (cursor.moveToNext())
        {
            Item_Center itemCenter = new Item_Center();
            int logoIndex = cursor.getColumnIndex("logo");
            if(logoIndex!=-1)
            {
                byte[] logo = cursor.getBlob(logoIndex);
                itemCenter.setLogo(logo);
            }
            String tenCenter = cursor.getString(2);
            String sdt = cursor.getString(3);
            String diachi= cursor.getString(4);
            String email = cursor.getString(5);
            String mota = cursor.getString(6);
            // Lấy ID tài nguyên hình ảnh
            itemCenter.setTenCenter(tenCenter);
            itemCenter.setEmail(email);
            itemCenter.setDiachiCenter(diachi);
            itemCenter.setMota(mota);
            itemCenter.setSdt(sdt);
            itemCenters.add(itemCenter);
        }
        cursor.close();
        adapter = new AdapterCenter(itemCenters, new AdapterCenter.clickItemListenner() {
            @Override
            public void onClickItem(Item_Center itemCenter) {
                Bundle bundle = new Bundle();
                bundle.putByteArray("logo", itemCenter.getLogo());
                bundle.putString("ten", itemCenter.getTenCenter());
                bundle.putString("sdt", itemCenter.getSdt());
                bundle.putString("diachi", itemCenter.getDiachiCenter());
                bundle.putString("email", itemCenter.getEmail());
                bundle.putString("mota", itemCenter.getMota());

                CenterDetailFragment centerDetailFragment = new CenterDetailFragment();
                centerDetailFragment.setArguments(bundle);
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout1, centerDetailFragment)
                            .addToBackStack(null)
                            .commit();
            }
        });
        rcvListCenter.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvListCenter.setAdapter(adapter);
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
                        String tx2 = removeAccents( itemCenters.get(i).diachiCenter.trim().toLowerCase());
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
        imgClose.setOnClickListener(view1 -> {
            getParentFragmentManager().popBackStack();
        });
        return  view;
    }
    public static String removeAccents(String input) {
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}