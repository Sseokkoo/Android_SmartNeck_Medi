package com.smartneck.twofive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.smartneck.twofive.Fit.Fit_LoginActivity;
import com.smartneck.twofive.Fit.Main.Fit_MainActivity;
import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_Preset;
import com.smartneck.twofive.Fit.util.User.Fit_User;
import com.smartneck.twofive.Main.BleConnectActivity;
import com.smartneck.twofive.Member.LoginActivity;
import com.smartneck.twofive.util.HttpConnect;
import com.smartneck.twofive.util.NetworkStatus;
import com.smartneck.twofive.util.Param;
import com.smartneck.twofive.util.*;
import com.smartneck.twofive.util.User.Admin;


import static com.smartneck.twofive.Fit.util.Fit_Constants.TAG;
import static com.smartneck.twofive.GlobalApplication.userPreference;
import static com.smartneck.twofive.Member.MemberSelectActivity.admin;

public class SelectType extends AppCompatActivity {

    Button med_start, fit_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_intro);

        med_start = findViewById(R.id.start_med);
        fit_start = findViewById(R.id.start_fit);


        med_start.setOnClickListener(v -> {
            autoLogin();
        });
        fit_start.setOnClickListener(v -> {
            String locale = getResources().getConfiguration().locale.getCountry();
            String language = getResources().getConfiguration().locale.getLanguage();
            Fit_User.language = language;
            Fit_User.country = locale;
            Log.d(Fit_Constants.TAG, "local: " + locale + " / lang: " + language);
            Fit_Constants constants = new Fit_Constants();
            constants.setAgeArray();
            setVolueHeight();
            fit_autoLogin();
        });
    }

    public void autoLogin() {

        String account = userPreference.getString("autoAccount" , "");
        String password = userPreference.getString("autoPassword" , "");
        boolean auto = userPreference.getBoolean("auto" , false);
        Log.d("자동로그인" , "auto login run\naccount -> "+account+"\npassword -> "+password+"\nauto -> "+auto);

        if (!auto){

            Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        if (NetworkStatus.CURRENT_STATUS != NetworkStatus.TYPE_NOT_CONNECTED){
            Log.d("자동ㄹ그인" , "NetWork State > Connected");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpConnect httpConnect = new HttpConnect();
                    Param param = new Param();
                    param.add("account" , account);
                    param.add("password" , password);
                    if (httpConnect.httpConnect(param.getValue() , new Address().getLogin() , true ) == 200){
                        if (httpConnect.getReceiveMessage() == "success"){
                            Intent intent = new Intent(getApplicationContext() , BleConnectActivity.class);
                            admin = userPreference.login(account,password);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }).start();


        }else{
            Log.d("확인" , "NetWork State > Not Connected");

            admin = userPreference.login(account , password);

            if (admin.getAccount().length() == 0){
                Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            Intent intent = new  Intent(getApplicationContext() , BleConnectActivity.class);
            startActivity(intent);
            finish();
        }

    }
    void setVolueHeight() {
        Log.d(TAG, "setVolueHeight: " + Fit_Preset.getHomeHeightValue(50));
    }

    /*
     * 자동로그인
     *
     * 자동로그인한적이 있는 사용자는 토큰인증 후 자동로그인 처리
     *
     * 자동 로그인 시 MainActivity로 이동
     *
     */

    void fit_autoLogin() {
        Fit_User.getToken(this);

        if (!Fit_User.getAutoLoginState(this)) {
            Intent intent = new Intent(getApplicationContext(), Fit_LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        Fit_Param param = new Fit_Param();
        param.add("token", Fit_User.token);
        Fit_Address address = new Fit_Address();
        Fit_HttpConnect httpConnect = new Fit_HttpConnect();
        Log.d(TAG, "param: " + param.getParam());
        if (httpConnect.httpConnect(param.getParam(), address.getAutoLogin()) == 200) {
            Log.d(TAG, "receive message : " + httpConnect.getReceiveMessage());
            if (!httpConnect.getReceiveMessage().equals("fail")) {
                Fit_User.getUserInfoJson(getApplicationContext(), httpConnect.getReceiveMessage());
                Intent intent = new Intent(getApplicationContext(), Fit_MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), Fit_LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), Fit_LoginActivity.class);
            startActivity(intent);
            finish();
            Log.d(TAG, "response code : " + httpConnect.getResponseCode());
        }

        if (httpConnect.httpConnect("", address.getYouTubeUrl()) == 200) {
            if (!httpConnect.getReceiveMessage().equals(Fit_Constants.FAIL)) {

                String[] urlSplit = httpConnect.getReceiveMessage().split("@");
                Fit_Address.YOUTUBE_HOW_TO_EXERCISE = urlSplit[0];
                Fit_Address.YOUTUBE_SUB_EXERCISE = urlSplit[1];
                Log.d(TAG, " - - - - - - - - - - YOUTUBE URL: " + Fit_Address.YOUTUBE_HOW_TO_EXERCISE);
//                        Address.domain = urlSplit[2];
            }
        }

        if (httpConnect.httpConnect("", address.getTermsOfUse()) == 200) {
            String split[] = httpConnect.getReceiveMessage().split("#");
            if (Fit_User.language.equals("ko")) {
                Fit_Constants.Terms1 = split[0];
                Fit_Constants.Terms2 = split[1];
                Fit_Constants.Terms3 = split[2];
                Fit_Constants.Terms4 = split[3];
            } else if (Fit_User.language.equals("en")) {
                Fit_Constants.Terms1 = split[4];
                Fit_Constants.Terms2 = split[5];
                Fit_Constants.Terms3 = split[6];
                Fit_Constants.Terms4 = split[7];
            } else {
                Fit_Constants.Terms1 = split[4];
                Fit_Constants.Terms2 = split[5];
                Fit_Constants.Terms3 = split[6];
                Fit_Constants.Terms4 = split[7];
            }

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
