package com.company.myapplication.webservice;

import android.util.Log;

import java.util.Map;

/**
 * LoveLin
 * <p>
 * Describe
 */
public class RequestThread extends Thread {

    private RequestListener mListener;
    private Map<String, String> mMap;
    private String method;
    private int type;


    public RequestThread(RequestListener mListener, int type, Map<String, String> mMap, String method) {
        this.mListener = mListener;
        this.method = method;
        this.type = type;
        this.mMap = mMap;
    }

    @Override
    public void run() {
        String url = WebServiceHelper.URL;
        switch (type) {
            case 1:                 //为1模块的时候
                sendWebserviceRequest(url);

                break;
        }


    }

    private void sendWebserviceRequest(String url) {
        Log.e("Request====", "数据请求信息：" + type);
        String response = WebServiceHelper.webService(mMap, method, url);
        Log.e("Request====", "返回的登录信息：" + response);

        if (mListener != null) {
            mListener.onResponse(type, response);
        }
    }


}
