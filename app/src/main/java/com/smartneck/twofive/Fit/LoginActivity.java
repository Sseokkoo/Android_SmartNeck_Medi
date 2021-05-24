package com.smartneck.twofive.Fit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.smartneck.twofive.Fit.Join.Join_1_Activity;
import com.smartneck.twofive.Fit.Main.MainActivity;
import com.smartneck.twofive.Fit.util.Address;
import com.smartneck.twofive.Fit.util.HttpConnect;
import com.smartneck.twofive.Fit.util.Param;
import com.smartneck.twofive.Fit.util.User.User;

import static com.smartneck.twofive.Fit.FindAccountActivity.ACCOUNT;

public class LoginActivity extends AppCompatActivity {
    EditText et_member_id, et_member_password;
    CheckBox cb_auto_login;
    TextView tv_member_join, tv_member_account , tv_member_password;
    Button btn_login;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        init();



    }

    /* 초기화 */
    private void init(){
        handler = new Handler();

        et_member_id = findViewById(R.id.et_member_id);
        et_member_password = findViewById(R.id.et_member_password);
        btn_login = findViewById(R.id.btn_login);
        tv_member_account = findViewById(R.id.tv_member_account);
        tv_member_password = findViewById(R.id.tv_member_password);
        tv_member_join = findViewById(R.id.tv_member_join);
        cb_auto_login = findViewById(R.id.cb_auto_login);
        setEvent();


        /* 블루투스 권한설정 */
        int IS_ACCESS_COARSE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int IS_ACCESS_FINE_LOCATION = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (android.os.Build.VERSION.SDK_INT >= 29){
            if (IS_ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
        }else{
            if (IS_ACCESS_COARSE_LOCATION == PackageManager.PERMISSION_GRANTED && IS_ACCESS_FINE_LOCATION == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
        }
    }
    private void setEvent(){
        et_member_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_member_id.length() >= 8 && et_member_password.length() > 0) {
//                    btn_login.setBackgroundResource(R.drawable.button_background_enabled);
                    btn_login.setBackgroundColor(Color.parseColor("#cc1b17"));

                    btn_login.setTextColor(Color.parseColor("#ffffff"));
                } else {
//                    btn_login.setBackgroundResource(R.drawable.edit_text_background);
                    btn_login.setBackgroundColor(Color.parseColor("#bdbdbd"));
                    btn_login.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });

        tv_member_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "----- tv_member_join onClick");

                Intent intent = new Intent(getApplicationContext(), Join_1_Activity.class);
                startActivity(intent);
            }
        });

        tv_member_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext() , FindAccountActivity.class);
                startActivity(intent);
            }
        });
        tv_member_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , com.smartneck.twofive.Fit.FindPasswordActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "----- btn_login onClick");
                String id = et_member_id.getText().toString().trim();
                String pw = et_member_password.getText().toString().trim();
                login(id, pw);

            }
        });
    }


    private void login(final String id, final String pw) {

        User.setAutologinState(this, cb_auto_login.isChecked());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final HttpConnect httpConnect = new HttpConnect();
                Param param = new Param();
                param.add("id", id);
                param.add("pw", pw);
                final Address address = new Address();
                if (httpConnect.httpConnect(param.getParam(), address.getLogin()) == 200) {
                    if (!httpConnect.getReceiveMessage().equals("fail")) {
//                        User.MemberNo = Integer.parseInt(httpConnect.getReceiveMessage());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                User.getUserInfoJson(getApplicationContext(), httpConnect.getReceiveMessage());
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(com.smartneck.twofive.Fit.LoginActivity.this, getString(R.string.toast_enter_check), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(com.smartneck.twofive.Fit.LoginActivity.this, "internet connect check", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "run: "+address.getLogin());

                        }
                    });
                }
                Log.d(TAG, "response code: " + httpConnect.getResponseCode());
                Log.d(TAG, "receive message: " + httpConnect.getReceiveMessage());
            }
        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onActivityResult: ");
        if (resultCode == RESULT_OK){
            Log.d(TAG, "onActivityResult: " + data.getStringExtra("account"));
            et_member_id.setText(data.getStringExtra("account"));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ACCOUNT != null){
            et_member_id.setText(ACCOUNT);
            ACCOUNT = null;
        }
    }
}