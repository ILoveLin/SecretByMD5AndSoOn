package com.company.myapplication.aes;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.company.myapplication.R;
import com.company.myapplication.rsa.Person;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * AES 加密测试
 * AES对消息内容加解密，关于AES加密
 */
public class AesThreeActivity extends AppCompatActivity {

    private String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tow);
        initData();

    }

    private void initData() {
        List<Person> personList = new ArrayList<>();
        int testMaxCount = 1000;//测试的最大数据条数
        //添加测试数据
        for (int i = 0; i < testMaxCount; i++) {
            Person person = new Person();
            person.setAge(i + "");
            person.setName(String.valueOf(i));
            personList.add(person);
        }
        //FastJson生成json数据
        String jsonData = new Gson().toJson(personList);
        Log.e("MainActivity", "AES加密前json数据 ---->" + jsonData);
        Log.e("MainActivity", "AES加密前json数据长度 ---->" + jsonData.length());

        //生成一个动态key
        String secretKey = AesUtils.generateKey();
        Log.e("MainActivity", "AES动态secretKey ---->" + secretKey);

        //AES加密
        long start = System.currentTimeMillis();
        String encryStr = AesUtils.encrypt(secretKey, jsonData);
        long end = System.currentTimeMillis();
        Log.e("MainActivity", "AES加密耗时 cost time---->" + (end - start));
        Log.e("MainActivity", "AES加密后json数据 ---->" + encryStr);
        Log.e("MainActivity", "AES加密后json数据长度 ---->" + encryStr.length());
        //AES解密
        start = System.currentTimeMillis();
        String decryStr = AesUtils.decrypt(secretKey, encryStr);
        end = System.currentTimeMillis();
        Log.e("MainActivity", "AES解密耗时 cost time---->" + (end - start));
        Log.e("MainActivity", "AES解密后json数据 ---->" + decryStr);
    }

}