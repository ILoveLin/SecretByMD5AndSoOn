package com.company.myapplication.rsa;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.company.myapplication.base64.Base64Decoder;
import com.company.myapplication.base64.Base64Encoder;
import com.company.myapplication.R;
import com.google.gson.Gson;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;


/**
 * RSA 加密不限制长度测试
 * RSA对token加解密，关于RSA加密
 */
public class RsaTowActivity extends AppCompatActivity {

    private String jsonData;
    private KeyPair keyPair;
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tow);
        initData();

    }

    private void initData() {
        List<Person> personList = new ArrayList<>();
        int testMaxCount = 10000;//测试的最大数据条数
        //添加测试数据
        for (int i = 0; i < testMaxCount; i++) {
            Person person = new Person();
            person.setAge(i + "");
            person.setName(String.valueOf(i));
            personList.add(person);
        }
        Gson mGson = new Gson();
        jsonData = mGson.toJson(personList);
        Log.e("MainActivity", "加密前json数据 ---->" + jsonData);
        Log.e("MainActivity", "加密前json数据长度 ---->" + jsonData.length());


        keyPair = RSAUtils.generateRSAKeyPair(RSAUtils.DEFAULT_KEY_SIZE);
        // 公钥
        publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Log.e("MainActivity", "加密===公钥===数据 ---->" + publicKey);
        Log.e("MainActivity", "加密===公钥==publicKey.getEncoded()==数据 ---->" + publicKey.getEncoded());
        Log.e("MainActivity", "加密===私钥===数据 ---->" + privateKey);
        Log.e("MainActivity", "加密===公钥==privateKey.getEncoded()==数据 ---->" + privateKey.getEncoded());

        try {
            iniText();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void iniText() throws Exception {
        //公钥加密
        long start=System.currentTimeMillis();
        byte[] encryptBytes=    RSAUtils.encryptByPublicKeyForSpilt(jsonData.getBytes(),publicKey.getEncoded());
        long end=System.currentTimeMillis();
        Log.e("MainActivity","公钥加密耗时 cost time---->"+(end-start));
        String encryStr=Base64Encoder.encode(encryptBytes);
        Log.e("MainActivity","加密后json数据 --1-->"+encryStr);
        Log.e("MainActivity","加密后json数据长度 --1-->"+encryStr.length());
        //私钥解密
        start=System.currentTimeMillis();
        byte[] decryptBytes=  RSAUtils.decryptByPrivateKeyForSpilt(Base64Decoder.decodeToBytes(encryStr),privateKey.getEncoded());
        String decryStr=new String(decryptBytes);
        end=System.currentTimeMillis();
        Log.e("MainActivity","私钥解密耗时 cost time---->"+(end-start));
        Log.e("MainActivity","解密后json数据 --1-->"+decryStr);

        //私钥加密
        start=System.currentTimeMillis();
        encryptBytes=    RSAUtils.encryptByPrivateKeyForSpilt(jsonData.getBytes(),privateKey.getEncoded());
        end=System.currentTimeMillis();
        Log.e("MainActivity","私钥加密密耗时 cost time---->"+(end-start));
        encryStr= Base64Encoder.encode(encryptBytes);
        Log.e("MainActivity","加密后json数据 --2-->"+encryStr);
        Log.e("MainActivity","加密后json数据长度 --2-->"+encryStr.length());
        //公钥解密
        start=System.currentTimeMillis();
        decryptBytes=  RSAUtils.decryptByPublicKeyForSpilt(Base64Decoder.decodeToBytes(encryStr),publicKey.getEncoded());
        decryStr=new String(decryptBytes);
        end=System.currentTimeMillis();
        Log.e("MainActivity","公钥解密耗时 cost time---->"+(end-start));
        Log.e("MainActivity","解密后json数据 --2-->"+decryStr);
    }

}