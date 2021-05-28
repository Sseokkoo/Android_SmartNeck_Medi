package com.smartneck.twofive.Fit.Join;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.User.Fit_User;
import com.smartneck.twofive.R;

public class Fit_Join_1_Activity extends AppCompatActivity {
    Button btn_next;
    static String joinMail;
    EditText edt_mail;
    ImageView btn_previous;
    static ArrayList<Activity> activityList;
//    String Address = CFG_DOMAIN + "/_request_app/app_join_id_check.aspx";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fit_join_1_);
        init();
        onClick();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(Fit_Constants.TAG, "run: " + activityList.size());
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        removeArrayitem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeArrayitem();

    }
    void removeArrayitem(){
        if (activityList.size() == 1) {
            activityList.remove(activityList.size() - 1);
        }
    }
    void init() {
        handler = new Handler();
        activityList = new ArrayList();
        btn_next = findViewById(R.id.join_1_next);
        edt_mail = findViewById(R.id.join_1_edt);
        btn_previous = findViewById(R.id.join_1_previous);

        String locale = getResources().getConfiguration().locale.getCountry();

        Fit_User.country = locale;
    }

    void onClick() {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_mail.length() == 0) {
                    Toast.makeText(Fit_Join_1_Activity.this, getString(R.string.login_insert_id), Toast.LENGTH_SHORT).show();
                    return;
                }


                final String id = edt_mail.getText().toString().trim();


                if (!validate(id)){
                    Toast.makeText(Fit_Join_1_Activity.this, getString(R.string.toast_mail), Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                        Fit_Address address = new Fit_Address();


                        Log.d(Fit_Constants.TAG, "onClick: "+id );

                        if (httpConnect.httpConnect("id=" + id, address.getCheckToID()) == 200) {
                            Log.d(Fit_Constants.TAG, "receive: " + httpConnect.getReceiveMessage());
                            if (httpConnect.getReceiveMessage().equals("success")) {
                                activityList.add(Fit_Join_1_Activity.this);
                                joinMail = edt_mail.getText().toString();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(getApplicationContext(), Fit_Join_2_Activity.class);
                                        startActivity(intent);
                                    }
                                });

                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(Fit_Join_1_Activity.this, getString(R.string.toast_exist_account), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                    }
                }).start();

            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edt_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_mail.length() == 0){
                    btn_next.setBackgroundColor(Color.parseColor("#cccccc"));
                }else{
                    btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
                }
            }
        });


    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
//            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);



    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
