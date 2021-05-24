package com.smartneck.twofive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.smartneck.twofive.Main.BleConnectActivity;
import com.smartneck.twofive.Member.LoginActivity;
import com.smartneck.twofive.util.HttpConnect;
import com.smartneck.twofive.util.NetworkStatus;
import com.smartneck.twofive.util.Param;
import com.smartneck.twofive.util.*;
import com.smartneck.twofive.util.User.Admin;


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
