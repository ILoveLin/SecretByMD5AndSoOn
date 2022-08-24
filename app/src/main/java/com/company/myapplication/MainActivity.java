package com.company.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.company.myapplication.base64.Base64Utils;
import com.company.myapplication.utils.RSAUtils;
import com.company.myapplication.webservicev2.WebserviceV2Activity;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;


import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

/**
 * 支付宝生成的RSA加密的公钥私钥测试
 * 有数据长度限制 1024
 * <p>
 * 结论：
 * <p>
 * RSA对token加解密，关于RSA加密
 * AES对消息内容加解密，关于AES加密
 * <p>
 * 在RSA中，服务器和客户端使用同一个公钥对token进行加密，服务器持有私钥可以解密token。
 * 而AES在服务器和客户端都是利用相同的token来产生密钥对，这样在客户端用AES加密的消息内容，发送到服务器之后用AES解密就可以得到消息内容明文。
 * 由于RSA计算量大，相当缓慢，但是安全系数高，故用来加密小段的token数据。
 * 由于AES计算量小，比RSA快，故用来加密大段的消息明文。
 * 一般的C/S模式中都是采用了RSA+AES来加密的方式。
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_str;
    private String source;
    private TextView tv_03;
    private TextView tv_02;
    // 注意copy密钥字符串时,不要有空格. 这对密钥必须是一对
//    private static String PUCLIC_KEY = "公钥";
//    private static String PRIVATE_KEY = "私钥";
    private static String PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFa1i3soqRUVgYVg3xhyK48VLnIvVaRhQOwp+8AvgrOjB+PNXYU2iImLvTbyxf452hzvt0q3gDVoMZPZUWjTR6AlvHi5dWDj7HPczaFcG5NEps4X0P6V4JJqZzTzqFBSG9s0dO9qosCjfkRbSbzvNkC2YKjr5tVKPXK7ADd9K1TwIDAQAB";
    private static String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIVrWLeyipFRWBhWDfGHIrjxUuci9VpGFA7Cn7wC+Cs6MH481dhTaIiYu9NvLF/jnaHO+3SreANWgxk9lRaNNHoCW8eLl1YOPsc9zNoVwbk0SmzhfQ/pXgkmpnNPOoUFIb2zR072qiwKN+RFtJvO82QLZgqOvm1Uo9crsAN30rVPAgMBAAECgYAo7W0hrNtlCJcFoCBW3yV/pkfQL6EkOCse/AUUH7URTaBimTbgTXXbsT6s9YWfRKuuX1gzygXewEMnEwURDe/yq80zBpKIeXp28xFkcmIP7yfLylTTLHg2tOMRtOGzcTORwU6MspCncTlIUWPlh3/obQY07YdCcbCECjTos7iEAQJBAMj/iTAthwvIrrPH3kqBcpobZxCB19N9dMWE+yLogj3lMh6aBOp4WZo1lek7LgHT1B8gkh0HqhoDoqqGGTmb4w8CQQCp7bqxUi/WUGX98YhTrKLhmSJQG4sx80iF4h/AFzpCt0LimpIORAf/VM0n2wjN+Iv9MwHR032Jo7KV1ND4nwnBAkAkSPVELEPf3bekHuXDP4EnzuCZO0dyF4jYC3ymvaCded3FZnqStW0/iILqmtNgYxQ9Fk0qTnLnxVY9QH4XR0wDAkAIFPt1JZCK4+YF7u/p8uMIUc00/CphpM2FOJ0D+NbXh3nR50IdFZU/ypu7UoqXqUOXNk7W2PU2j6eVwrpxqLbBAkBDQfHzPeq00ual6nU1J6NBuloiUs/+N7O/m7ZJH9pC90rNhOEHZsT2lasBaeM3IPDUoueh7cYSppWZIegUmfRh";
    private AppCompatButton btn_webv2;
    private TextView tv_image01;
    private TextView tv_image02;
    private ImageView image_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_str = findViewById(R.id.tv_str);
        image_bg = findViewById(R.id.image_bg);
        tv_02 = findViewById(R.id.tv_02);
        tv_03 = findViewById(R.id.tv_03);
        tv_image01 = findViewById(R.id.tv_image01);
        tv_image02 = findViewById(R.id.tv_image01);
        btn_webv2 = findViewById(R.id.btn_webv2);
        AppCompatButton btn_en = findViewById(R.id.btn_en);
        AppCompatButton btn_de = findViewById(R.id.btn_de);
        AppCompatButton btn_base6401 = findViewById(R.id.btn_base6401);
        AppCompatButton btn_base6402 = findViewById(R.id.btn_base6402);
        btn_en.setOnClickListener(this);
        btn_de.setOnClickListener(this);
        btn_webv2.setOnClickListener(this);
        tv_image01.setOnClickListener(this);
        tv_image02.setOnClickListener(this);
        btn_base6401.setOnClickListener(this);
        btn_base6402.setOnClickListener(this);
        source = tv_str.getText().toString().trim();
        getStoragePermission();

    }

    //取权限后才开始
    private void getStoragePermission() {
        XXPermissions.with(this)
                // 不适配 Android 11 可以这样写
                //.permission(Permission.Group.STORAGE)
                // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
//                .permission(Permission.CAMERA)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {

                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {

                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(getApplicationContext(), permissions);
                        } else {

                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //Base64图片加密
            case R.id.btn_base6401:
                tv_image01.setText("Base64图片加密中...");
                File base64Picture = new File("/sdcard/MyDownImages/af305fbeba7989fe_630/002.jpg");
                boolean exists = base64Picture.exists();
                String absolutePath = base64Picture.getAbsolutePath();
                Log.e("TAG", "exists===" + exists);
                String name = base64Picture.getName();
                String name1 = base64Picture.getPath();
                Log.e("TAG", "name===" + name);
                Log.e("TAG", "name1===" + name1);
                Log.e("TAG", "absolutePath===" + absolutePath);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            //图片转成base64
                            String content = Base64Utils.imageToBase64(absolutePath);
                            Log.e("TAG", "加密结果base64===" + content);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_image01.setText(content + "");
                                }
                            });
                        } catch (Exception e) {
                            Log.e("TAG", "加密结果base64==error=");
                            e.printStackTrace();
                        }
                    }
                }.start();

                break;
            //Base64图片解密
            case R.id.btn_base6402:
                String trim = tv_image01.getText().toString().trim();
                //方式一:转换成File存入本地,然后用Glide 加载图片
                try {
                    boolean b = Base64Utils.base64ToFile(trim, "/sdcard/MyDownImages/af305fbeba7989fe_630/001AA.jpg");
                    Log.e("TAG", "b===" + b);
                    File file = new File("/sdcard/MyDownImages/af305fbeba7989fe_630/001AA.jpg");
                    boolean exists1 = file.exists();
                    String absolutePath1 = file.getAbsolutePath();
                    Log.e("TAG", "exists1===" + exists1);
                    Log.e("TAG", "absolutePath1===" + absolutePath1);
                    Glide.with(MainActivity.this).load(file).into(image_bg);


//                    //方式二:直接转换成bitmap不存入,只做显示
//                    image_bg.setImageBitmap(Base64Utils.base64Decode2Bitmap(trim));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            //--------------- 此处模拟android端数据使用<服务器端公钥>加密后传到服务器--------------
            // 加密
            case R.id.btn_en:
                Log.e("TAG", "加密");
                try {
                    // 从字符串中得到公钥
                    PublicKey publicKey = RSAUtils.loadPublicKey(PUCLIC_KEY);
                    // 从文件中得到公钥
//                    InputStream inPublic = getResources().getAssets().open("rsa_public_key_me.pem");
//                    PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
                    // 加密
                    byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
                    // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
                    String afterencrypt = Base64Utils.encode(encryptByte);
                    tv_02.setText(afterencrypt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
//            -----------------这里模拟服务器端人员拿到加密后的数据用 < 服务器私钥 > 解密---------- -
            // 解密
            case R.id.btn_de:
                Log.e("TAG", "解密");

                String encryptContent = tv_02.getText().toString().trim();
                try {
                    // 从字符串中得到私钥
                    PrivateKey privateKey = RSAUtils.loadPrivateKey(PRIVATE_KEY);
                    // 从文件中得到私钥
//                    InputStream inPrivate = getResources().getAssets().open("rsa_private_key_pkcs8_me.pem");
//                    PrivateKey privateKey = RSAUtils.loadPrivateKey(inPrivate);
                    // 因为RSA加密后的内容经Base64再加密转换了一下，所以先Base64解密回来再给RSA解密
                    byte[] decryptByte = RSAUtils.decryptData(Base64Utils.decode(encryptContent), privateKey);
                    String decryptStr = new String(decryptByte);
                    tv_03.setText(decryptStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_webv2:
                Log.e("TAG", "跳转");
                Intent intent = new Intent(MainActivity.this, WebserviceV2Activity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}