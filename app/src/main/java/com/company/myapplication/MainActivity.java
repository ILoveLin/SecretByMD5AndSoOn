package com.company.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.company.myapplication.base64.Base64Utils;
import com.company.myapplication.webservicev2.WebserviceV2Activity;

import java.security.PrivateKey;
import java.security.PublicKey;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        tv_str = findViewById(R.id.tv_str);
        tv_02 = findViewById(R.id.tv_02);
        tv_03 = findViewById(R.id.tv_03);
        btn_webv2 = findViewById(R.id.btn_webv2);
        AppCompatButton btn_en = findViewById(R.id.btn_en);
        AppCompatButton btn_de = findViewById(R.id.btn_de);
        btn_en.setOnClickListener(this);
        btn_de.setOnClickListener(this);
        btn_webv2.setOnClickListener(this);
        source = tv_str.getText().toString().trim();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            --------------- 此处模拟android端数据使用<服务器端公钥>加密后传到服务器--------------
            // 加密
            case R.id.btn_en:
                Log.e("TAG","加密");
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
                Log.e("TAG","解密");

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
                Log.e("TAG","跳转");

                break;
            default:
                break;
        }
    }
}