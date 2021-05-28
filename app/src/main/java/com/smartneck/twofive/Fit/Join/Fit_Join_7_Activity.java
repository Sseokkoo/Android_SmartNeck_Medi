package com.smartneck.twofive.Fit.Join;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smartneck.twofive.R;
import com.smartneck.twofive.Fit.util.Fit_Address;
import com.smartneck.twofive.Fit.util.Fit_Constants;
import com.smartneck.twofive.Fit.util.Fit_HttpConnect;
import com.smartneck.twofive.Fit.util.Fit_Param;
import com.smartneck.twofive.Fit.util.User.Fit_User;

import static com.smartneck.twofive.Fit.Join.Fit_Join_1_Activity.activityList;

public class Fit_Join_7_Activity extends AppCompatActivity {
//    String Address = CFG_DOMAIN + "/_request_app/app_join.aspx";
    CheckBox cb_all , cb_1 , cb_2 , cb_3 , cb_4;
    ImageView btn_previous;
    Button btn_next;
    Handler handler;
    static String survey;
    static String survey2;
    TextView terms_1 , terms_2 , terms_3 , terms_4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fit_join_7_);

        init();
        onClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.clear();
    }
    void removeArrayitem(){
        if (activityList.size() == 1) {
            activityList.remove(activityList.size() - 1);
        }
    }
    void init(){
        cb_all = findViewById(R.id.cb_all);
        cb_1 = findViewById(R.id.cb_1);
        cb_2 = findViewById(R.id.cb_2);
        cb_3 = findViewById(R.id.cb_3);
        cb_4 = findViewById(R.id.cb_4);

        terms_1 = findViewById(R.id.join_7_terms_1);
        terms_2 = findViewById(R.id.join_7_terms_2);
        terms_3 = findViewById(R.id.join_7_terms_3);
        terms_4 = findViewById(R.id.join_7_terms_4);

        terms_1.setText(Fit_Constants.Terms1);
        terms_2.setText(Fit_Constants.Terms2);
        terms_3.setText(Fit_Constants.Terms3);
        terms_4.setText(Fit_Constants.Terms4);

        terms_1.setMovementMethod(new ScrollingMovementMethod());
        terms_2.setMovementMethod(new ScrollingMovementMethod());
        terms_3.setMovementMethod(new ScrollingMovementMethod());
        terms_4.setMovementMethod(new ScrollingMovementMethod());

        btn_next = findViewById(R.id.join_7_next);
        btn_previous = findViewById(R.id.join_7_previous);
        handler = new Handler();

    }

    void onClick(){
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cb_1.isChecked() || !cb_2.isChecked() || !cb_3.isChecked()){
                    Toast.makeText(Fit_Join_7_Activity.this, getString(R.string.toast_please_agree), Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String receive = "";
                        if (cb_4.isChecked()){
                            receive = "yes";
                        }else{
                            receive = "no";
                        }
                        Fit_Param param = new Fit_Param();
                        param.add("id" , Fit_Join_1_Activity.joinMail);
                        param.add("pw" , Fit_Join_2_Activity.joinPassword);
                        param.add("name" , Fit_Join_3_Activity.joinName);
                        param.add("gender" , Fit_Join_4_Activity.joinGender);
                        param.add("birth" , Fit_Join_4_Activity.joinBirth);
                        param.add("phone" , Fit_Join_5_Activity.joinPhone);
                        param.add("country" , Fit_User.country);
                        param.add("receive" , receive);
                        param.add("survey" , survey);
                        param.add("survey2" , survey2);

                        Fit_Address address = new Fit_Address();

                        String url = address.getJoin();
                        final Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                        if (httpConnect.httpConnect(param.getParam() , url) == 200){
                            Log.d(Fit_Constants.TAG, "run: " + httpConnect.getReceiveMessage());

                            if (!httpConnect.getReceiveMessage().equals("fail")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        for (int i = 0; i < activityList.size(); i++){
                                            activityList.get(i).finish();
                                        }


                                        finish();
                                        knhimJoin();

                                    }
                                });
                            }else{
                                Log.d(Fit_Constants.TAG, "run: " + httpConnect.getReceiveMessage());
                            }


                        }else{
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Fit_Join_7_Activity.this, String.valueOf(httpConnect.getResponseCode()), Toast.LENGTH_SHORT).show();
                                    Log.d(Fit_Constants.TAG, "receive message: " + httpConnect.getReceiveMessage());
                                }
                            });
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
        cb_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    cb_1.setChecked(true);
                    cb_2.setChecked(true);
                    cb_3.setChecked(true);
                }else if (!b && cb_1.isChecked() && cb_2.isChecked() && cb_3.isChecked()){
                    cb_1.setChecked(false);
                    cb_2.setChecked(false);
                    cb_3.setChecked(false);
                }
                setBtnColor();

            }
        });
        cb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    cb_all.setChecked(false);
                }
                setBtnColor();

            }
        });
        cb_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    cb_all.setChecked(false);
                }
                setBtnColor();

            }
        });
        cb_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    cb_all.setChecked(false);
                }
                setBtnColor();
            }
        });
    }

    void setBtnColor(){
        if (cb_1.isChecked() && cb_2.isChecked() && cb_3.isChecked())
            btn_next.setBackgroundColor(Color.parseColor("#cc1b17"));
        else
            btn_next.setBackgroundColor(Color.parseColor("#cccccc"));

    }

    private void knhimJoin(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                Fit_Param param = new Fit_Param();
                param.add("id" , Fit_Join_1_Activity.joinMail);
                param.add("passwd" , Fit_Join_2_Activity.joinPassword);
                param.add("name" , Fit_Join_3_Activity.joinName);
                param.add("gender" , Fit_Join_4_Activity.joinGender);
                param.add("birthday" , Fit_Join_4_Activity.joinBirth);
                param.add("phone" , Fit_Join_5_Activity.joinPhone);
                param.add("country" , Fit_User.country);


                Fit_Address address = new Fit_Address();

                String url = address.getJoin();
                final Fit_HttpConnect httpConnect = new Fit_HttpConnect();
                if (httpConnect.httpConnect(param.getParam() , url) == 200){
                    Log.d(Fit_Constants.TAG, "run: " + httpConnect.getReceiveMessage());

//                    if (!httpConnect.getReceiveMessage().equals("fail")){
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                for (int i = 0; i < activityList.size(); i++){
//                                    activityList.get(i).finish();
//                                }
//
//
//                                finish();
//                            }
//                        });
//                    }else{
//                        Log.d(Constants.TAG, "run: " + httpConnect.getReceiveMessage());
//                    }


                }else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Fit_Join_7_Activity.this, String.valueOf(httpConnect.getResponseCode()), Toast.LENGTH_SHORT).show();
                            Log.d(Fit_Constants.TAG, "receive message: " + httpConnect.getReceiveMessage());
                        }
                    });
                }

            }
        }).start();

    }
}
