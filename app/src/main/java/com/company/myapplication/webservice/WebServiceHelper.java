package com.company.myapplication.webservice;

import android.util.Log;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * LoveLin
 * <p> 帮助类,请求地址,以及请求
 * Describe
 */
public class WebServiceHelper {
//    天气预报Web Service
    private static final String NAMESPACE = "http://WebXml.com.cn/";
    public static String URL = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";
    private static final String TAG = "WebServiceHelper";
    public static String SHOW = "";
    private static Gson gson = new Gson();


    /**
     * 通用一个参数的方法
     *
     * @param map 参数集合
     * @return
     */
    public static String webService(Map<String, String> map, String method, String url) {
        String responseData = "";

        //注意版本使用，这个需要跟后台询问或者从wsdl文档或者服务说明中查看
        int envolopeVersion = SoapEnvelope.VER12;
        //可能是namspace+method拼接
        String soapAction = "http://WebXml.com.cn/getSupportCity";
        SoapObject request = new SoapObject(NAMESPACE, method);
        //参数一定注意要有序，尽管是addProperty（），不要当作HttpUrl可以使用LinkedHashMap封装

        for (String key : map.keySet()) {
            Log.e("WebServiceHelper", "key:" + key);
            Log.e("WebServiceHelper", "value:" + map.get(key));
            request.addProperty(key, map.get(key));
        }

//                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(envolopeVersion);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        HttpTransportSE transportSE = new HttpTransportSE(url);
        try {
            //                    se.call(soapAction, envelope);    //ver11，第一个参数不能为空
            transportSE.call(null, envelope);//envolopeVersion为ver12第一个参数可以为空，必须接口支持ver12才行
            SoapObject response = (SoapObject) envelope.bodyIn;
            if (envelope.getResponse()!=null){
                responseData = envelope.getResponse().toString();
            }
            //response的处理需要根据返回的具体情况，基本都要进行下面一步
            SoapObject o = (SoapObject) response.getProperty(0);
            //当前方法返回的结果为一个数组
            String s = gson.toJson(responseData);

            Log.e("zjy", "MainActivity.java->run(): size=" + o.getPropertyCount());
            Log.e("zjy", "run(): ------o.toString()========" + o.toString());
            Log.e("zjy", "run(): ------o.toJson()========" + s);


            for (int i = 0; i < o.getPropertyCount(); i++) {
                Log.e("zjy", "MainActivity.java->run(): ==" + o.getPropertyAsString(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return responseData;
    }
}