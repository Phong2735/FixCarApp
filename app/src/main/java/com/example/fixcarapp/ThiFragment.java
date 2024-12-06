package com.example.fixcarapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ThiFragment extends Fragment {
    ImageView imgBack;
    TextView tvLight;
    Button btnSubmitLight;
    EditText edtLight;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thi, container, false);
        imgBack = view.findViewById(R.id.imgBack);
        tvLight = view.findViewById(R.id.tvLight);
        btnSubmitLight = view.findViewById(R.id.btnSubmitLight);
        edtLight = view.findViewById(R.id.edtLight);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            lightEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float lightLevel = event.values[0]; // Độ sáng đo được (lux)
                    tvLight.setText("Độ sáng hiện tại: " + lightLevel + " lux");
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // Không cần xử lý trong trường hợp này
                }
            };
        } else {
            tvLight.setText("Thiết bị không hỗ trợ cảm biến ánh sáng.");
        }
        if(getArguments()!=null)
        {
            String lightlevel = getArguments().getString("lightlevel");
            tvLight.setText("Độ sáng hiện tại: "+ lightlevel.toString());
        }
        btnSubmitLight.setOnClickListener( view1 -> {
        });
        imgBack.setOnClickListener(v-> {
            getParentFragmentManager().popBackStack();
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (lightSensor != null) {
            sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lightSensor != null) {
            sensorManager.unregisterListener(lightEventListener);
        }
    }
}