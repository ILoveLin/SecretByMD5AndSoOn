package com.company.myapplication.webservice;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.company.myapplication.R;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

/**
 * LoveLin
 * <p>
 * Describe webservice 测试
 */
public class WebFourthActivity extends AppCompatActivity implements RequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
//        SoapObject request = new SoapObject("", "method");
        Map<String, String> map = new HashMap<>();
        map.put("byProvinceName", "江西");
//        map.put("officeId", "1");
        new RequestThread(this, 1, map, "getSupportCity").start();

    }


    @Override
    public void onResponse(int Type, String response) {
        Log.e("TAG", "onResponse===response==" + response);

    }
}
