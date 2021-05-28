package com.smartneck.twofive.Fit.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by GP62 on 2019-01-15.
 */

public class Fit_HttpConnect {
    int serverResponseCode = 0;
    String param , uri;
    public String receiveMessage;



    public int httpConnect(String param , String uri) {
        this.param = param;
        this.uri = uri;
        try {
/* 서버연결 */
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();

/* 안드로이드 -> 서버 파라메터값 전달 */
            OutputStream outs = conn.getOutputStream();
            outs.write(param.getBytes("UTF-8"));
            outs.flush();
            outs.close();

/* 서버 -> 안드로이드 파라메터값 전달 */
            serverResponseCode = conn.getResponseCode();
            InputStream is = null;
            BufferedReader in = null;

            //is = conn.getErrorStream();
            is = conn.getInputStream();
            in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
            String line = null;
            StringBuffer buff = new StringBuffer();
            while ((line = in.readLine()) != null) {
                buff.append(line + "\n");
            }
            //서버에서 받은 메시지
            receiveMessage = buff.toString().trim();
            Log.e("RECV DATA", receiveMessage);
            Log.e("RECV DATA", "여긴가: " + serverResponseCode );

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverResponseCode;
    }

    public String getReceiveMessage(){
        return receiveMessage;
    }
    public int getResponseCode(){
        return serverResponseCode;
    }

}
